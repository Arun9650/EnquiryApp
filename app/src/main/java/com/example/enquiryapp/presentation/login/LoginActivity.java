package com.example.enquiryapp.presentation.login;

import static com.example.enquiryapp.util.Util.getEditTextInput;

import android.content.Intent;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.R;
import com.example.enquiryapp.databinding.ActivityLoginBinding;
import com.example.enquiryapp.databinding.EditTextItemBinding;
import com.example.enquiryapp.presentation.base.BaseActivity;
import com.example.enquiryapp.presentation.enquiry.activity.EnquiryReportsActivity;
import com.example.enquiryapp.util.Util;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class LoginActivity extends BaseActivity<ActivityLoginBinding>
    implements View.OnClickListener {

  private final Consumer<LoginState> stateConsumer =
      state -> {
        if (state != null) {

          progressDialog.showHide(state.isLoading());

          if (!state.getErrorMessage().isBlank()) {
            Util.showToast(LoginActivity.this, state.getErrorMessage());
          }

          if (state.isLoggedIn()) {
            startEnquiryReportsActivity();
          }
        }
      };
  private LoginViewModel loginViewModel;

  @NonNull
  @Override
  protected OnBackPressedCallback getOnBackPressedCallback() {
    return new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        finish();
      }
    };
  }

  @Override
  protected ActivityLoginBinding getViewBinding() {
    return ActivityLoginBinding.inflate(getLayoutInflater());
  }

  @Override
  protected void setupViews() {
    initEditTexts();

    loginViewModel =
        new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);
  }

  @Override
  protected void setupClickListeners() {
    super.setupClickListeners();
    binding.loginBtn.setOnClickListener(this);
  }

  @Override
  protected void observe() {
    super.observe();
    disposable =
        loginViewModel
            .state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateConsumer, Timber::e);
  }

  private void initEditTexts() {
    EditTextItemBinding username = binding.usernameEt;
    username.iconIv.setImageResource(R.drawable.ic_user);
    username.til.setHint(R.string.username_mobile_no);
    username.til.setSuffixText(getString(R.string.required));

    EditTextItemBinding password = binding.passwordEt;
    password.iconIv.setImageResource(R.drawable.ic_padlock);
    password.til.setHint(R.string.enter_password);
    password.til.setSuffixText(getString(R.string.required));
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == binding.loginBtn.getId()) {
      if (isInternetNotConnected()) {
        showNoInternetDialog();
        return;
      }

      LoginState state = loginViewModel.state.getValue();
      if (state == null) {
        state = new LoginState();
      }

      state.setUsername(getEditTextInput(binding.usernameEt.et));
      state.setPassword(getEditTextInput(binding.passwordEt.et));

      loginViewModel.onEvent(LoginAction.LOGIN);
    }
  }

  private void startEnquiryReportsActivity() {
    Intent intent = new Intent(this, EnquiryReportsActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }
}

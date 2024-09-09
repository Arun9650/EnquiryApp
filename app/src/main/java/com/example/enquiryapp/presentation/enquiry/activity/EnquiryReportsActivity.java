package com.example.enquiryapp.presentation.enquiry.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.R;
import com.example.enquiryapp.databinding.ActivityEnquiryReportsBinding;
import com.example.enquiryapp.presentation.base.BaseActivity;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportAction;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportState;
import com.example.enquiryapp.presentation.enquiry.adapter.EnquiryReportAdapter;
import com.example.enquiryapp.presentation.enquiry.factory.EnquiryReportViewModelFactory;
import com.example.enquiryapp.presentation.enquiry.viewmodels.EnquiryReportViewModel;
import com.example.enquiryapp.presentation.login.LoginActivity;
import com.example.enquiryapp.util.Constants;
import com.example.enquiryapp.util.DataHelper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class EnquiryReportsActivity extends BaseActivity<ActivityEnquiryReportsBinding>
    implements View.OnClickListener {
  private static final int ADD_UPDATE_ENQUIRY_REPORT_CODE = 230;
  private EnquiryReportAdapter enquiryReportAdapter;
  private final Consumer<EnquiryReportState> stateConsumer =
      state -> {
        if (state != null) {

          progressDialog.showHide(state.isLoading());
          if (state.getReports().isEmpty()) {
            binding.enquiryReportsRv.setVisibility(View.GONE);
            binding.noReportsTv.setVisibility(View.VISIBLE);
            binding.refreshBtn.setVisibility(View.VISIBLE);
          } else {
            enquiryReportAdapter.updateList(state.getReports());
            binding.refreshBtn.setVisibility(View.GONE);
            binding.noReportsTv.setVisibility(View.GONE);
            binding.enquiryReportsRv.setVisibility(View.VISIBLE);
          }
        }
      };
  private EnquiryReportViewModel enquiryReportViewModel;
  private Disposable itemClickDisposable;

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
  protected ActivityEnquiryReportsBinding getViewBinding() {
    return ActivityEnquiryReportsBinding.inflate(getLayoutInflater());
  }

  @Override
  protected void setupViews() {
    setActionBar();
    enquiryReportAdapter = new EnquiryReportAdapter();
    binding.enquiryReportsRv.setAdapter(enquiryReportAdapter);

    enquiryReportViewModel =
        new ViewModelProvider(this, new EnquiryReportViewModelFactory())
            .get(EnquiryReportViewModel.class);
  }

  @Override
  protected void observe() {
    super.observe();

    disposable =
        enquiryReportViewModel
            .state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateConsumer, Timber::e);

    itemClickDisposable =
        enquiryReportAdapter
            .getItemClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                enquiryReport -> {
                  Intent intent =
                      new Intent(EnquiryReportsActivity.this, AddEditEnquiryActivity.class);
                  intent.putExtra(Constants.ENQUIRY_REPORT, enquiryReport);
                  startActivityForResult(intent, ADD_UPDATE_ENQUIRY_REPORT_CODE);
                },
                Timber::e);
  }

  private void setActionBar() {
    setSupportActionBar(binding.toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(R.string.enquiry_reports);
    }
  }

  @Override
  protected void setupClickListeners() {
    super.setupClickListeners();
    binding.addEnquiryReportBtn.setOnClickListener(this);
    binding.refreshBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == binding.addEnquiryReportBtn.getId()) {
      if (isInternetNotConnected()) {
        showNoInternetDialog();
        return;
      }
      startActivityForResult(
          new Intent(EnquiryReportsActivity.this, AddEditEnquiryActivity.class),
          ADD_UPDATE_ENQUIRY_REPORT_CODE);
    } else if (id == binding.refreshBtn.getId()) {
      if (isInternetNotConnected()) {
        showNoInternetDialog();
        return;
      }
      enquiryReportViewModel.onEvent(EnquiryReportAction.REFRESH);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == ADD_UPDATE_ENQUIRY_REPORT_CODE) {
      if (resultCode == RESULT_OK) {
        enquiryReportViewModel.onEvent(EnquiryReportAction.REFRESH);
      }
    }
  }

  protected void onDestroy() {
    super.onDestroy();
    if (itemClickDisposable != null && !itemClickDisposable.isDisposed()) {
      itemClickDisposable.dispose();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_logout) {
      logout();
      return true;
    } else if (id == R.id.action_refresh) {
      enquiryReportViewModel.onEvent(EnquiryReportAction.REFRESH);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void logout() {
    DataHelper.logout();
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    finish();
  }
}

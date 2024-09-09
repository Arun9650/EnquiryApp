package com.example.enquiryapp.presentation.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import com.example.enquiryapp.R;
import com.example.enquiryapp.util.ProgressDialog;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.DynamicColorsOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

  protected ProgressDialog progressDialog;
  protected AlertDialog noInternetDialog = null;
  protected OnBackPressedCallback onBackPressedCallback;
  protected T binding;
  protected Disposable disposable;
  private ConnectivityManager connectivityManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setColorOverlayTheme(R.style.Theme_EnquiryApp);
    binding = getViewBinding();
    setContentView(binding.getRoot());
    onBackPressedCallback = getOnBackPressedCallback();
    getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

    progressDialog = new ProgressDialog(this);
    progressDialog.show();

    setupViews();
    setupClickListeners();
    observe();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
    try {
      progressDialog.dismiss();
    } catch (Exception e) {
      Timber.e(e);
    }
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  @NonNull
  protected abstract OnBackPressedCallback getOnBackPressedCallback();

  protected abstract T getViewBinding();

  protected abstract void setupViews();

  protected void setupClickListeners() {
    // Implement click listeners setup here
  }

  protected void observe() {
    // Implement data observation here
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressedCallback.handleOnBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setColorOverlayTheme(int colorOverlay) {
    DynamicColors.applyToActivityIfAvailable(
        BaseActivity.this,
        new DynamicColorsOptions.Builder().setThemeOverlay(colorOverlay).build());
  }

  @Override
  protected void onResume() {
    super.onResume();
    showNoInternetDialog();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (noInternetDialog != null) {
      noInternetDialog.dismiss();
    }
  }

  protected boolean isInternetNotConnected() {
    if (connectivityManager == null) {
      connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
  }

  protected void showNoInternetDialog() {
    if (!isInternetNotConnected()) {
      return;
    }
    progressDialog.hide();
    noInternetDialog =
        new MaterialAlertDialogBuilder(this)
            .setTitle(R.string.no_internet_connection)
            .setMessage(R.string.no_internet_message)
            .setPositiveButton(
                R.string.ok,
                (dialog, id) -> {
                  dialog.dismiss();
                  if (isInternetNotConnected()) {
                    progressDialog.hide();
                    showNoInternetDialog();
                  }
                })
            .create();
    noInternetDialog.show();
  }
}

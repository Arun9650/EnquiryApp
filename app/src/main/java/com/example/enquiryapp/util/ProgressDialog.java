package com.example.enquiryapp.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.enquiryapp.R;
import timber.log.Timber;

public class ProgressDialog extends Dialog {

  public ProgressDialog(@NonNull Context context) {
    super(context);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getWindow() != null) {
      getWindow().setBackgroundDrawableResource(android.R.color.transparent);
      setContentView(R.layout.dialog_progress);
      setCancelable(false);
      setCanceledOnTouchOutside(false);
    }
  }

  public void showHide(boolean shouldShow) {
    if (shouldShow) {
      show();
    } else {
      hide();
    }
  }

  @Override
  public void hide() {
    if (isShowing()) {
      super.hide();
    }
  }

  @Override
  public void show() {
    if (!isShowing()) {
      super.show();
    }
  }

  @Override
  public void dismiss() {
    super.dismiss();
    try {
      super.dismiss();
    } catch (Exception e) {
      Timber.e(e);
    }
  }
}

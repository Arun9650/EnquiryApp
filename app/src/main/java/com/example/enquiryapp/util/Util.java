package com.example.enquiryapp.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {
  private Util() {}

  public static void showToast(@NonNull Context context, @Nullable String msg) {
    if (msg == null || msg.isBlank()) {
      return;
    }
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
  }

  @NonNull
  public static String getEditTextInput(@NonNull EditText editText) {
    if (editText.getText() != null) {
      return editText.getText().toString().trim();
    }
    return "";
  }

  public static int getEditTextInput(@NonNull EditText editText, int defVal) {
    try {
      return Integer.parseInt(editText.getText().toString().trim());
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return defVal;
    }
  }

  public static boolean isValidEmail(@NonNull String email) {
    String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern pattern = Pattern.compile(emailPattern);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public static int generateUniqueId() {
    return (int) System.currentTimeMillis();
  }
}

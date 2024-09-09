package com.example.enquiryapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public final class DataHelper {
  private static SharedPreferences sharedPreferences;

  private DataHelper() {}

  public static void initialize(@NonNull Context context) {
    sharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
  }

  public static void login(int id, @NonNull String employeeName) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Constants.EMPLOYEE_NAME, employeeName);
    editor.putInt(Constants.EMPLOYEE_ID, id);
    editor.putBoolean(Constants.IS_LOGGED_IN, true);
    editor.apply();
  }

  public static void logout() {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(Constants.EMPLOYEE_NAME, null);
    editor.putInt(Constants.EMPLOYEE_ID, -1);
    editor.putBoolean(Constants.IS_LOGGED_IN, false);
    editor.apply();
  }

  public static boolean isLoggedIn() {
    return sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false);
  }

  public static int getEmployeeId() {
    return sharedPreferences.getInt(Constants.EMPLOYEE_ID, -1);
  }

  public static String getEmployeeName() {
    return sharedPreferences.getString(Constants.EMPLOYEE_NAME, "");
  }
}

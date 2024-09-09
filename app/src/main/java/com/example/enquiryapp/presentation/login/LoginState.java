package com.example.enquiryapp.presentation.login;

import androidx.annotation.NonNull;
import com.example.enquiryapp.util.Copyable;

public class LoginState implements Copyable<LoginState> {
  private String username = "";
  private String password = "";
  private String errorMessage = "";
  private boolean isLoading = false;
  private boolean isLoggedIn = false;

  @NonNull
  public String getUsername() {
    return username;
  }

  public void setUsername(@NonNull String username) {
    this.username = username;
  }

  @NonNull
  public String getPassword() {
    return password;
  }

  public void setPassword(@NonNull String password) {
    this.password = password;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void setLoading(boolean loading) {
    isLoading = loading;
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    isLoggedIn = loggedIn;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(@NonNull String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @NonNull
  @Override
  public LoginState copy() {
    LoginState copy = new LoginState();
    copy.username = this.username;
    copy.password = this.password;
    copy.errorMessage = this.errorMessage;
    copy.isLoading = this.isLoading;
    copy.isLoggedIn = this.isLoggedIn;
    return copy;
  }
}

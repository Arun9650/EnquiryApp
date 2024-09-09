package com.example.enquiryapp.presentation.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.presentation.login.LoginViewModel;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(LoginViewModel.class)) {
      return (T) new LoginViewModel(DatabaseManager.getInstance());
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}

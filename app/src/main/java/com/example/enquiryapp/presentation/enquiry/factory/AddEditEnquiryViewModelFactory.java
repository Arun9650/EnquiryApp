package com.example.enquiryapp.presentation.enquiry.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.presentation.enquiry.viewmodels.AddEditEnquiryViewModel;

public class AddEditEnquiryViewModelFactory implements ViewModelProvider.Factory {

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(AddEditEnquiryViewModel.class)) {
      return (T) new AddEditEnquiryViewModel(DatabaseManager.getInstance());
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}

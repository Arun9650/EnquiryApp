package com.example.enquiryapp.presentation.enquiry.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.presentation.enquiry.viewmodels.EnquiryReportViewModel;

public class EnquiryReportViewModelFactory implements ViewModelProvider.Factory {

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(EnquiryReportViewModel.class)) {
      return (T) new EnquiryReportViewModel(DatabaseManager.getInstance());
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}

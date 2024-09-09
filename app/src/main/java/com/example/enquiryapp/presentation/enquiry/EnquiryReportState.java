package com.example.enquiryapp.presentation.enquiry;

import androidx.annotation.NonNull;
import com.example.enquiryapp.domain.EnquiryReport;
import com.example.enquiryapp.util.Copyable;
import java.util.ArrayList;
import java.util.List;

public class EnquiryReportState implements Copyable<EnquiryReportState> {
  private boolean isLoading = false;
  private List<EnquiryReport> reports = new ArrayList<>();

  public boolean isLoading() {
    return isLoading;
  }

  public void setLoading(boolean loading) {
    isLoading = loading;
  }

  @NonNull
  public List<EnquiryReport> getReports() {
    return reports;
  }

  public void setReports(@NonNull List<EnquiryReport> reports) {
    this.reports = reports;
  }

  @NonNull
  @Override
  public EnquiryReportState copy() {
    EnquiryReportState state = new EnquiryReportState();
    state.reports = this.reports;
    state.isLoading = this.isLoading;
    return state;
  }
}

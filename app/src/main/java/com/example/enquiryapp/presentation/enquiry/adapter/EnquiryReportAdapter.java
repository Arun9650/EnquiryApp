package com.example.enquiryapp.presentation.enquiry.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.enquiryapp.databinding.EnquiryReportItemBinding;
import com.example.enquiryapp.domain.EnquiryReport;
import com.example.enquiryapp.presentation.base.BaseAdapter;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class EnquiryReportAdapter extends BaseAdapter<EnquiryReportItemBinding, EnquiryReport> {

  public EnquiryReportAdapter() {
    initDiffer(EnquiryReportAdapter.this);
  }

  @Override
  public EnquiryReportItemBinding getLayoutBinding(LayoutInflater inflater, ViewGroup container) {
    return EnquiryReportItemBinding.inflate(inflater, container, false);
  }

  @Override
  public void bindView(
      EnquiryReportItemBinding viewBinding,
      int pos,
      EnquiryReport item,
      PublishSubject<EnquiryReport> itemClickSubject) {
    viewBinding.nameTv.setText(item.getName());
    viewBinding.mobileNumberTv.setText(item.getMobileNumber());
    viewBinding.getRoot().setOnClickListener(v -> itemClickSubject.onNext(item));
  }
}

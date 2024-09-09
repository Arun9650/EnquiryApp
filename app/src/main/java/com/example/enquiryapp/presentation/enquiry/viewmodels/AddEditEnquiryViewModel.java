package com.example.enquiryapp.presentation.enquiry.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.domain.EnquiryReport;
import com.example.enquiryapp.presentation.enquiry.AddEditEnquiryState;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportAction;
import com.example.enquiryapp.util.Util;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import java.sql.Date;
import timber.log.Timber;

public class AddEditEnquiryViewModel extends ViewModel {
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final DatabaseManager databaseManager;
  public BehaviorSubject<AddEditEnquiryState> state =
      BehaviorSubject.createDefault(new AddEditEnquiryState());
  private final Consumer<Throwable> throwableConsumer =
      throwable -> {
        Timber.e(throwable);
        AddEditEnquiryState enquiryState = state.getValue();
        if (enquiryState != null) {
          AddEditEnquiryState enquiryState2 = enquiryState.copy();
          enquiryState2.setLoading(false);
          enquiryState2.setFinish(true);
          enquiryState2.setErrorMessage("Something went wrong");
        }
      };

  public AddEditEnquiryViewModel(@NonNull DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public void onEvent(@NonNull EnquiryReportAction action) {
    if (action == EnquiryReportAction.ADD) {
      onAdd();
    } else if (action == EnquiryReportAction.UPDATE) {
      onUpdate();
    } else if (action == EnquiryReportAction.DELETE) {
      onDelete();
    } else if (action == EnquiryReportAction.EDIT_MODE) {
      onEnableEditMode();
    }
  }

  private void onEnableEditMode() {
    AddEditEnquiryState enquiryState = state.getValue();
    if (enquiryState != null) {
      AddEditEnquiryState newState = enquiryState.copy();
      newState.setInEditMode(true);
      state.onNext(newState.copy());
    }
  }

  private void onAdd() {
    Disposable disposable =
        Single.fromCallable(
                () -> {
                  AddEditEnquiryState newState = null;
                  if (state.getValue() != null) {
                    newState = state.getValue();
                    newState.setErrorMessage("");
                    newState.setLoading(true);
                    state.onNext(newState.copy());

                    String errorMsg = null;
                    if (newState.getName().isBlank()) {
                      errorMsg = "Please enter name";
                    } else if (newState.getMobileNumber().isBlank()) {
                      errorMsg = "Please enter mobile number";
                    } else if (!newState.getEmail().isBlank()
                        && !Util.isValidEmail(newState.getEmail())) {
                      errorMsg = "Please enter valid email";
                    } else if (newState.getAddress().isBlank()) {
                      errorMsg = "Please enter address";
                    } else {

                      EnquiryReport enquiryReport = new EnquiryReport();
                      enquiryReport.setName(newState.getName());
                      enquiryReport.setMobileNumber(newState.getMobileNumber());
                      enquiryReport.setEmail(newState.getEmail());
                      enquiryReport.setAddress(newState.getAddress());
                      enquiryReport.setLongitude(newState.getLongitude());
                      enquiryReport.setLatitude(newState.getLatitude());
                      enquiryReport.setEmployeeId(newState.getEmpId());
                      enquiryReport.setEmployeeName(newState.getEmpName());
                      enquiryReport.setEntryTime(new Date(System.currentTimeMillis()));

                      databaseManager.addEnquiryReport(enquiryReport);
                      newState.setFinish(true);
                    }
                    newState.setErrorMessage(errorMsg == null ? "" : errorMsg);

                    newState.setLoading(false);
                    state.onNext(newState.copy());
                  }

                  return newState;
                })
            .subscribeOn(Schedulers.io())
            .subscribe(newState -> state.onNext(newState), throwableConsumer);
    compositeDisposable.add(disposable);
  }

  private void onUpdate() {
    Disposable disposable =
        Single.fromCallable(
                () -> {
                  AddEditEnquiryState newState = null;
                  if (state.getValue() != null) {
                    newState = state.getValue();
                    newState.setErrorMessage("");
                    newState.setLoading(true);
                    state.onNext(newState.copy());

                    String errorMsg = null;
                    if (newState.getName().isBlank()) {
                      errorMsg = "Please enter name";
                    } else if (newState.getMobileNumber().isBlank()) {
                      errorMsg = "Please enter mobile number";
                    } else if (!newState.getEmail().isBlank()
                        && !Util.isValidEmail(newState.getEmail())) {
                      errorMsg = "Please enter valid email";
                    } else if (newState.getAddress().isBlank()) {
                      errorMsg = "Please enter address";
                    } else {

                      EnquiryReport enquiryReport = new EnquiryReport();
                      enquiryReport.setId(newState.getId());
                      enquiryReport.setName(newState.getName());
                      enquiryReport.setMobileNumber(newState.getMobileNumber());
                      enquiryReport.setEmail(newState.getEmail());
                      enquiryReport.setAddress(newState.getAddress());
                      enquiryReport.setLongitude(newState.getLongitude());
                      enquiryReport.setLatitude(newState.getLatitude());
                      enquiryReport.setEmployeeId(newState.getEmpId());
                      enquiryReport.setEmployeeName(newState.getEmpName());
                      enquiryReport.setEntryTime(new Date(System.currentTimeMillis()));

                      databaseManager.updateEnquiryReport(enquiryReport);
                      newState.setFinish(true);
                    }
                    newState.setErrorMessage(errorMsg == null ? "" : errorMsg);

                    newState.setLoading(false);
                    state.onNext(newState.copy());
                  }

                  return newState;
                })
            .subscribeOn(Schedulers.io())
            .subscribe(newState -> state.onNext(newState), throwableConsumer);
    compositeDisposable.add(disposable);
  }

  private void onDelete() {
    Disposable disposable =
        Single.fromCallable(
                () -> {
                  AddEditEnquiryState newState = null;
                  if (state.getValue() != null) {
                    newState = state.getValue();
                    newState.setErrorMessage("");
                    newState.setLoading(true);
                    state.onNext(newState.copy());
                    databaseManager.deleteEnquiryDetails(newState.getId());
                    newState.setFinish(true);
                    newState.setLoading(false);
                    state.onNext(newState.copy());
                  }

                  return newState;
                })
            .subscribeOn(Schedulers.io())
            .subscribe(newState -> state.onNext(newState), throwableConsumer);
    compositeDisposable.add(disposable);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    compositeDisposable.clear();
  }
}

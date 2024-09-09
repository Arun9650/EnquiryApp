package com.example.enquiryapp.presentation.enquiry.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportAction;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportState;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

public class EnquiryReportViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BehaviorSubject<EnquiryReportState> state = BehaviorSubject.createDefault(new EnquiryReportState());

  private final Consumer<Throwable> throwableConsumer =
      throwable -> {
        Timber.e(throwable);
        EnquiryReportState reportState = state.getValue();
        if (reportState != null) {
          EnquiryReportState reportState2 = reportState.copy();
          reportState2.setLoading(false);
        }
      };

  private final DatabaseManager databaseManager;
  public EnquiryReportViewModel(@NonNull final DatabaseManager databaseManager){
      this.databaseManager=databaseManager;
      getEnquiryReports();
  }


    public void onEvent(@NonNull EnquiryReportAction action) {
        if(action==EnquiryReportAction.REFRESH){
            getEnquiryReports();
        }
    }

    private void getEnquiryReports() {
        Disposable disposable =
                Single.fromCallable(
                                () -> {
                                    EnquiryReportState newState = null;
                                    if (state.getValue() != null) {
                                        newState = state.getValue();
                                        newState.setLoading(true);
                                        state.onNext(newState.copy());

                                        newState.setReports(databaseManager.getAllEnquiryDetails());

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

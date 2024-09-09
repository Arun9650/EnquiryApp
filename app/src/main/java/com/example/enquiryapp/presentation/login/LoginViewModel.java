package com.example.enquiryapp.presentation.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.example.enquiryapp.db.DatabaseManager;
import com.example.enquiryapp.domain.EmployeeDetails;
import com.example.enquiryapp.util.DataHelper;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

public class LoginViewModel extends ViewModel {

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final DatabaseManager databaseManager;
  public BehaviorSubject<LoginState> state = BehaviorSubject.createDefault(new LoginState());
  private final Consumer<Throwable> throwableConsumer =
      throwable -> {
        Timber.e(throwable);
        LoginState loginState = state.getValue();
        if (loginState != null) {
          LoginState loginState2 = loginState.copy();
          loginState2.setLoading(false);
          loginState2.setErrorMessage("Something went wrong");
        }
      };

  public LoginViewModel(@NonNull DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
    checkLogin();
  }

  private void checkLogin() {
    if (state.getValue() != null) {
      LoginState newState = state.getValue();
      newState.setErrorMessage("");
      newState.setLoading(true);
      state.onNext(newState.copy());
      newState.setLoggedIn(DataHelper.isLoggedIn());
      newState.setLoading(false);
      state.onNext(newState.copy());
    }
  }

  public void onEvent(@NonNull LoginAction action) {
    if (action == LoginAction.LOGIN) {
      onLoginAction();
    }
  }

  private void onLoginAction() {
    Disposable disposable =
        Single.fromCallable(
                () -> {
                  LoginState newState = null;
                  if (state.getValue() != null) {
                    newState = state.getValue();
                    newState.setErrorMessage("");
                    newState.setLoading(true);
                    state.onNext(newState.copy());

                    String errorMsg = null;
                    if (newState.getUsername().isBlank()) {
                      errorMsg = "Please enter username";
                    } else if (newState.getPassword().isBlank()) {
                      errorMsg = "Please enter password";
                    } else if (!databaseManager.isUsernameExists(newState.getUsername())) {
                      errorMsg = "Username not exist";
                    } else if (!databaseManager.isUsernameAndPasswordMatch(
                        newState.getUsername(), newState.getPassword())) {
                      errorMsg = "Username and Password not matched";
                    } else {
                      EmployeeDetails employeeDetails =
                          databaseManager.getEmployeeDetails(
                              newState.getUsername(), newState.getPassword());
                      DataHelper.login(employeeDetails.getEmpId(), employeeDetails.getEmpNam());
                      newState.setLoggedIn(DataHelper.isLoggedIn());
                    }
                    newState.setErrorMessage(errorMsg == null ? "" : errorMsg);

                    newState.setLoading(false);
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

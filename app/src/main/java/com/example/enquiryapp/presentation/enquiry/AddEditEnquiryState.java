package com.example.enquiryapp.presentation.enquiry;

import androidx.annotation.NonNull;
import com.example.enquiryapp.util.Copyable;

import java.sql.Date;
import java.util.Objects;

public class AddEditEnquiryState implements Copyable<AddEditEnquiryState> {

  private boolean isLoading = false;
  private boolean isInEditMode = false;
  private boolean isFinish = false;
  private String errorMessage = "";
  private String name = "";
  private String mobileNumber = "";
  private String email = "";
  private int empId = -1;
  private String empName = "";
  private int id = -1;
  private Date entryTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getEntryTime() {
    return entryTime;
  }

  public void setEntryTime(Date entryTime) {
    this.entryTime = entryTime;
  }

  private String longitude = "";
  private String latitude = "";
  private String address = "";

  public boolean isFinish() {
    return isFinish;
  }

  public void setFinish(boolean finish) {
    isFinish = finish;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void setLoading(boolean loading) {
    isLoading = loading;
  }

  public boolean isInEditMode() {
    return isInEditMode;
  }

  public void setInEditMode(boolean inEditMode) {
    isInEditMode = inEditMode;
  }

  @NonNull
  public String getAddress() {
    return address;
  }

  public void setAddress(@NonNull String address) {
    this.address = address;
  }

  @NonNull
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(@NonNull String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(@NonNull String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  @NonNull
  public String getEmail() {
    return email;
  }

  public void setEmail(@NonNull String email) {
    this.email = email;
  }

  @NonNull
  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(@NonNull String longitude) {
    this.longitude = longitude;
  }

  public int getEmpId() {
    return empId;
  }

  public void setEmpId(int empId) {
    this.empId = empId;
  }

  @NonNull
  public String getEmpName() {
    return Objects.requireNonNullElse(empName, "");
  }

  public void setEmpName(String empName) {
    this.empName = empName;
  }

  @NonNull
  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(@NonNull String latitude) {
    this.latitude = latitude;
  }

  @NonNull
  @Override
  public AddEditEnquiryState copy() {
    AddEditEnquiryState state = new AddEditEnquiryState();
    state.isLoading = this.isLoading;
    state.isFinish = this.isFinish;
    state.isInEditMode = this.isInEditMode;
    state.address = this.address;
    state.errorMessage = this.errorMessage;
    state.name = this.name;
    state.mobileNumber = this.mobileNumber;
    state.email = this.email;
    state.empId = this.empId;
    state.empName = this.empName;
    state.longitude = this.longitude;
    state.latitude = this.latitude;
    state.id = this.id;
    state.entryTime = this.entryTime;
    return state;
  }
}

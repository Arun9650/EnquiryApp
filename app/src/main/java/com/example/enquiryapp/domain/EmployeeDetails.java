package com.example.enquiryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.Objects;

public class EmployeeDetails implements Parcelable {
  public static final Creator<EmployeeDetails> CREATOR = new Creator<EmployeeDetails>() {
    @Override
    public EmployeeDetails createFromParcel(Parcel in) {
      return new EmployeeDetails(in);
    }

    @Override
    public EmployeeDetails[] newArray(int size) {
      return new EmployeeDetails[size];
    }
  };
  private int empId = -1;
  private String empNam = "";
  private String phoneNumber = "";
  private String emailId = "";
  private String userName = "";

  public EmployeeDetails(){}

  protected EmployeeDetails(Parcel in) {
    empId = in.readInt();
    empNam = in.readString();
    phoneNumber = in.readString();
    emailId = in.readString();
    userName = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(empId);
    dest.writeString(empNam);
    dest.writeString(phoneNumber);
    dest.writeString(emailId);
    dest.writeString(userName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @NonNull
  public String getEmailId() {
    return Objects.requireNonNullElse( emailId,"");
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public int getEmpId() {
    return empId;
  }

  public void setEmpId(int empId) {
    this.empId = empId;
  }

  @NonNull
  public String getEmpNam() {
    return Objects.requireNonNullElse(empNam, "");
  }

  public void setEmpNam(String empNam) {
    this.empNam = empNam;
  }

  @NonNull
  public String getPhoneNumber() {
    return Objects.requireNonNullElse(phoneNumber, "");
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getUserName() {
    return Objects.requireNonNullElse(userName, "");
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}

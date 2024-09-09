package com.example.enquiryapp.domain;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.sql.Date;
import java.util.Objects;

public class EnquiryReport implements Parcelable {

  public static final Creator<EnquiryReport> CREATOR = new Creator<EnquiryReport>() {
    @Override
    public EnquiryReport createFromParcel(Parcel in) {
      return new EnquiryReport(in);
    }

    @Override
    public EnquiryReport[] newArray(int size) {
      return new EnquiryReport[size];
    }
  };
  private int id;
  private int employeeId;
  private String employeeName = "";
  private String name = "";
  private String mobileNumber = "";
  private Date entryTime;
  private String email = "";
  private String address = "";
  private String longitude = "";
  private String latitude = "";

  public EnquiryReport() {}

  protected EnquiryReport(Parcel in) {
    id = in.readInt();
    employeeId = in.readInt();
    employeeName = in.readString();
    name = in.readString();
    mobileNumber = in.readString();
    email = in.readString();
    address = in.readString();
    longitude = in.readString();
    latitude = in.readString();
    entryTime=new Date(in.readLong());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(employeeId);
    dest.writeString(employeeName);
    dest.writeString(name);
    dest.writeString(mobileNumber);
    dest.writeString(email);
    dest.writeString(address);
    dest.writeString(longitude);
    dest.writeString(latitude);
    dest.writeLong(entryTime.getTime());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(int employeeId) {
    this.employeeId = employeeId;
  }

  @NonNull
  public String getEmployeeName() {
    return Objects.requireNonNullElse(employeeName, "");
  }

  public void setEmployeeName(@NonNull String employeeName) {
    this.employeeName = employeeName;
  }

  public Date getEntryTime() {
    return entryTime;
  }

  public void setEntryTime(Date entryTime) {
    this.entryTime = entryTime;
  }

  @NonNull
  public String getLongitude() {
    return Objects.requireNonNullElse(longitude, "");
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  @NonNull
  public String getLatitude() {
    return Objects.requireNonNullElse(latitude, "");
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  @NonNull
  public String getName() {
    return Objects.requireNonNullElse(name, "");
  }

  public void setName(String name) {
    this.name = name;
  }

  @NonNull
  public String getMobileNumber() {
    return Objects.requireNonNullElse(mobileNumber, "");
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  @NonNull
  public String getEmail() {
    return Objects.requireNonNullElse(email, "");
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @NonNull
  public String getAddress() {
    return Objects.requireNonNullElse(address, "");
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @NonNull
  @Override
  public String toString() {
    return "EnquiryReport{"
        + "id="
        + id
        + ", employeeId="
        + employeeId
        + ", employeeName='"
        + employeeName
        + '\''
        + ", name='"
        + name
        + '\''
        + ", mobileNumber='"
        + mobileNumber
        + '\''
        + ", entryTime="
        + entryTime
        + ", email='"
        + email
        + '\''
        + ", address='"
        + address
        + '\''
        + ", longitude='"
        + longitude
        + '\''
        + ", latitude='"
        + latitude
        + '\''
        + '}';
  }
}

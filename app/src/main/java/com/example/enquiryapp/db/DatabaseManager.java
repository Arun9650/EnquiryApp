package com.example.enquiryapp.db;

import android.os.StrictMode;
import androidx.annotation.NonNull;
import com.example.enquiryapp.domain.EmployeeDetails;
import com.example.enquiryapp.domain.EnquiryReport;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
  private static final String ip = "101.53.144.17";
  private static final String port = "1232";
  private static final String Classes = "net.sourceforge.jtds.jdbc.Driver";
  private static final String databaseName = "EmpTracker";
  private static final String userid = "tracker";
  private static final String password = "123456";
  private static final String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + databaseName;
  private static Connection connection = null;

  private DatabaseManager() {}

  public static DatabaseManager getInstance() {
    return InstanceHolder.instance;
  }

  private static void connect() {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    try {
      Class.forName(Classes);
      connection = DriverManager.getConnection(url, userid, password);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public void addEnquiryReport(@NonNull EnquiryReport enquiryReport) {
    PreparedStatement preparedStatement = null;

    try {
      connect();
      String query =
          "INSERT INTO EnquiryDetails "
              + "(custname, custphoneno, custemailid, custaddress, "
              + "latitude, longitude, entrytime, empid, empname) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

      preparedStatement = connection.prepareStatement(query);

      preparedStatement.setString(1, enquiryReport.getName());
      preparedStatement.setString(2, enquiryReport.getMobileNumber());
      preparedStatement.setString(3, enquiryReport.getEmail());
      preparedStatement.setString(4, enquiryReport.getAddress());
      preparedStatement.setString(5, enquiryReport.getLatitude());
      preparedStatement.setString(6, enquiryReport.getLongitude());
      preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
      preparedStatement.setInt(8, enquiryReport.getEmployeeId());
      preparedStatement.setString(9, enquiryReport.getEmployeeName());

      preparedStatement.executeUpdate();
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void updateEnquiryReport(@NonNull EnquiryReport enquiryReport) {
    PreparedStatement preparedStatement = null;

    try {
      connect();
      String query =
          "UPDATE EnquiryDetails SET "
              + "custname = ?, custphoneno = ?, custemailid = ?, "
              + "custaddress = ?, latitude = ?, longitude = ?, "
              + "entrytime = ?, empid = ?, empname = ? "
              + "WHERE enquiryid = ?";

      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, enquiryReport.getName());
      preparedStatement.setString(2, enquiryReport.getMobileNumber());
      preparedStatement.setString(3, enquiryReport.getEmail());
      preparedStatement.setString(4, enquiryReport.getAddress());
      preparedStatement.setString(5, enquiryReport.getLatitude());
      preparedStatement.setString(6, enquiryReport.getLongitude());
      preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
      preparedStatement.setInt(8, enquiryReport.getEmployeeId());
      preparedStatement.setString(9, enquiryReport.getEmployeeName());
      preparedStatement.setInt(10, enquiryReport.getId());

      preparedStatement.executeUpdate();
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public List<EnquiryReport> getAllEnquiryDetails() {
    List<EnquiryReport> enquiryReportList = new ArrayList<>();

    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      connect();

      String query = "SELECT * FROM EnquiryDetails";
      preparedStatement = connection.prepareStatement(query);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        EnquiryReport enquiryReport = new EnquiryReport();
        enquiryReport.setId(resultSet.getInt("enquiryid"));
        enquiryReport.setName(resultSet.getString("custname"));
        enquiryReport.setMobileNumber(resultSet.getString("custphoneno"));
        enquiryReport.setEmail(resultSet.getString("custemailid"));
        enquiryReport.setAddress(resultSet.getString("custaddress"));
        enquiryReport.setLatitude(resultSet.getString("latitude"));
        enquiryReport.setLongitude(resultSet.getString("longitude"));
        enquiryReport.setEntryTime(resultSet.getDate("entrytime"));
        enquiryReport.setEmployeeId(resultSet.getInt("empid"));
        enquiryReport.setEmployeeName(resultSet.getString("empname"));

        enquiryReportList.add(enquiryReport);
      }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return enquiryReportList;
  }

  public void disconnect() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public boolean isUsernameExists(@NonNull String username) {
    try {
      connect();
      String query = "SELECT COUNT(*) FROM EmpDetails WHERE empusername = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        int count = resultSet.getInt(1);
        return count > 0;
      }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isUsernameAndPasswordMatch(@NonNull String username, @NonNull String password) {
    try {
      connect();
      String query = "SELECT COUNT(*) FROM EmpDetails WHERE empusername = ? AND emppassword = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        int count = resultSet.getInt(1);
        return count > 0;
      }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    }
    return false;
  }

  @NonNull
  public EmployeeDetails getEmployeeDetails(@NonNull String username, @NonNull String password) {
    EmployeeDetails employeeDetails = new EmployeeDetails();
    try {
      connect();
      String query = "SELECT * FROM EmpDetails WHERE empusername = ? AND emppassword = ?";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        int empId = resultSet.getInt("empid");
        String empName = resultSet.getString("empname");
        String empPhoneNo = resultSet.getString("empphoneno");
        String empUsername = resultSet.getString("empusername");
        String empEmailId = resultSet.getString("empemailid");

        employeeDetails.setEmpId(empId);
        employeeDetails.setEmpNam(empName);
        employeeDetails.setPhoneNumber(empPhoneNo);
        employeeDetails.setUserName(empUsername);
        employeeDetails.setEmailId(empEmailId);
      }
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    }
    return employeeDetails;
  }

  public void deleteEnquiryDetails(int enquiryId) {
    try {
      String deleteQuery = "DELETE FROM EnquiryDetails WHERE enquiryid = ?";
      connect();
      PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
      preparedStatement.setInt(1, enquiryId);
      preparedStatement.executeUpdate();
    } catch (SQLException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  private static final class InstanceHolder {
    private static final DatabaseManager instance = new DatabaseManager();
  }
}

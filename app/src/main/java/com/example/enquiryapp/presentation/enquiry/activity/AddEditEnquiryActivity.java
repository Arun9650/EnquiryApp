package com.example.enquiryapp.presentation.enquiry.activity;

import static com.example.enquiryapp.util.Util.getEditTextInput;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.LocationManager;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.enquiryapp.R;
import com.example.enquiryapp.databinding.ActivityAddEditEnquiryBinding;
import com.example.enquiryapp.databinding.EditTextItemBinding;
import com.example.enquiryapp.domain.EnquiryReport;
import com.example.enquiryapp.presentation.base.BaseActivity;
import com.example.enquiryapp.presentation.enquiry.AddEditEnquiryState;
import com.example.enquiryapp.presentation.enquiry.EnquiryReportAction;
import com.example.enquiryapp.presentation.enquiry.factory.AddEditEnquiryViewModelFactory;
import com.example.enquiryapp.presentation.enquiry.viewmodels.AddEditEnquiryViewModel;
import com.example.enquiryapp.util.Constants;
import com.example.enquiryapp.util.DataHelper;
import com.example.enquiryapp.util.OnLocationResult;
import com.example.enquiryapp.util.Util;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Date;
import java.util.Locale;
import timber.log.Timber;
import android.location.Geocoder;
import android.location.Address;
import java.io.IOException;
import java.util.List;


public class AddEditEnquiryActivity extends BaseActivity<ActivityAddEditEnquiryBinding>
    implements View.OnClickListener, OnLocationResult {
  private static final String TAG = AddEditEnquiryActivity.class.getSimpleName();
  private FusedLocationProviderClient fusedLocationClient;
  private EnquiryReport enquiryReport;
  private final Consumer<AddEditEnquiryState> stateConsumer =
      state -> {
        if (state != null) {

          progressDialog.showHide(state.isLoading());

          Util.showToast(AddEditEnquiryActivity.this, state.getErrorMessage());

          onEditModeChange(state.isInEditMode());

          if (state.isFinish()) {
            setResult(RESULT_OK);
            finish();
          }
        }
      };
  private LocationManager locationManager;
  private String latitude, longitude;
  private AddEditEnquiryViewModel addEditEnquiryViewModel;

  private void onEditModeChange(boolean isEditMode) {
    if (enquiryReport == null) {
      return;
    }
    binding.nameEt.et.setEnabled(isEditMode);
    binding.mobileNumberEt.et.setEnabled(isEditMode);
    binding.emailEt.et.setEnabled(isEditMode);
    binding.addressEt.et.setEnabled(isEditMode);
  }

  @NonNull
  @Override
  protected OnBackPressedCallback getOnBackPressedCallback() {
    return new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
      }
    };
  }

  @Override
  protected ActivityAddEditEnquiryBinding getViewBinding() {
    return ActivityAddEditEnquiryBinding.inflate(getLayoutInflater());
  }

  @Override
  protected void setupViews() {

    getData();
    initEditTexts();
    setActionBar();
    binding.addEditReportBtn.setText(
        enquiryReport == null ? R.string.add_enquiry_report : R.string.update_enquiry_report);
    if (enquiryReport == null) {
      binding.lastEditTv.setVisibility(View.GONE);
    } else {
      if (enquiryReport.getEntryTime() != null) {
        String dateStr =
            new SimpleDateFormat("MMM d yyyy", Locale.getDefault())
                .format(new Date(enquiryReport.getEntryTime().getTime()));
        binding.lastEditTv.setText(String.format("Last edited : %s", dateStr));
        binding.lastEditTv.setVisibility(View.VISIBLE);
      }
      Timber.tag(TAG).d(enquiryReport.toString());
    }

    addEditEnquiryViewModel =
        new ViewModelProvider(this, new AddEditEnquiryViewModelFactory())
            .get(AddEditEnquiryViewModel.class);

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  protected void observe() {
    super.observe();
    disposable =
        addEditEnquiryViewModel
            .state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(stateConsumer, Timber::e);
  }

  private void getData() {
    if (getIntent() != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        enquiryReport =
            getIntent().getParcelableExtra(Constants.ENQUIRY_REPORT, EnquiryReport.class);
      } else {
        enquiryReport = getIntent().getParcelableExtra(Constants.ENQUIRY_REPORT);
      }
    }
  }

  private void setActionBar() {
    setSupportActionBar(binding.toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(
          enquiryReport == null ? R.string.add_enquiry_report : R.string.update_enquiry_report);
    }
  }

  private void initEditTexts() {
    EditTextItemBinding name = binding.nameEt;
    name.iconIv.setImageResource(R.drawable.ic_user);
    name.til.setHint(R.string.name_as_per_aadhar_card);
    name.til.setSuffixText(getString(R.string.required));

    EditTextItemBinding mobileNumber = binding.mobileNumberEt;
    mobileNumber.iconIv.setImageResource(R.drawable.ic_phone_book);
    mobileNumber.til.setHint(R.string.mobile_number);
    mobileNumber.til.setSuffixText(getString(R.string.required));

    EditTextItemBinding email = binding.emailEt;
    email.iconIv.setImageResource(R.drawable.ic_email);
    email.til.setHint(R.string.enter_email_id);
    email.til.setSuffixText(null);

    EditTextItemBinding address = binding.addressEt;
    address.iconIv.setImageResource(R.drawable.ic_adress);
    address.til.setHint(R.string.address);
    address.til.setSuffixText(getString(R.string.required));

    EditTextItemBinding empId = binding.empIdEt;
    empId.iconIv.setImageResource(R.drawable.ic_id_card);
    empId.til.setHint(null);
    empId.til.setSuffixText(getString(R.string.required));
    empId.et.setEnabled(false);
    empId.et.setText(String.format(Locale.getDefault(), "%d", DataHelper.getEmployeeId()));

    EditTextItemBinding empName = binding.empNameEt;
    empName.iconIv.setImageResource(R.drawable.ic_employee);
    empName.til.setHint(R.string.emp_name);
    empName.til.setSuffixText(getString(R.string.required));
    empName.et.setEnabled(false);
    empName.et.setText(DataHelper.getEmployeeName());

    EditTextItemBinding latitudeEt = binding.latitudeEt;
    latitudeEt.til.setHint(R.string.latitude);
    latitudeEt.et.setEnabled(false);
    latitudeEt.iconIv.setImageResource(R.drawable.ic_globe);

    EditTextItemBinding longitudeEt = binding.longitudeEt;
    longitudeEt.til.setHint(R.string.longitude);
    longitudeEt.et.setEnabled(false);
    longitudeEt.iconIv.setImageResource(R.drawable.ic_globe);

    latitudeEt.getRoot().setVisibility(View.GONE);
    latitudeEt.getRoot().setVisibility(View.GONE);

    if (enquiryReport != null) {
      name.et.setText(enquiryReport.getName());
      mobileNumber.et.setText(enquiryReport.getMobileNumber());
      email.et.setText(enquiryReport.getEmail());
      address.et.setText(enquiryReport.getAddress());
      empId.et.setText(String.format(Locale.getDefault(), "%d", enquiryReport.getEmployeeId()));
      empName.et.setText(enquiryReport.getEmployeeName());

      latitude = enquiryReport.getLatitude();
      longitude = enquiryReport.getLongitude();

      latitudeEt.et.setText(enquiryReport.getLatitude());
      latitudeEt.getRoot().setVisibility(View.VISIBLE);

      longitudeEt.et.setText(enquiryReport.getLongitude());
      longitudeEt.getRoot().setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void setupClickListeners() {
    super.setupClickListeners();
    binding.addEditReportBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == binding.addEditReportBtn.getId()) {
      if (isInternetNotConnected()) {
        showNoInternetDialog();
        return;
      }

      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {

        if (!isGpsEnabled()) {
          Util.showToast(AddEditEnquiryActivity.this, "Please enable gps");
          enableGpsSettings();
          return;
        }
        progressDialog.show();
        fusedLocationClient
            .getLastLocation()
            .addOnSuccessListener(
                this,
                location -> {
                  if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    onSuccess();
                  } else {
                    onError();
                  }
                  progressDialog.dismiss();
                });
      } else {
        progressDialog.dismiss();
        Util.showToast(
            AddEditEnquiryActivity.this, "Please grant location permission from settings");
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (enquiryReport != null) {
      getMenuInflater().inflate(R.menu.edit_report, menu);
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (enquiryReport != null) {
      int id = item.getItemId();
      if (id == R.id.action_edit) {
        item.setVisible(false);
        addEditEnquiryViewModel.onEvent(EnquiryReportAction.EDIT_MODE);
        return true;
      } else if (id == R.id.action_delete) {

        AddEditEnquiryState state = addEditEnquiryViewModel.state.getValue();
        if (state == null) {
          state = new AddEditEnquiryState();
        }

        if (enquiryReport != null) {
          state.setId(enquiryReport.getId());
        }

        addEditEnquiryViewModel.onEvent(EnquiryReportAction.DELETE);
        return true;
      }
    }
    return super.onOptionsItemSelected(item);
  }

 @Override
public void onSuccess() {
    binding.latitudeEt.et.setText(latitude);
    binding.longitudeEt.et.setText(longitude);

    // Get location name using Geocoder
    Geocoder geocoder = new Geocoder(AddEditEnquiryActivity.this, Locale.getDefault());
    try {
        List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        if (addresses != null && !addresses.isEmpty()) {
            String locationName = addresses.get(0).getAddressLine(0); // You can also use getLocality(), getCountryName(), etc.
            binding.latitudeEt.et.setText(locationName); // Display the location name instead of latitude
            binding.longitudeEt.et.setVisibility(View.GONE); // Hide longitude since we're using location name
        } else {
            Util.showToast(AddEditEnquiryActivity.this, "Unable to get location name");
        }
    } catch (IOException e) {
        Util.showToast(AddEditEnquiryActivity.this, "Error getting location name");
        Timber.e(e, "Geocoder failed");
    }

    AddEditEnquiryState state = addEditEnquiryViewModel.state.getValue();
    if (state == null) {
        state = new AddEditEnquiryState();
    }

    if (enquiryReport != null) {
        state.setId(enquiryReport.getId());
    }
    state.setName(getEditTextInput(binding.nameEt.et));
    state.setMobileNumber(getEditTextInput(binding.mobileNumberEt.et));
    state.setEmail(getEditTextInput(binding.emailEt.et));
    state.setAddress(getEditTextInput(binding.addressEt.et));
    state.setLatitude(latitude);
    state.setLongitude(longitude);
    state.setEmpId(getEditTextInput(binding.empIdEt.et, -1));
    state.setEmpName(getEditTextInput(binding.empNameEt.et));

    if (enquiryReport == null) {
        addEditEnquiryViewModel.onEvent(EnquiryReportAction.ADD);
    } else {
        addEditEnquiryViewModel.onEvent(EnquiryReportAction.UPDATE);
    }
}

  @Override
  public void onError() {
    runOnUiThread(() -> Util.showToast(AddEditEnquiryActivity.this, "Error getting location"));
  }

  private boolean isGpsEnabled() {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  private void enableGpsSettings() {
    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    startActivity(intent);
  }
}

package com.example.enquiryapp.util;

import androidx.annotation.NonNull;

public interface Copyable<T> {
  @NonNull
  T copy();
}

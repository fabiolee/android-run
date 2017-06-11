package com.blogspot.carirunners.run.util;

import com.blogspot.carirunners.run.api.ApiResponse;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiUtil {
    public static <T> LiveData<ApiResponse<T>> successCall(T data) {
        return createCall(Response.success(data));
    }

    public static <T> LiveData<ApiResponse<T>> createCall(Response<T> response) {
        MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue(new ApiResponse<>(response));
        return data;
    }
}

package com.project.scm.api;

import com.project.scm.model.request.LoginRequestDTO;
import com.project.scm.model.response.CustomerResponseDTO;
import com.project.scm.model.response.LoginResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);

    @GET("api/customer/user/{id}")
    Call<CustomerResponseDTO> getCustomerByUserId(@Path("id") Long id);
}

package com.project.scm.api;

import com.project.scm.model.request.LoginRequestDTO;
import com.project.scm.model.response.CustomerResponseDTO;
import com.project.scm.model.response.LoginResponseDTO;
import com.project.scm.model.response.ProductResponseDTO;
import com.project.scm.model.response.InvoiceResponseDTO;

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

    @GET("api/customerOrders")
    Call<java.util.List<com.project.scm.model.response.CustomerOrderResponseDTO>> getAllCustomerOrders();

    @GET("api/customerOrders/track")
    Call<com.project.scm.model.response.CustomerOrderResponseDTO> trackOrder(@retrofit2.http.Query("orderNumber") String orderNumber);

    @POST("api/customerOrders")
    Call<com.project.scm.model.response.CustomerOrderResponseDTO> createOrder(@Body com.project.scm.model.request.CustomerOrderRequestDTO dto);

    @GET("api/products")
    Call<java.util.List<ProductResponseDTO>> getAllProducts();

    @GET("api/invoices")
    Call<java.util.List<InvoiceResponseDTO>> getAllInvoices();
}

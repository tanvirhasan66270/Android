package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_dashboard);

        View mainView = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        View footer = findViewById(R.id.footerButtons);
        View bottomNav = findViewById(R.id.bottomNav);

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(header.getPaddingLeft(), systemBars.top, header.getPaddingRight(), header.getPaddingBottom());
            bottomNav.setPadding(bottomNav.getPaddingLeft(), bottomNav.getPaddingTop(), bottomNav.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        findViewById(R.id.btnDispatch).setOnClickListener(v -> {
            submitOrder();
        });

        // Navigation
        findViewById(R.id.btn_nav_home).setOnClickListener(v -> {
            startActivity(new Intent(this, Dashboard_Activity.class));
            finish();
        });

        findViewById(R.id.btn_nav_shipments).setOnClickListener(v -> {
            startActivity(new Intent(this, TrackingDashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_nav_billing).setOnClickListener(v -> {
            startActivity(new Intent(this, BillingPage.class));
            finish();
        });

        findViewById(R.id.btn_nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, SupportDesk.class));
            finish();
        });
        
        loadCustomerData();
    }
    
    private void loadCustomerData() {
        com.project.scm.session.SessionManager session = new com.project.scm.session.SessionManager(this);
        if (session.getCustomer() != null) {
            android.widget.TextView nameView = findViewById(R.id.tvCustomerName);
            if (nameView != null) {
                nameView.setText(session.getCustomer().getName() + " (" + session.getCustomer().getEmail() + ")");
            }
        }
    }

    private void submitOrder() {
        android.widget.EditText addressEt = findViewById(R.id.etAddress);
        String address = addressEt != null ? addressEt.getText().toString() : "";

        android.widget.Spinner paymentSpinner = findViewById(R.id.spinnerPayment);
        String paymentMethod = paymentSpinner != null && paymentSpinner.getSelectedItem() != null ? 
            paymentSpinner.getSelectedItem().toString() : "CASH";

        com.project.scm.model.request.CustomerOrderRequestDTO dto = new com.project.scm.model.request.CustomerOrderRequestDTO();
        com.project.scm.session.SessionManager session = new com.project.scm.session.SessionManager(this);
        if (session.getCustomer() != null) {
            dto.setCustomerId(session.getCustomer().getId());
        }
        
        dto.setDeliveryAddress(address);
        dto.setPaymentMethod(paymentMethod);
        dto.setServiceType("STANDARD");
        
        // Hardcode a product for demonstration since UI parsing is complex without IDs
        com.project.scm.model.request.OrderLineItemRequestDTO item = new com.project.scm.model.request.OrderLineItemRequestDTO();
        item.setProductId(1L);
        item.setQuantity(1);
        java.util.List<com.project.scm.model.request.OrderLineItemRequestDTO> items = new java.util.ArrayList<>();
        items.add(item);
        dto.setItems(items);

        com.project.scm.api.ApiService apiService = com.project.scm.api.ApiClient.getClient(getApplicationContext());
        apiService.createOrder(dto).enqueue(new retrofit2.Callback<com.project.scm.model.response.CustomerOrderResponseDTO>() {
            @Override
            public void onResponse(retrofit2.Call<com.project.scm.model.response.CustomerOrderResponseDTO> call, retrofit2.Response<com.project.scm.model.response.CustomerOrderResponseDTO> response) {
                if (response.isSuccessful()) {
                    android.widget.Toast.makeText(OrderDashboardActivity.this, "Order Dispatched!", android.widget.Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    android.widget.Toast.makeText(OrderDashboardActivity.this, "Failed to create order", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.project.scm.model.response.CustomerOrderResponseDTO> call, Throwable t) {
                android.widget.Toast.makeText(OrderDashboardActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}

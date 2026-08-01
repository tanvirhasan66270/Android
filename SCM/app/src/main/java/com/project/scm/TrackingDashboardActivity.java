package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrackingDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracking_dashboard);

        View mainView = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        View bottomNav = findViewById(R.id.bottomNav);

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(header.getPaddingLeft(), systemBars.top, header.getPaddingRight(), header.getPaddingBottom());
            bottomNav.setPadding(bottomNav.getPaddingLeft(), bottomNav.getPaddingTop(), bottomNav.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Bottom Navigation
        findViewById(R.id.btn_nav_home).setOnClickListener(v -> {
            startActivity(new Intent(this, Dashboard_Activity.class));
            finish();
        });

        findViewById(R.id.btn_nav_orders).setOnClickListener(v -> {
            startActivity(new Intent(this, OrderDashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_nav_shipments).setOnClickListener(v -> {
            // Already on Shipments
        });

        findViewById(R.id.btn_nav_billing).setOnClickListener(v -> {
            startActivity(new Intent(this, BillingPage.class));
            finish();
        });

        findViewById(R.id.btn_nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, SupportDesk.class));
            finish();
        });
        
        android.widget.EditText etTrackId = findViewById(R.id.etTrackId);
        View btnTrackNow = findViewById(R.id.btnTrackNow);
        if (etTrackId != null && btnTrackNow != null) {
            btnTrackNow.setOnClickListener(v -> {
                String trackId = etTrackId.getText().toString().trim();
                if (!trackId.isEmpty()) {
                    trackOrder(trackId);
                }
            });
        }
    }
    
    private void trackOrder(String orderNumber) {
        com.project.scm.api.ApiService apiService = com.project.scm.api.ApiClient.getClient(getApplicationContext());
        apiService.trackOrder(orderNumber).enqueue(new retrofit2.Callback<com.project.scm.model.response.CustomerOrderResponseDTO>() {
            @Override
            public void onResponse(retrofit2.Call<com.project.scm.model.response.CustomerOrderResponseDTO> call, retrofit2.Response<com.project.scm.model.response.CustomerOrderResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.project.scm.model.response.CustomerOrderResponseDTO order = response.body();
                    android.widget.Toast.makeText(TrackingDashboardActivity.this, "Order Status: " + order.getStatus(), android.widget.Toast.LENGTH_SHORT).show();
                    // Here we can populate the UI with order data
                } else {
                    android.widget.Toast.makeText(TrackingDashboardActivity.this, "Tracking not found", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.project.scm.model.response.CustomerOrderResponseDTO> call, Throwable t) {
                android.widget.Toast.makeText(TrackingDashboardActivity.this, "Error: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}

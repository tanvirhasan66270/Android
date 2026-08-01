package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BillingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_billing_page);

        View mainView = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        View bottomNav = findViewById(R.id.bottomNav);

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(header.getPaddingLeft(), systemBars.top, header.getPaddingRight(), header.getPaddingBottom());
            bottomNav.setPadding(bottomNav.getPaddingLeft(), bottomNav.getPaddingTop(), bottomNav.getPaddingRight(), systemBars.bottom);
            return insets;
        });

        // Navigation
        findViewById(R.id.btn_nav_home).setOnClickListener(v -> {
            startActivity(new Intent(this, Dashboard_Activity.class));
            finish();
        });

        findViewById(R.id.btn_nav_orders).setOnClickListener(v -> {
            startActivity(new Intent(this, OrderDashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_nav_shipments).setOnClickListener(v -> {
            startActivity(new Intent(this, TrackingDashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_nav_billing).setOnClickListener(v -> {
            // Already on Billing
        });

        findViewById(R.id.btn_nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, SupportDesk.class));
            finish();
        });

        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            // Handle drawer or menu
        });
        
        loadInvoices();
    }
    
    private void loadInvoices() {
        com.project.scm.api.ApiService apiService = com.project.scm.api.ApiClient.getClient(getApplicationContext());
        apiService.getAllInvoices().enqueue(new retrofit2.Callback<java.util.List<com.project.scm.model.response.InvoiceResponseDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.project.scm.model.response.InvoiceResponseDTO>> call, retrofit2.Response<java.util.List<com.project.scm.model.response.InvoiceResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    java.util.List<com.project.scm.model.response.InvoiceResponseDTO> invoices = response.body();
                    updateBillingUI(invoices);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.project.scm.model.response.InvoiceResponseDTO>> call, Throwable t) {
                android.widget.Toast.makeText(BillingPage.this, "Failed to load invoices", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBillingUI(java.util.List<com.project.scm.model.response.InvoiceResponseDTO> invoices) {
        double totalBalance = 0;
        int invoiceCount = invoices.size();
        
        for (com.project.scm.model.response.InvoiceResponseDTO inv : invoices) {
            totalBalance += inv.getDueAmount();
        }
        
        android.widget.TextView balanceText = findViewById(R.id.tvBalanceAmount);
        if (balanceText != null) {
            balanceText.setText(String.format(java.util.Locale.getDefault(), "৳%.2f", totalBalance));
        }
    }
}

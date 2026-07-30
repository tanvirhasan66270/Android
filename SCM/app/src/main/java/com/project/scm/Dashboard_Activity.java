package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Grid item: Track Product
        findViewById(R.id.btn_track_product).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, tracking_Dashboard.class)));

        // Grid item: Place New Order
        findViewById(R.id.btn_new_order).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, OrderDashboardActivity.class)));

        // Grid item: Billing Ledger
        findViewById(R.id.btn_billing_ledger).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, BillingPage.class)));

        // Grid item: Support Desk
        findViewById(R.id.btn_support_desk).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, SupportDesk.class)));

        // Bottom Nav item: Orders
        findViewById(R.id.btn_nav_orders).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, OrderDashboardActivity.class)));

        // Bottom Nav item: Shipments
        findViewById(R.id.btn_nav_shipments).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, tracking_Dashboard.class)));

        // Bottom Nav item: Billing
        findViewById(R.id.btn_nav_billing).setOnClickListener(v -> startActivity(new Intent(Dashboard_Activity.this, BillingPage.class)));

        // Log Out Button
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            startActivity(new Intent(Dashboard_Activity.this, Login_Activity.class));
            finish();
        });

    }
}
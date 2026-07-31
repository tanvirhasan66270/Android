package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SupportDesk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support_desk);

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

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            // Dummy submit logic
            finish();
        });

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
            startActivity(new Intent(this, TrackingDashboardActivity.class));
            finish();
        });

        findViewById(R.id.btn_nav_billing).setOnClickListener(v -> {
            startActivity(new Intent(this, BillingPage.class));
            finish();
        });

        findViewById(R.id.btn_nav_profile).setOnClickListener(v -> {
            // Already on Profile (SupportDesk)
        });

        EditText etIssue = findViewById(R.id.etIssue);
        TextView tvCounter = findViewById(R.id.tvCounter);

        etIssue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = (s != null) ? s.length() : 0;
                tvCounter.setText(length + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}

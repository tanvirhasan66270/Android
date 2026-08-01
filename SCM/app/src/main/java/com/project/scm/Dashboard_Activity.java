package com.project.scm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.scm.adaptor.OrderAdapter;
import com.project.scm.adaptor.PipelineAdapter;
import com.project.scm.adaptor.RecommendedProductAdapter;
import com.project.scm.model.response.CustomerOrderResponseDTO;
import com.project.scm.model.response.ProductResponseDTO;
import com.project.scm.session.SessionManager;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Dashboard_Activity extends AppCompatActivity {

    // Welcome Card views
    private TextView welcomeTitleName;

    // Wallet & Payments views
    private TextView totalBalance;

    // Stats Bar Grid views
    private TextView totalOrdersCount;
    private TextView activeOrder;
    private TextView deliveredValue;
    private TextView pendingValue;

    // Analysis & Activity views
    private View containerNoLogistics;

    // Recommended Product views
    private RecyclerView recyclerRecommended;
    private RecommendedProductAdapter recommendedProductAdapter;
    private final List<ProductResponseDTO> recommendedProductList = new ArrayList<>();

    // Pipeline Views
    private RecyclerView recyclerPipelineLogs;
    private PipelineAdapter pipelineAdapter;
    private final List<CustomerOrderResponseDTO> pipelineList = new ArrayList<>();

    // RecyclerView for Recent Orders
    private RecyclerView recyclerRecentOrders;
    private OrderAdapter orderAdapter;
    private final List<CustomerOrderResponseDTO> orderList = new ArrayList<>();
    private TextView findAllOrder;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);

        bindViews();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();

        loadUserData();
        loadRecentOrder();
        loadRecommendedProducts();
    }

    private void bindViews() {
        welcomeTitleName = findViewById(R.id.welcomeTitleName);

        totalBalance = findViewById(R.id.totalBalance);

        totalOrdersCount = findViewById(R.id.totalOrdersCount);
        activeOrder = findViewById(R.id.activeOrder);
        deliveredValue = findViewById(R.id.deliveredValue);
        pendingValue = findViewById(R.id.pendingValue);

        containerNoLogistics = findViewById(R.id.containerNoLogistics);
        recyclerPipelineLogs = findViewById(R.id.recyclerPipelineLogs);
        recyclerRecommended = findViewById(R.id.recyclerRecommended);
        recyclerRecentOrders = findViewById(R.id.recyclerRecentOrders);
        findAllOrder = findViewById(R.id.findAllOrder);
    }

    private void setupToolbar() {
        // Toolbar related setup
    }

    private void setupRecyclerView() {
        if (recyclerRecentOrders != null) {
            recyclerRecentOrders.setLayoutManager(new LinearLayoutManager(this));
            recyclerRecentOrders.setHasFixedSize(true);
            orderAdapter = new OrderAdapter(orderList, order -> {
                Intent intent = new Intent(Dashboard_Activity.this, TrackingDashboardActivity.class);
                startActivity(intent);
            });
            recyclerRecentOrders.setAdapter(orderAdapter);
        }

        if (recyclerPipelineLogs != null) {
            recyclerPipelineLogs.setLayoutManager(new LinearLayoutManager(this));
            pipelineAdapter = new PipelineAdapter(pipelineList);
            recyclerPipelineLogs.setAdapter(pipelineAdapter);
        }

        if (recyclerRecommended != null) {
            recyclerRecommended.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recommendedProductAdapter = new RecommendedProductAdapter(recommendedProductList, product -> {
                java.io.File pdfFile = com.project.scm.utils.PdfGenerator.generateProductPdf(this, product);
                if (pdfFile != null && pdfFile.exists()) {
                    android.net.Uri pdfUri = androidx.core.content.FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", pdfFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(pdfUri, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                        android.widget.Toast.makeText(Dashboard_Activity.this, "No PDF Viewer found", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });
            recyclerRecommended.setAdapter(recommendedProductAdapter);
        }
    }

    private void setupClickListeners() {
        View trackProduct = findViewById(R.id.btn_track_product);
        if (trackProduct != null) trackProduct.setOnClickListener(v -> startActivity(new Intent(this, TrackingDashboardActivity.class)));

        View newOrder = findViewById(R.id.btn_new_order);
        if (newOrder != null) newOrder.setOnClickListener(v -> startActivity(new Intent(this, OrderDashboardActivity.class)));

        View billingLedger = findViewById(R.id.btn_billing_ledger);
        if (billingLedger != null) billingLedger.setOnClickListener(v -> startActivity(new Intent(this, BillingPage.class)));

        View supportDesk = findViewById(R.id.btn_support_desk);
        if (supportDesk != null) supportDesk.setOnClickListener(v -> startActivity(new Intent(this, SupportDesk.class)));

        View navOrders = findViewById(R.id.btn_nav_orders);
        if (navOrders != null) navOrders.setOnClickListener(v -> startActivity(new Intent(this, OrderDashboardActivity.class)));

        View navShipments = findViewById(R.id.btn_nav_shipments);
        if (navShipments != null) navShipments.setOnClickListener(v -> startActivity(new Intent(this, TrackingDashboardActivity.class)));

        View navBilling = findViewById(R.id.btn_nav_billing);
        if (navBilling != null) navBilling.setOnClickListener(v -> startActivity(new Intent(this, BillingPage.class)));

        if (findAllOrder != null) findAllOrder.setOnClickListener(v -> startActivity(new Intent(this, OrderDashboardActivity.class)));

        View logout = findViewById(R.id.btn_logout);
        if (logout != null) logout.setOnClickListener(v -> {
            sessionManager.logout();
            startActivity(new Intent(this, Login_Activity.class));
            finish();
        });
    }

    private void loadUserData() {
        if (sessionManager.getCustomer() != null && welcomeTitleName != null) {
            welcomeTitleName.setText("Welcome Back, " + sessionManager.getCustomer().getName() + "!");
        }
    }

    private void loadRecentOrder() {
        com.project.scm.api.ApiService apiService = com.project.scm.api.ApiClient.getClient(getApplicationContext());
        apiService.getAllCustomerOrders().enqueue(new retrofit2.Callback<List<CustomerOrderResponseDTO>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<CustomerOrderResponseDTO>> call, @NonNull retrofit2.Response<List<CustomerOrderResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    if (orderAdapter != null) orderAdapter.notifyDataSetChanged();
                    updatePipelineLogs(orderList);
                    calculateAndShowStats(orderList);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<CustomerOrderResponseDTO>> call, @NonNull Throwable t) {
                android.widget.Toast.makeText(Dashboard_Activity.this, "Failed to load orders", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecommendedProducts() {
        com.project.scm.api.ApiService apiService = com.project.scm.api.ApiClient.getClient(getApplicationContext());
        apiService.getAllProducts().enqueue(new retrofit2.Callback<List<ProductResponseDTO>>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<List<ProductResponseDTO>> call, @NonNull retrofit2.Response<List<ProductResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recommendedProductList.clear();
                    recommendedProductList.addAll(response.body());
                    if (recommendedProductAdapter != null) recommendedProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<ProductResponseDTO>> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private void calculateAndShowStats(List<CustomerOrderResponseDTO> orders) {
        if (orders == null) return;
        int activeCount = 0, deliveredCount = 0, pendingCount = 0;
        double totalBalanceValue = 0;

        for (CustomerOrderResponseDTO order : orders) {
            totalBalanceValue += order.getTotalAmount();
            String status = order.getStatus();
            if (status != null) {
                switch (status) {
                    case "DELIVERED": deliveredCount++; break;
                    case "PENDING": pendingCount++; break;
                    case "CONFIRMED":
                    case "PROCESSING":
                    case "SHIPPED":
                    case "OUT_FOR_DELIVERY": activeCount++; break;
                }
            }
        }
        
        if (totalBalance != null) totalBalance.setText(String.format(java.util.Locale.getDefault(), "৳%.2f", totalBalanceValue));
        if (totalOrdersCount != null) totalOrdersCount.setText(String.valueOf(orders.size()));
        if (activeOrder != null) activeOrder.setText(String.valueOf(activeCount));
        if (deliveredValue != null) deliveredValue.setText(String.valueOf(deliveredCount));
        if (pendingValue != null) pendingValue.setText(String.valueOf(pendingCount));
    }

    private void updatePipelineLogs(List<CustomerOrderResponseDTO> orders) {
        if (orders == null || pipelineAdapter == null) return;
        pipelineList.clear();
        for (CustomerOrderResponseDTO order : orders) {
            String status = order.getStatus();
            if (status != null && (status.equals("CONFIRMED") || status.equals("PROCESSING") || 
                status.equals("SHIPPED") || status.equals("OUT_FOR_DELIVERY") || status.equals("DELIVERED"))) {
                pipelineList.add(order);
            }
        }
        pipelineAdapter.notifyDataSetChanged();
        if (pipelineList.isEmpty()) {
            if (containerNoLogistics != null) containerNoLogistics.setVisibility(View.VISIBLE);
            if (recyclerPipelineLogs != null) recyclerPipelineLogs.setVisibility(View.GONE);
        } else {
            if (containerNoLogistics != null) containerNoLogistics.setVisibility(View.GONE);
            if (recyclerPipelineLogs != null) recyclerPipelineLogs.setVisibility(View.VISIBLE);
        }
    }
}

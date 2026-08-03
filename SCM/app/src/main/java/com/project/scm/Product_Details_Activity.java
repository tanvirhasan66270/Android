package com.project.scm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.project.scm.api.ApiClient;
import com.project.scm.model.response.ProductResponseDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product_Details_Activity extends AppCompatActivity {

    private ProductResponseDTO product;
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        
        View mainView = findViewById(R.id.main);
        View header = findViewById(R.id.header);
        
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            header.setPadding(header.getPaddingLeft(), systemBars.top, header.getPaddingRight(), header.getPaddingBottom());
            return insets;
        });

        String productJson = getIntent().getStringExtra("product_data");
        if (productJson != null) {
            product = gson.fromJson(productJson, ProductResponseDTO.class);
            bindProductData();
        } else {
            Toast.makeText(this, "Product data missing", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateHeaderTimestamp();
    }

    private void bindProductData() {
        // Basic Info (Hero Section)
        setDetailItem(R.id.itemCode, R.drawable.ic_copy, "Product Code", product.getProductCode());
        setDetailItem(R.id.itemName, R.drawable.ic_package, "Name", product.getName());
        setDetailItem(R.id.itemCategory, R.drawable.ic_ledger, "Category", product.getCategoryName() + " (ID: " + product.getCategoryId() + ")");
        setDetailItem(R.id.itemUnit, R.drawable.ic_cart, "Unit", product.getUnit());

        // Status Badge
        TextView tvStatusText = findViewById(R.id.tvStatusText);
        View viewStatusDot = findViewById(R.id.viewStatusDot);
        if (product.isActive()) {
            tvStatusText.setText("Active Product");
            viewStatusDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF549934));
        } else {
            tvStatusText.setText("Inactive Product");
            viewStatusDot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFEA4335));
        }

        // Product Information Table
        setInfoRow(R.id.rowQuantity, R.drawable.ic_shopping_bag, "Quantity", String.valueOf(product.getQuantity()));
        setInfoRow(R.id.rowWeight, R.drawable.ic_gear, "Weight", product.getWeight() + " kg");
        setInfoRow(R.id.rowUnitCost, R.drawable.ic_wallet, "Unit Cost", "৳" + String.format(Locale.getDefault(), "%,.2f", product.getUnitCost()));
        setInfoRow(R.id.rowSellingPrice, R.drawable.ic_due, "Selling Price", "৳" + String.format(Locale.getDefault(), "%,.2f", product.getSellingPrice()));
        setInfoRow(R.id.rowReorderPoint, R.drawable.ic_sync, "Reorder Point", String.valueOf(product.getReorderPoint()));
        setInfoRow(R.id.rowAvailability, R.drawable.ic_check_circle, "Availability", product.getAvailability());
        setInfoRow(R.id.rowExpiry, R.drawable.ic_bell, "Has Expiry Date", product.getHasExpiryDate());
        setInfoRow(R.id.rowActive, R.drawable.ic_logout, "Active Status", product.isActive() ? "Active" : "Inactive");

        // Product Image
        ImageView ivProductImage = findViewById(R.id.ivProductImage);
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            String imageUrl = ApiClient.IMAGE_URL + "product/" + product.getImage();
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.baground)
                    .error(R.drawable.baground)
                    .into(ivProductImage);
        }
    }

    private void setDetailItem(int layoutId, int iconRes, String label, String value) {
        View view = findViewById(layoutId);
        ((ImageView) view.findViewById(R.id.ivItemIcon)).setImageResource(iconRes);
        ((TextView) view.findViewById(R.id.tvItemLabel)).setText(label);
        ((TextView) view.findViewById(R.id.tvItemValue)).setText(value != null ? value : "N/A");
    }

    private void setInfoRow(int layoutId, int iconRes, String label, String value) {
        View view = findViewById(layoutId);
        ((ImageView) view.findViewById(R.id.ivRowIcon)).setImageResource(iconRes);
        ((TextView) view.findViewById(R.id.tvRowLabel)).setText(label);
        ((TextView) view.findViewById(R.id.tvRowValue)).setText(value != null ? value : "N/A");
    }

    private void updateHeaderTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date now = new Date();
        
        ((TextView) findViewById(R.id.tvHeaderDate)).setText(dateFormat.format(now));
        ((TextView) findViewById(R.id.tvHeaderTime)).setText(timeFormat.format(now));
        ((TextView) findViewById(R.id.tvFooterTimestamp)).setText(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(now));
    }
}
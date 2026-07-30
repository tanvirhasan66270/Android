package com.project.scm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class tracking_Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracking_dashboard)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.btn_nav_home).setOnClickListener {
            val intent = Intent(this, Dashboard_Activity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<View>(R.id.btn_nav_orders).setOnClickListener {
            val intent = Intent(this, OrderDashboardActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_nav_billing).setOnClickListener {
            val intent = Intent(this, BillingPage::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.btn_nav_shipments).setOnClickListener {
            // Already on Shipments (Tracking)
        }

    }
}
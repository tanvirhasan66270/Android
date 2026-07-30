package com.project.scm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class BillingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_billing_page)

        val mainView = findViewById<View>(R.id.main)
        val header = findViewById<View>(R.id.header)
        val bottomNav = findViewById<View>(R.id.bottomNav)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            header.updatePadding(top = systemBars.top)
            bottomNav.updatePadding(bottom = systemBars.bottom)
            insets
        }

        // Navigation
        findViewById<View>(R.id.btn_nav_home).setOnClickListener {
            startActivity(Intent(this, Dashboard_Activity::class.java))
            finish()
        }

        findViewById<View>(R.id.btn_nav_orders).setOnClickListener {
            startActivity(Intent(this, OrderDashboardActivity::class.java))
            finish()
        }

        findViewById<View>(R.id.btn_nav_shipments).setOnClickListener {
            startActivity(Intent(this, tracking_Dashboard::class.java))
            finish()
        }
        
        findViewById<View>(R.id.btn_nav_billing).setOnClickListener {
            // Already on Billing
        }
        
        // Setup Menu or other button listeners if needed
        findViewById<View>(R.id.btnMenu).setOnClickListener {
            // Handle drawer or menu
        }
    }
}
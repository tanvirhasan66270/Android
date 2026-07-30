package com.project.scm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class OrderDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_dashboard)

        val mainView = findViewById<View>(R.id.main)
        val header = findViewById<View>(R.id.header)
        val footer = findViewById<View>(R.id.footerButtons)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            header.updatePadding(top = systemBars.top)
            footer.updatePadding(bottom = systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.btnCancel).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnDispatch).setOnClickListener {
            // Logic for dispatching order
            finish()
        }

        // Navigation
        findViewById<View>(R.id.btn_nav_home).setOnClickListener {
            startActivity(Intent(this, Dashboard_Activity::class.java))
            finish()
        }

        findViewById<View>(R.id.btn_nav_shipments).setOnClickListener {
            startActivity(Intent(this, tracking_Dashboard::class.java))
            finish()
        }

        findViewById<View>(R.id.btn_nav_billing).setOnClickListener {
            startActivity(Intent(this, BillingPage::class.java))
            finish()
        }
    }
}
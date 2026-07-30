package com.project.scm

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class SupportDesk : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_support_desk)

        val mainView = findViewById<View>(R.id.main)
        val header = findViewById<View>(R.id.header)
        val bottomNav = findViewById<View>(R.id.bottomNav)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            header.updatePadding(top = systemBars.top)
            bottomNav.updatePadding(bottom = systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<View>(R.id.btn_nav_home).setOnClickListener {
            startActivity(Intent(this, Dashboard_Activity::class.java))
            finish()
        }

        findViewById<View>(R.id.btnCancel).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnSubmit).setOnClickListener {
            // Dummy submit logic
            finish()
        }

        val etIssue = findViewById<EditText>(R.id.etIssue)
        val tvCounter = findViewById<TextView>(R.id.tvCounter)

        etIssue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCounter.text = "${s?.length ?: 0}/1000"
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
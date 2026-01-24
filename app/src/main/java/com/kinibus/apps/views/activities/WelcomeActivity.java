package com.kinibus.apps.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.kinibus.apps.R;

/**
 * WelcomeActivity - Halaman welcome/onboarding sebelum login
 * Menampilkan hero section dengan branding dan call-to-action buttons
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    // UI Components
    private MaterialButton loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        Log.d(TAG, "WelcomeActivity created - showing onboarding screen");
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        loginButton = findViewById(R.id.btn_login);
        registerButton = findViewById(R.id.btn_register);
    }

    /**
     * Setup click listeners for buttons
     */
    private void setupClickListeners() {
        // Login button - navigate to LoginActivity
        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked - navigating to LoginActivity");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button - navigate to RegisterActivity
        registerButton.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked - navigating to RegisterActivity");
            try {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("FROM_WELCOME", true); // Add flag to indicate coming from welcome screen
                startActivity(intent);
                Log.d(TAG, "Successfully started RegisterActivity");
            } catch (Exception e) {
                Log.e(TAG, "Failed to start RegisterActivity: " + e.getMessage());
                e.printStackTrace();
                // Fallback to LoginActivity if RegisterActivity fails
                Intent fallbackIntent = new Intent(this, LoginActivity.class);
                fallbackIntent.putExtra("REGISTER_MODE", true);
                startActivity(fallbackIntent);
            }
        });

        // Skip button - navigate directly to Dashboard (guest mode)
        findViewById(R.id.tv_skip).setOnClickListener(v -> {
            Log.d(TAG, "Skip button clicked - navigating to Dashboard (guest mode)");
            navigateToDashboard();
        });
    }

    /**
     * Navigate to Dashboard for guest users
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("GUEST_MODE", true); // Flag untuk guest mode
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "WelcomeActivity destroyed");
    }
}

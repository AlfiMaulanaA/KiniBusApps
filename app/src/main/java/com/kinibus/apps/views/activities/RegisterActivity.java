package com.kinibus.apps.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.kinibus.apps.R;
import com.kinibus.apps.viewmodels.AuthViewModel;

/**
 * RegisterActivity - Halaman pendaftaran user baru
 * Mengumpulkan data: nama, email, password, konfirmasi password
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    // UI Components
    private ImageButton backButton;
    private TextInputEditText fullNameInput, emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton registerButton;

    // ViewModel
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize components
        initializeViews();
        initializeViewModel();
        setupClickListeners();

        Log.d(TAG, "RegisterActivity created");
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        fullNameInput = findViewById(R.id.full_name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        registerButton = findViewById(R.id.register_button);
    }

    /**
     * Initialize ViewModel
     */
    private void initializeViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe loading state
        authViewModel.getIsLoading().observe(this, isLoading -> {
            registerButton.setEnabled(!isLoading);
            registerButton.setText(isLoading ? "Mendaftarkan..." : "Daftar Sekarang");
        });

        // Observe authentication state
        authViewModel.getIsAuthenticated().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                Log.d(TAG, "Registration successful, navigating to main screen");
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                navigateToMainScreen();
            }
        });

        // Observe error messages
        authViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });
    }

    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        // Back button
        backButton.setOnClickListener(v -> {
            finish(); // Go back to previous activity
        });

        // Register button
        registerButton.setOnClickListener(v -> performRegistration());

        // Login link
        findViewById(R.id.login_link).setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close register activity
        });

        // Password visibility toggles
        setupPasswordToggles();
    }

    /**
     * Setup password visibility toggle buttons
     */
    private void setupPasswordToggles() {
        ImageButton passwordToggle = findViewById(R.id.password_toggle);
        ImageButton confirmPasswordToggle = findViewById(R.id.confirm_password_toggle);

        // Password toggle
        passwordToggle.setOnClickListener(v -> {
            togglePasswordVisibility(passwordInput, passwordToggle);
        });

        // Confirm password toggle
        confirmPasswordToggle.setOnClickListener(v -> {
            togglePasswordVisibility(confirmPasswordInput, confirmPasswordToggle);
        });
    }

    /**
     * Toggle password field visibility
     */
    private void togglePasswordVisibility(TextInputEditText passwordField, ImageButton toggleButton) {
        if (passwordField.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Show password
            passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.drawable.ic_launcher_foreground); // Visibility icon
        } else {
            // Hide password
            passwordField.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.drawable.ic_launcher_foreground); // Visibility off icon
        }
        // Move cursor to end
        passwordField.setSelection(passwordField.getText().length());
    }

    /**
     * Perform registration validation and authentication
     */
    private void performRegistration() {
        // Get input values
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(fullName, email, password, confirmPassword)) {
            return;
        }

        // For demo purposes, show success message and navigate to LoginActivity
        // In real app, this would register the user
        Log.d(TAG, "Registration form submitted for: " + email);

        Toast.makeText(this, "Form registrasi valid! Silakan login dengan akun yang dibuat.", Toast.LENGTH_LONG).show();

        // Navigate back to LoginActivity instead of directly registering
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("FROM_REGISTER", true);
        startActivity(intent);
        finish();
    }

    /**
     * Validate registration inputs
     */
    private boolean validateInputs(String fullName, String email, String password, String confirmPassword) {
        boolean isValid = true;

        // Full name validation
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (fullName.length() < 2) {
            Toast.makeText(this, "Nama lengkap minimal 2 karakter", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Email validation
        if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Password validation
        if (password.isEmpty()) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Confirm password validation
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Konfirmasi password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password dan konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Registration error: " + message);
    }

    /**
     * Navigate to dashboard after successful registration
     */
    private void navigateToMainScreen() {
        Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();

        // Navigate to Dashboard
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RegisterActivity destroyed");
    }
}
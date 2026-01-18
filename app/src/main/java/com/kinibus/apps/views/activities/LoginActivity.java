package com.kinibus.apps.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kinibus.apps.MainActivity;
import com.kinibus.apps.R;
import com.kinibus.apps.helpers.GoogleSignInHelper;
import com.kinibus.apps.repositories.AuthRepository;
import com.kinibus.apps.viewmodels.AuthViewModel;

/**
 * Login Activity - Entry point untuk authentication
 * Menggunakan MVVM pattern dengan AuthViewModel
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    // UI Components
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton, googleSignInButton, registerButton;
    private LinearLayout loginForm;
    private ImageView backButton;

    // ViewModel & Repository
    private AuthViewModel authViewModel;
    private AuthRepository authRepository;
    private GoogleSignInHelper googleSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize components
        initializeViews();
        initializeViewModel();
        initializeGoogleSignIn();
        setupClickListeners();

        // Check if user already logged in
        checkCurrentUser();

        // Check for register mode from WelcomeActivity
        checkRegisterMode();

        // Check if coming from register success
        checkFromRegister();
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        // Login button (primary action)
        loginButton = findViewById(R.id.login_button);

        // Input fields
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);

        // Social login buttons
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        // appleSignInButton = findViewById(R.id.apple_sign_in_button); // For future Apple Sign-In

        // Password toggle
        // passwordToggle = findViewById(R.id.password_toggle); // For future password visibility toggle
    }

    /**
     * Initialize ViewModel
     */
    private void initializeViewModel() {
        authRepository = AuthRepository.getInstance();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe loading state
        authViewModel.getIsLoading().observe(this, isLoading -> {
            loginButton.setEnabled(!isLoading);
            googleSignInButton.setEnabled(!isLoading);

            // Update button text
            loginButton.setText(isLoading ? "Sedang Masuk..." : "Masuk");
        });

        // Observe authentication state
        authViewModel.getIsAuthenticated().observe(this, isAuthenticated -> {
            if (isAuthenticated) {
                Log.d(TAG, "Authentication successful, navigating to main screen");
                Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show();
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
     * Initialize Google Sign-In
     */
    private void initializeGoogleSignIn() {
        googleSignInHelper = GoogleSignInHelper.getInstance();
        googleSignInHelper.initialize(this, new GoogleSignInHelper.GoogleSignInCallback() {
            @Override
            public void onSuccess(String idToken) {
                Log.d(TAG, "Google Sign-In successful, authenticating with Firebase");
                // Authenticate dengan Firebase menggunakan Google ID token
                authViewModel.loginWithGoogle(idToken);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Google Sign-In failed: " + error);
                showError("Google Sign-In gagal: " + error);
            }
        });
    }

    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        // Primary login button
        loginButton.setOnClickListener(v -> performLogin());

        // Google Sign-In button
        if (googleSignInButton != null) {
            googleSignInButton.setOnClickListener(v -> {
                if (googleSignInHelper != null) {
                    Log.d(TAG, "Starting Google Sign-In flow");
                    googleSignInHelper.signIn();
                } else {
                    showError("Google Sign-In belum diinisialisasi");
                }
            });
        }

        // Register link - navigate to RegisterActivity
        findViewById(R.id.register_link).setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Forgot password
        findViewById(R.id.forgot_password_text).setOnClickListener(v -> {
            // Navigate to ForgotPasswordActivity
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Show login form with animation
     */
    private void showLoginForm() {
        if (loginForm != null) {
            loginForm.setVisibility(View.VISIBLE);
            loginForm.animate()
                .alpha(1.0f)
                .setDuration(300)
                .start();
        }
    }

    /**
     * Hide login form with animation
     */
    private void hideLoginForm() {
        if (loginForm != null) {
            loginForm.animate()
                .alpha(0.0f)
                .setDuration(300)
                .withEndAction(() -> loginForm.setVisibility(View.GONE))
                .start();
        }
    }

    /**
     * Show register form (same as login form but with register mode)
     */
    private void showRegisterForm() {
        // For now, just show login form - can be enhanced later
        showLoginForm();
        Toast.makeText(this, "Mode pendaftaran - isi email dan password lalu klik register", Toast.LENGTH_SHORT).show();
    }

    /**
     * Check if user is already logged in
     */
    private void checkCurrentUser() {
        if (authRepository.isAuthenticated.getValue() != null &&
            authRepository.isAuthenticated.getValue()) {
            Log.d(TAG, "User already logged in, navigating to main screen");
            navigateToMainScreen();
        }
    }

    /**
     * Check if user came from register button in WelcomeActivity
     */
    private void checkRegisterMode() {
        boolean isRegisterMode = getIntent().getBooleanExtra("REGISTER_MODE", false);
        if (isRegisterMode) {
            Log.d(TAG, "User in register mode from WelcomeActivity");
            Toast.makeText(this, "Mode pendaftaran - isi email dan password lalu klik register", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check if user came from successful registration
     */
    private void checkFromRegister() {
        boolean fromRegister = getIntent().getBooleanExtra("FROM_REGISTER", false);
        if (fromRegister) {
            Log.d(TAG, "User came from successful registration");
            Toast.makeText(this, "Registrasi berhasil! Silakan login dengan akun yang telah dibuat.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Perform login validation and authentication
     */
    private void performLogin() {
        // Get input values
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Perform login
        authViewModel.login(email, password);
    }

    /**
     * Validate email and password inputs
     */
    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

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

        return isValid;
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Authentication error: " + message);
    }

    /**
     * Navigate to main screen with bottom navigation after successful authentication
     */
    private void navigateToMainScreen() {
        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity with bottom navigation
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check authentication state when activity becomes visible
        checkCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up observers if needed
    }
}

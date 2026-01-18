package com.kinibus.apps.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kinibus.apps.helpers.FirebaseAuthHelper;
import com.kinibus.apps.models.User;

/**
 * Repository untuk mengelola operasi authentication
 * Mengintegrasikan Firebase Auth dengan Firestore untuk data user
 */
public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private static AuthRepository instance;
    private FirebaseAuthHelper authHelper;
    private FirebaseFirestore db;

    // LiveData untuk status authentication
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>();

    // Private constructor untuk Singleton
    private AuthRepository() {
        authHelper = FirebaseAuthHelper.getInstance();
        db = FirebaseFirestore.getInstance();
        checkCurrentUser();
    }

    // Method untuk mendapatkan instance Singleton
    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Register user baru
     */
    public void registerUser(String email, String password, String nama, String nomorTelepon) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        authHelper.registerUser(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Registration berhasil, simpan data user ke Firestore
                    String userId = authHelper.getCurrentUserId();
                    if (userId != null) {
                        saveUserToFirestore(userId, email, nama, nomorTelepon);
                    } else {
                        isLoading.setValue(false);
                        errorMessage.setValue("Failed to get user ID after registration");
                    }
                } else {
                    isLoading.setValue(false);
                    String error = task.getException() != null ?
                        task.getException().getMessage() : "Registration failed";
                    errorMessage.setValue(error);
                    Log.e(TAG, "Registration failed: " + error);
                }
            });
    }

    /**
     * Simpan data user ke Firestore setelah registration
     */
    private void saveUserToFirestore(String userId, String email, String nama, String nomorTelepon) {
        User user = new User(email, nama, nomorTelepon);

        db.collection("users").document(userId).set(user)
            .addOnSuccessListener(aVoid -> {
                isLoading.setValue(false);
                isAuthenticated.setValue(true);
                Log.d(TAG, "User data saved to Firestore successfully");
            })
            .addOnFailureListener(e -> {
                isLoading.setValue(false);
                errorMessage.setValue("Failed to save user data: " + e.getMessage());
                Log.e(TAG, "Failed to save user data: " + e.getMessage());
                // Note: User tetap terdaftar di Auth, tapi data profile tidak tersimpan
            });
    }

    /**
     * Login user
     */
    public void loginUser(String email, String password) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        authHelper.loginUser(email, password)
            .addOnCompleteListener(task -> {
                isLoading.setValue(false);
                if (task.isSuccessful()) {
                    isAuthenticated.setValue(true);
                    Log.d(TAG, "Login successful for: " + email);
                } else {
                    String error = task.getException() != null ?
                        task.getException().getMessage() : "Login failed";
                    errorMessage.setValue(error);
                    Log.e(TAG, "Login failed: " + error);
                }
            });
    }

    /**
     * Login dengan Google
     */
    public void loginWithGoogle(String idToken) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        authHelper.loginWithGoogle(idToken)
            .addOnCompleteListener(task -> {
                isLoading.setValue(false);
                if (task.isSuccessful()) {
                    // Check apakah user sudah ada di Firestore
                    String userId = authHelper.getCurrentUserId();
                    if (userId != null) {
                        checkAndCreateGoogleUser(userId);
                    }
                } else {
                    String error = task.getException() != null ?
                        task.getException().getMessage() : "Google sign-in failed";
                    errorMessage.setValue(error);
                    Log.e(TAG, "Google sign-in failed: " + error);
                }
            });
    }

    /**
     * Check dan create user data untuk Google sign-in
     */
    private void checkAndCreateGoogleUser(String userId) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    // User belum ada di Firestore, buat data baru
                    String email = authHelper.getCurrentUserEmail();
                    String displayName = authHelper.getCurrentUser() != null ?
                        authHelper.getCurrentUser().getDisplayName() : "Google User";

                    User user = new User(email, displayName, "");
                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener(aVoid -> {
                            isAuthenticated.setValue(true);
                            Log.d(TAG, "Google user created in Firestore");
                        })
                        .addOnFailureListener(e -> {
                            errorMessage.setValue("Failed to create Google user profile");
                            Log.e(TAG, "Failed to create Google user: " + e.getMessage());
                        });
                } else {
                    // User sudah ada
                    isAuthenticated.setValue(true);
                    Log.d(TAG, "Google user already exists in Firestore");
                }
            })
            .addOnFailureListener(e -> {
                errorMessage.setValue("Failed to check user data");
                Log.e(TAG, "Failed to check user: " + e.getMessage());
            });
    }

    /**
     * Password reset
     */
    public void resetPassword(String email) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        authHelper.resetPassword(email)
            .addOnCompleteListener(task -> {
                isLoading.setValue(false);
                if (task.isSuccessful()) {
                    errorMessage.setValue("Password reset email sent to " + email);
                    Log.d(TAG, "Password reset email sent to: " + email);
                } else {
                    String error = task.getException() != null ?
                        task.getException().getMessage() : "Failed to send reset email";
                    errorMessage.setValue(error);
                    Log.e(TAG, "Password reset failed: " + error);
                }
            });
    }

    /**
     * Logout
     */
    public void logout() {
        authHelper.logout();
        isAuthenticated.setValue(false);
        Log.d(TAG, "User logged out");
    }

    /**
     * Check current user status
     */
    private void checkCurrentUser() {
        boolean loggedIn = authHelper.isUserLoggedIn();
        isAuthenticated.setValue(loggedIn);
        Log.d(TAG, "Current user status: " + (loggedIn ? "Logged in" : "Not logged in"));
    }

    /**
     * Get current user data dari Firestore
     */
    public void getCurrentUserData(MutableLiveData<User> userLiveData) {
        String userId = authHelper.getCurrentUserId();
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        userLiveData.setValue(user);
                    } else {
                        errorMessage.setValue("User profile not found");
                    }
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to load user data: " + e.getMessage());
                    Log.e(TAG, "Failed to load user data: " + e.getMessage());
                });
        } else {
            errorMessage.setValue("No authenticated user");
        }
    }

    /**
     * Update user profile
     */
    public void updateUserProfile(User updatedUser) {
        String userId = authHelper.getCurrentUserId();
        if (userId != null) {
            isLoading.setValue(true);
            db.collection("users").document(userId).set(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    Log.d(TAG, "User profile updated successfully");
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to update profile: " + e.getMessage());
                    Log.e(TAG, "Failed to update profile: " + e.getMessage());
                });
        } else {
            errorMessage.setValue("No authenticated user to update");
        }
    }

    /**
     * Get current user email
     */
    public String getCurrentUserEmail() {
        return authHelper.getCurrentUserEmail();
    }

    /**
     * Get current user ID
     */
    public String getCurrentUserId() {
        return authHelper.getCurrentUserId();
    }
}

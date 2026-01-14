package com.kinibus.apps.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kinibus.apps.models.User;
import com.kinibus.apps.repositories.AuthRepository;

/**
 * ViewModel untuk Authentication
 * Mengelola state dan business logic untuk login/register
 */
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    // LiveData untuk UI state
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public AuthViewModel() {
        authRepository = AuthRepository.getInstance();

        // Observe repository state changes
        authRepository.isLoading.observeForever(loading -> {
            isLoading.setValue(loading);
        });

        authRepository.isAuthenticated.observeForever(auth -> {
            isAuthenticated.setValue(auth);
        });

        authRepository.errorMessage.observeForever(error -> {
            if (error != null && !error.isEmpty()) {
                errorMessage.setValue(error);
            }
        });
    }

    /**
     * Login dengan email dan password
     */
    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("Email tidak boleh kosong");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Password tidak boleh kosong");
            return;
        }

        authRepository.loginUser(email, password);
    }

    /**
     * Register user baru
     */
    public void register(String email, String password, String nama, String nomorTelepon) {
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("Email tidak boleh kosong");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            errorMessage.setValue("Password tidak boleh kosong");
            return;
        }

        if (nama == null || nama.trim().isEmpty()) {
            errorMessage.setValue("Nama tidak boleh kosong");
            return;
        }

        if (nomorTelepon == null || nomorTelepon.trim().isEmpty()) {
            errorMessage.setValue("Nomor telepon tidak boleh kosong");
            return;
        }

        authRepository.registerUser(email, password, nama, nomorTelepon);
    }

    /**
     * Login dengan Google
     */
    public void loginWithGoogle(String idToken) {
        if (idToken == null || idToken.trim().isEmpty()) {
            errorMessage.setValue("Google sign-in token tidak valid");
            return;
        }

        authRepository.loginWithGoogle(idToken);
    }

    /**
     * Password reset
     */
    public void resetPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("Email tidak boleh kosong");
            return;
        }

        authRepository.resetPassword(email);
    }

    /**
     * Logout
     */
    public void logout() {
        authRepository.logout();
    }

    /**
     * Update user profile
     */
    public void updateUserProfile(User updatedUser) {
        authRepository.updateUserProfile(updatedUser);
    }

    // Getters untuk LiveData
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsAuthenticated() {
        return isAuthenticated;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Clear error message
     */
    public void clearError() {
        errorMessage.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up if needed
    }
}
package com.kinibus.apps.helpers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Helper class untuk operasi Firebase Authentication
 * Menggunakan Singleton pattern untuk konsistensi
 */
public class FirebaseAuthHelper {
    private static final String TAG = "FirebaseAuthHelper";
    private static FirebaseAuthHelper instance;
    private FirebaseAuth mAuth;

    // Private constructor untuk Singleton
    private FirebaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Method untuk mendapatkan instance Singleton
    public static synchronized FirebaseAuthHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthHelper();
        }
        return instance;
    }

    // Register dengan email dan password
    public Task<AuthResult> registerUser(String email, String password) {
        Log.d(TAG, "Attempting to register user: " + email);
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    // Login dengan email dan password
    public Task<AuthResult> loginUser(String email, String password) {
        Log.d(TAG, "Attempting to login user: " + email);
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    // Login dengan Google credential
    public Task<AuthResult> loginWithGoogle(String idToken) {
        Log.d(TAG, "Attempting Google sign-in");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return mAuth.signInWithCredential(credential);
    }

    // Password reset
    public Task<Void> resetPassword(String email) {
        Log.d(TAG, "Sending password reset to: " + email);
        return mAuth.sendPasswordResetEmail(email);
    }

    // Logout
    public void logout() {
        Log.d(TAG, "User logged out");
        mAuth.signOut();
    }

    // Get current user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Check if user is logged in
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    // Get user ID
    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    // Get user email
    public String getCurrentUserEmail() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    // Update user password
    public Task<Void> updatePassword(String newPassword) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Updating password for user: " + user.getEmail());
            return user.updatePassword(newPassword);
        } else {
            Log.w(TAG, "No current user to update password");
            return null;
        }
    }

    // Update user email
    public Task<Void> updateEmail(String newEmail) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Updating email for user: " + user.getEmail() + " to: " + newEmail);
            return user.updateEmail(newEmail);
        } else {
            Log.w(TAG, "No current user to update email");
            return null;
        }
    }

    // Delete user account
    public Task<Void> deleteAccount() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Deleting account for user: " + user.getEmail());
            return user.delete();
        } else {
            Log.w(TAG, "No current user to delete");
            return null;
        }
    }

    // Reload user data
    public Task<Void> reloadUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            Log.d(TAG, "Reloading user data for: " + user.getEmail());
            return user.reload();
        } else {
            Log.w(TAG, "No current user to reload");
            return null;
        }
    }
}
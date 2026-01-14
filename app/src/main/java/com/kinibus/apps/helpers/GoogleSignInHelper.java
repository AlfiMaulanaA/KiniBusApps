package com.kinibus.apps.helpers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Helper class untuk Google Sign-In integration
 * Mengelola Google Sign-In client dan authentication flow
 */
public class GoogleSignInHelper {
    private static final String TAG = "GoogleSignInHelper";
    private static GoogleSignInHelper instance;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private Activity currentActivity;
    private ActivityResultLauncher<Intent> signInLauncher;

    // Callback interface
    public interface GoogleSignInCallback {
        void onSuccess(String idToken);
        void onError(String error);
    }

    private GoogleSignInCallback callback;

    // Private constructor untuk Singleton
    private GoogleSignInHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // Method untuk mendapatkan instance Singleton
    public static synchronized GoogleSignInHelper getInstance() {
        if (instance == null) {
            instance = new GoogleSignInHelper();
        }
        return instance;
    }

    /**
     * Initialize Google Sign-In client
     * Harus dipanggil di onCreate activity
     */
    public void initialize(Activity activity, GoogleSignInCallback callback) {
        this.currentActivity = activity;
        this.callback = callback;

        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1060565677214-bloc3mlu2qs1v02lmk4tqru16o87duuc.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);

        // Initialize activity result launcher
        setupActivityResultLauncher();
    }

    /**
     * Setup activity result launcher untuk handle sign-in result
     */
    private void setupActivityResultLauncher() {
        if (currentActivity instanceof androidx.activity.ComponentActivity) {
            androidx.activity.ComponentActivity componentActivity =
                (androidx.activity.ComponentActivity) currentActivity;

            signInLauncher = componentActivity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        handleSignInResult(data);
                    } else {
                        Log.w(TAG, "Google Sign-In cancelled by user");
                        if (callback != null) {
                            callback.onError("Sign-in cancelled");
                        }
                    }
                }
            );
        }
    }

    /**
     * Start Google Sign-In flow
     */
    public void signIn() {
        if (googleSignInClient == null) {
            Log.e(TAG, "GoogleSignInClient not initialized. Call initialize() first.");
            if (callback != null) {
                callback.onError("Google Sign-In not properly configured");
            }
            return;
        }

        Intent signInIntent = googleSignInClient.getSignInIntent();
        if (signInLauncher != null) {
            signInLauncher.launch(signInIntent);
        } else {
            Log.e(TAG, "Activity result launcher not initialized");
            if (callback != null) {
                callback.onError("Activity launcher not ready");
            }
        }
    }

    /**
     * Handle sign-in result from Google
     */
    private void handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null) {
                String idToken = account.getIdToken();
                Log.d(TAG, "Google Sign-In successful, ID token obtained");

                if (callback != null && idToken != null) {
                    callback.onSuccess(idToken);
                } else {
                    Log.e(TAG, "ID token is null");
                    if (callback != null) {
                        callback.onError("Failed to get ID token");
                    }
                }
            } else {
                Log.e(TAG, "GoogleSignInAccount is null");
                if (callback != null) {
                    callback.onError("Sign-in account is null");
                }
            }

        } catch (ApiException e) {
            Log.e(TAG, "Google sign-in failed", e);
            String errorMessage = getErrorMessage(e.getStatusCode());
            if (callback != null) {
                callback.onError(errorMessage);
            }
        }
    }

    /**
     * Convert Google Sign-In error codes to user-friendly messages
     */
    private String getErrorMessage(int statusCode) {
        switch (statusCode) {
            case 12500: // SIGN_IN_CANCELLED
                return "Sign-in was cancelled";
            case 12501: // SIGN_IN_CURRENTLY_IN_PROGRESS
                return "Sign-in already in progress";
            case 12502: // SIGN_IN_FAILED
                return "Sign-in failed";
            case 12503: // SIGN_IN_REQUIRED
                return "Sign-in required";
            default:
                return "Google Sign-In failed (Code: " + statusCode + ")";
        }
    }

    /**
     * Sign out from Google account
     */
    public void signOut() {
        if (googleSignInClient != null) {
            googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Google sign-out successful");
                    } else {
                        Log.w(TAG, "Google sign-out failed", task.getException());
                    }
                });
        }
    }

    /**
     * Revoke Google access (complete sign out)
     */
    public void revokeAccess() {
        if (googleSignInClient != null) {
            googleSignInClient.revokeAccess()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Google access revoked");
                    } else {
                        Log.w(TAG, "Failed to revoke Google access", task.getException());
                    }
                });
        }
    }

    /**
     * Check if user is signed in with Google
     */
    public boolean isSignedInWithGoogle() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(currentActivity);
        return account != null;
    }

    /**
     * Get last signed-in Google account
     */
    public GoogleSignInAccount getLastSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(currentActivity);
    }

    /**
     * Silent sign-in (if user previously signed in)
     */
    public void silentSignIn(GoogleSignInCallback callback) {
        if (googleSignInClient == null) {
            callback.onError("GoogleSignInClient not initialized");
            return;
        }

        googleSignInClient.silentSignIn()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    GoogleSignInAccount account = task.getResult();
                    String idToken = account.getIdToken();
                    if (idToken != null) {
                        callback.onSuccess(idToken);
                    } else {
                        callback.onError("Failed to get ID token from silent sign-in");
                    }
                } else {
                    Log.d(TAG, "Silent sign-in failed, user needs to sign in manually");
                    callback.onError("Silent sign-in failed");
                }
            });
    }
}

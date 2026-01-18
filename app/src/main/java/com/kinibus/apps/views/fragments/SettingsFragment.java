package com.kinibus.apps.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.kinibus.apps.R;
import com.kinibus.apps.repositories.AuthRepository;
import com.kinibus.apps.views.activities.LoginActivity;
import com.kinibus.apps.views.activities.ProfileActivity;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private AuthRepository authRepository;
    private MaterialCardView cardProfile, cardChangePassword, cardNotifications,
                           cardLanguage, cardHelp, cardContact, cardTerms;
    private MaterialButton btnLogout;
    private TextView tvAppVersion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize components
        authRepository = AuthRepository.getInstance();
        initializeViews(view);
        setupClickListeners();
        setupAppVersion();

        return view;
    }

    private void initializeViews(View view) {
        cardProfile = view.findViewById(R.id.card_profile);
        cardChangePassword = view.findViewById(R.id.card_change_password);
        cardNotifications = view.findViewById(R.id.card_notifications);
        cardLanguage = view.findViewById(R.id.card_language);
        cardHelp = view.findViewById(R.id.card_help);
        cardContact = view.findViewById(R.id.card_contact);
        cardTerms = view.findViewById(R.id.card_terms);
        btnLogout = view.findViewById(R.id.btn_logout);
        tvAppVersion = view.findViewById(R.id.tv_app_version);
    }

    private void setupClickListeners() {
        // Profile settings
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Change password - placeholder
        cardChangePassword.setOnClickListener(v -> {
            showFeatureNotAvailable("Fitur ubah kata sandi");
        });

        // Notifications - placeholder
        cardNotifications.setOnClickListener(v -> {
            showFeatureNotAvailable("Pengaturan notifikasi");
        });

        // Language - placeholder
        cardLanguage.setOnClickListener(v -> {
            showFeatureNotAvailable("Pengaturan bahasa");
        });

        // Help center - placeholder
        cardHelp.setOnClickListener(v -> {
            showFeatureNotAvailable("Pusat bantuan");
        });

        // Contact us - placeholder
        cardContact.setOnClickListener(v -> {
            showFeatureNotAvailable("Hubungi kami");
        });

        // Terms & conditions - placeholder
        cardTerms.setOnClickListener(v -> {
            showFeatureNotAvailable("Syarat dan ketentuan");
        });

        // Logout
        btnLogout.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void setupAppVersion() {
        try {
            String versionName = requireActivity().getPackageManager()
                    .getPackageInfo(requireActivity().getPackageName(), 0).versionName;
            tvAppVersion.setText(versionName);
        } catch (Exception e) {
            tvAppVersion.setText("1.0.0");
        }
    }

    private void showFeatureNotAvailable(String featureName) {
        Toast.makeText(getContext(),
            featureName + " akan segera hadir!",
            Toast.LENGTH_SHORT).show();
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Ya, Keluar", (dialog, which) -> performLogout())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void performLogout() {
        // Clear authentication state
        authRepository.logout();

        // Navigate back to login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();

        Toast.makeText(getContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
    }
}

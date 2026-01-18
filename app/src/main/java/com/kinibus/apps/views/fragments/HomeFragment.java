package com.kinibus.apps.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.kinibus.apps.R;
import com.kinibus.apps.views.activities.DashboardActivity;
import com.kinibus.apps.views.activities.ProfileActivity;

public class HomeFragment extends Fragment {

    private CardView cvQuickBook, cvMyBookings, cvProfile, cvSupport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        cvQuickBook = view.findViewById(R.id.cv_quick_book);
        cvMyBookings = view.findViewById(R.id.cv_my_bookings);
        cvProfile = view.findViewById(R.id.cv_profile);
        cvSupport = view.findViewById(R.id.cv_support);

        // Setup click listeners
        setupClickListeners();

        return view;
    }

    private void setupClickListeners() {
        // Quick Book - navigate to DashboardActivity
        cvQuickBook.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
        });

        // My Bookings - navigate to History/Profile
        cvMyBookings.setOnClickListener(v -> {
            // For now, navigate to ProfileActivity
            // Later can create dedicated HistoryActivity
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Profile - navigate to ProfileActivity
        cvProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Support - placeholder for now
        cvSupport.setOnClickListener(v -> {
            // TODO: Implement support/contact activity
            // For now, just show a message
            if (getActivity() != null) {
                android.widget.Toast.makeText(getActivity(),
                    "Fitur dukungan akan segera hadir!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}

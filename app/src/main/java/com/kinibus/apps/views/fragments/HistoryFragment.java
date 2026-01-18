package com.kinibus.apps.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Booking;
import com.kinibus.apps.repositories.AuthRepository;
import com.kinibus.apps.views.adapters.BookingAdapter;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements BookingAdapter.OnBookingClickListener {

    private static final String TAG = "HistoryFragment";

    private RecyclerView rvBookingHistory;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private LinearLayout emptyStateLayout;
    private ListenerRegistration bookingListener;
    private AuthRepository authRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize views
        rvBookingHistory = view.findViewById(R.id.rv_booking_history);
        emptyStateLayout = view.findViewById(R.id.empty_state_layout);

        // Initialize components
        authRepository = AuthRepository.getInstance();
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, this);

        // Setup RecyclerView
        rvBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBookingHistory.setAdapter(bookingAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookingHistory();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bookingListener != null) {
            bookingListener.remove();
        }
    }

    private void loadBookingHistory() {
        String userId = authRepository.getCurrentUserId();
        if (userId == null) {
            Log.w(TAG, "User not authenticated, cannot load booking history");
            showEmptyState();
            return;
        }

        Log.d(TAG, "Loading booking history for user: " + userId);

        if (bookingListener != null) {
            bookingListener.remove();
        }

        // Query bookings for current user, ordered by creation date (newest first)
        Query query = FirebaseFirestore.getInstance()
                .collection("bookings")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50); // Limit to prevent loading too many bookings

        bookingListener = query.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                Log.e(TAG, "Error loading booking history", error);
                showEmptyState();
                return;
            }

            if (snapshot != null) {
                bookingList.clear();
                Log.d(TAG, "Found " + snapshot.size() + " bookings");

                for (var document : snapshot) {
                    Booking booking = document.toObject(Booking.class);
                    if (booking != null) {
                        booking.setId(document.getId());
                        bookingList.add(booking);
                        Log.d(TAG, "Added booking: " + booking.getBusName() + " from " + booking.getDeparture() + " to " + booking.getDestination());
                    }
                }

                bookingAdapter.notifyDataSetChanged();

                if (bookingList.isEmpty()) {
                    showEmptyState();
                } else {
                    showBookingList();
                }
            }
        });
    }

    private void showEmptyState() {
        rvBookingHistory.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    private void showBookingList() {
        rvBookingHistory.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBookingClicked(Booking booking) {
        // TODO: Navigate to booking details activity
        // For now, just log the click
        Log.d(TAG, "Booking clicked: " + booking.getId() + " - " + booking.getBusName());

        // Future implementation: Open booking details screen
        // Intent intent = new Intent(getActivity(), BookingDetailActivity.class);
        // intent.putExtra("BOOKING_ID", booking.getId());
        // startActivity(intent);
    }
}

package com.kinibus.apps.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Bus;
import com.kinibus.apps.repositories.BusRepository;
import com.kinibus.apps.views.adapters.BusAdapter;
import com.kinibus.apps.views.fragments.EditTripBottomSheetFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DashboardActivity extends AppCompatActivity implements 
        EditTripBottomSheetFragment.OnTripDetailsChangedListener,
        BusAdapter.OnBusItemClickListener {

    private BusRepository busRepository;
    private RecyclerView rvBusList;
    private BusAdapter busAdapter;
    private List<Bus> busList;
    private ListenerRegistration busListener;
    private TextView tvRoute, tvTripDetails;
    private ChipGroup chipGroupClass, chipGroupSort;
    private LinearLayout emptyStateLayout;

    private String origin = null; // No default route - user must select
    private String destination = null;
    private Date tripDate = null;
    private String passengers = "1 Orang";
    private String activeClassFilter = "all";
    private String activeSort = "cheapest";
    private boolean isInitialLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        busRepository = new BusRepository();
        initializeViews();

        // Check Firestore connectivity first
        checkFirestoreConnectivity();

        // Don't load buses initially - wait for user to select route
        updateToolbarText();
        showEmptyState();
    }

    private Date getInitialDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2026, Calendar.JANUARY, 13);
        return cal.getTime();
    }

    private void initializeViews() {
        rvBusList = findViewById(R.id.rv_bus_list);
        tvRoute = findViewById(R.id.tv_route);
        tvTripDetails = findViewById(R.id.tv_trip_details);
        chipGroupClass = findViewById(R.id.chip_group_class);
        chipGroupSort = findViewById(R.id.chip_group_sort);
        emptyStateLayout = findViewById(R.id.empty_state_layout);

        rvBusList.setLayoutManager(new LinearLayoutManager(this));
        busList = new ArrayList<>();
        busAdapter = new BusAdapter(busList, this);
        rvBusList.setAdapter(busAdapter);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        findViewById(R.id.btn_edit).setOnClickListener(v -> {
            // Use current values or defaults for the edit dialog
            String currentOrigin = origin != null ? origin : "Gambir";
            String currentDestination = destination != null ? destination : "Pasteur";
            long currentDateTime = tripDate != null ? tripDate.getTime() : getInitialDate().getTime();

            EditTripBottomSheetFragment bottomSheet = EditTripBottomSheetFragment.newInstance(
                    currentOrigin, currentDestination, currentDateTime, passengers);
            bottomSheet.show(getSupportFragmentManager(), "EditTripBottomSheetFragment");
        });

        updateToolbarText();

        chipGroupClass.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_executive) {
                activeClassFilter = "EXECUTIVE";
            } else if (checkedId == R.id.chip_business) {
                activeClassFilter = "BUSINESS";
            } else if (checkedId == R.id.chip_economic) {
                activeClassFilter = "ECONOMIC";
            } else {
                activeClassFilter = "all";
            }
            // Only listen for updates if we have a selected route
            if (origin != null && destination != null && tripDate != null) {
                listenForBusUpdates();
            }
        });

        chipGroupSort.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chip_earliest) {
                activeSort = "earliest";
            } else {
                activeSort = "cheapest";
            }
            sortAndRefresh();
        });
    }

    private void listenForBusUpdates() {
        if (busListener != null) {
            busListener.remove();
        }

        Log.d("DashboardActivity", "Querying buses for: " + origin + " → " + destination + " on " + tripDate);

        Query query = busRepository.getBusesCollection()
                .whereEqualTo("keberangkatan", origin)
                .whereEqualTo("tujuan", destination);

        if (!"all".equals(activeClassFilter)) {
            query = query.whereEqualTo("jenis", activeClassFilter);
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(tripDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date endDate = cal.getTime();

        Log.d("DashboardActivity", "Date range: " + startDate + " to " + endDate);

        query = query.whereGreaterThanOrEqualTo("waktuKeberangkatan", startDate)
                     .whereLessThan("waktuKeberangkatan", endDate)
                     .orderBy("waktuKeberangkatan", Query.Direction.ASCENDING);

        busListener = busRepository.listenForBusUpdates(query, (snapshot, error) -> {
            if (error != null) {
                Log.e("DashboardActivity", "Listen failed.", error);
                String errorMsg = error.getMessage();
                if (errorMsg.contains("requires an index")) {
                    errorMsg = "Database index required. Please create composite index in Firebase Console:\n" +
                              "Fields: keberangkatan, tujuan, jenis, waktuKeberangkatan";
                }
                Toast.makeText(this, "Failed to load buses: " + errorMsg, Toast.LENGTH_LONG).show();
                return;
            }

            if (snapshot != null) {
                busList.clear();
                Log.d("DashboardActivity", "Found " + snapshot.size() + " bus documents");

                // Use a Set to prevent duplicates
                java.util.Set<String> addedBusIds = new java.util.HashSet<>();

                for (DocumentSnapshot document : snapshot) {
                    Bus bus = document.toObject(Bus.class);
                    if (bus != null && !addedBusIds.contains(document.getId())) {
                        bus.setId(document.getId());
                        busList.add(bus);
                        addedBusIds.add(document.getId());
                        Log.d("DashboardActivity", "Added bus: " + bus.getNama() + " (" + bus.getJenis() + ") at " + bus.getWaktuKeberangkatan());
                    }
                }

                Log.d("DashboardActivity", "Total unique buses in list: " + busList.size());
                sortAndRefresh();
            }
        });
    }

    private void sortAndRefresh() {
        if ("cheapest".equals(activeSort)) {
            Collections.sort(busList, Comparator.comparingLong(Bus::getHarga));
        } else {
            Collections.sort(busList, Comparator.comparing(Bus::getWaktuKeberangkatan));
        }

        busAdapter.notifyDataSetChanged();

        if (busList.isEmpty()) {
            rvBusList.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            rvBusList.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTripDetailsChanged(String origin, String destination, Date date, String passengers) {
        this.origin = origin;
        this.destination = destination;
        this.tripDate = date;
        this.passengers = passengers;

        updateToolbarText(passengers);
        listenForBusUpdates();
    }

    @Override
    public void onBusItemClicked(Bus bus) {
        // Navigate to Seat Selection Activity (new flow)
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("bus", bus);
        startActivity(intent);
    }

    private void updateToolbarText() {
        updateToolbarText(this.passengers);
    }

    private void updateToolbarText(String passengers) {
        if (origin != null && destination != null) {
            tvRoute.setText(origin + " → " + destination);
        } else {
            tvRoute.setText(getString(R.string.select_route_prompt));
        }

        if (tripDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM", Locale.getDefault());
            tvTripDetails.setText(sdf.format(tripDate) + " • " + passengers);
        } else {
            tvTripDetails.setText(getString(R.string.select_date_prompt) + " • " + passengers);
        }
    }

    private void showEmptyState() {
        rvBusList.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);

        // Update empty state message
        TextView emptyTitle = emptyStateLayout.findViewById(android.R.id.text1);
        TextView emptyMessage = emptyStateLayout.findViewById(android.R.id.text2);

        if (emptyTitle != null) {
            emptyTitle.setText(getString(R.string.empty_state_title));
        }
        if (emptyMessage != null) {
            emptyMessage.setText(getString(R.string.empty_state_message));
        }
    }

    private void checkFirestoreConnectivity() {
        Log.d("DashboardActivity", "=== FIRESTORE CONNECTIVITY CHECK ===");

        // Test basic connectivity
        busRepository.getBusesCollection()
            .limit(1)
            .get()
            .addOnSuccessListener(querySnapshot -> {
                Log.d("DashboardActivity", "✅ Firestore connectivity OK - Found " + querySnapshot.size() + " test documents");
                Toast.makeText(this, "Firestore connection OK", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Log.e("DashboardActivity", "❌ Firestore connectivity FAILED: " + e.getMessage());

                String errorMessage = "Firestore connection failed: " + e.getMessage();
                if (e.getMessage().contains("index")) {
                    errorMessage += "\n\nSolution: Create composite index in Firebase Console";
                } else if (e.getMessage().contains("unreachable") || e.getMessage().contains("timeout")) {
                    errorMessage += "\n\nSolution: Check internet connection";
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            });

        // Test specific query for debugging
        Log.d("DashboardActivity", "=== TESTING SPECIFIC QUERY ===");
        Query testQuery = busRepository.getBusesCollection()
            .whereEqualTo("keberangkatan", "Gambir")
            .limit(5);

        testQuery.get().addOnSuccessListener(snapshot -> {
            Log.d("DashboardActivity", "✅ Test query successful - Found " + snapshot.size() + " Gambir buses");
            for (DocumentSnapshot doc : snapshot) {
                Bus bus = doc.toObject(Bus.class);
                if (bus != null) {
                    Log.d("DashboardActivity", "   - " + bus.getNama() + " → " + bus.getTujuan() +
                          " at " + bus.getWaktuKeberangkatan());
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("DashboardActivity", "❌ Test query failed: " + e.getMessage());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (busListener != null) {
            busListener.remove();
        }
    }
}

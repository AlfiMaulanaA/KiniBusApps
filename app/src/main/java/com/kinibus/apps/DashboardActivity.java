package com.kinibus.apps;

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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
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

    private String origin = "Kp. Rambutan";
    private String destination = "Lw. Panjang";
    private Date tripDate = getInitialDate();
    private String passengers = "1 Orang";
    private String activeClassFilter = "all";
    private String activeSort = "cheapest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus);

        busRepository = new BusRepository();
        initializeViews();
        listenForBusUpdates();
    }

    private Date getInitialDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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
            EditTripBottomSheetFragment bottomSheet = EditTripBottomSheetFragment.newInstance(
                    origin, destination, tripDate.getTime(), passengers);
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
            listenForBusUpdates();
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
        
        query = query.whereGreaterThanOrEqualTo("waktuKeberangkatan", startDate)
                     .whereLessThan("waktuKeberangkatan", endDate)
                     .orderBy("waktuKeberangkatan", Query.Direction.ASCENDING);

        busListener = busRepository.listenForBusUpdates(query, (snapshot, error) -> {
            if (error != null) {
                Log.e("DashboardActivity", "Listen failed.", error);
                return;
            }

            if (snapshot != null) {
                busList.clear();
                for (DocumentSnapshot document : snapshot) {
                    Bus bus = document.toObject(Bus.class);
                    if (bus != null) {
                        bus.setId(document.getId());
                        busList.add(bus);
                    }
                }
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
        Intent intent = new Intent(this, DetailKursiActivity.class);
        intent.putExtra("BUS_ID", bus.getId());
        intent.putExtra("PASSENGERS", this.passengers);
        startActivity(intent);
    }

    private void updateToolbarText() {
        updateToolbarText(this.passengers);
    }

    private void updateToolbarText(String passengers) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM", Locale.getDefault());
        tvRoute.setText(origin + " → " + destination);
        tvTripDetails.setText(sdf.format(tripDate) + " • " + passengers);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (busListener != null) {
            busListener.remove();
        }
    }
}

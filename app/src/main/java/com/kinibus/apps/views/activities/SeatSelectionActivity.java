package com.kinibus.apps.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Bus;
import com.kinibus.apps.models.Seat;
import com.kinibus.apps.views.adapters.SeatAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * SeatSelectionActivity - Halaman pemilihan kursi bus
 * Features:
 * - Seat grid dengan status (available/selected/booked)
 * - Real-time seat availability dari Firestore
 * - Multi-seat selection
 * - Dynamic price calculation
 * - Navigate to Booking Summary
 */
public class SeatSelectionActivity extends AppCompatActivity implements SeatAdapter.OnSeatClickListener {
    
    private static final String TAG = "SeatSelectionActivity";
    private static final int SEAT_COLUMNS = 4;  // 4 columns (A, B, C, D)
    
    // UI Components
    private RecyclerView rvSeatGrid;
    private TextView tvBusInfo, tvSelectedSeats, tvTotalPrice;
    private MaterialButton btnContinue;
    
    // Data
    private SeatAdapter seatAdapter;
    private List<Seat> seatList;
    private Bus selectedBus;
    private long pricePerSeat;
    
    // Firebase
    private FirebaseFirestore db;
    private ListenerRegistration seatListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        
        // Get bus data dari intent
        selectedBus = (Bus) getIntent().getSerializableExtra("bus");
        if (selectedBus == null) {
            Toast.makeText(this, "Error: Bus data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        pricePerSeat = selectedBus.getHarga();
        
        // Initialize views
        initializeViews();
        
        // Setup seat grid
        setupSeatGrid();
        
        // Load seat data
        loadSeatData();
        
        // Setup listeners
        setupListeners();
        
        Log.d(TAG, "SeatSelectionActivity created for bus: " + selectedBus.getNama());
    }
    
    /**
     * Initialize UI components
     */
    private void initializeViews() {
        // Find views
        rvSeatGrid = findViewById(R.id.rv_seat_grid);
        tvBusInfo = findViewById(R.id.tv_bus_info);
        tvSelectedSeats = findViewById(R.id.tv_selected_seats);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnContinue = findViewById(R.id.btn_continue);
        
        // Set bus info
        String busInfo = selectedBus.getNama() + " • " + selectedBus.getJenis();
        tvBusInfo.setText(busInfo);
        
        // Set toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pilih Kursi");
        }
        
        // Initial state - no seats selected
        updateBottomBar();
    }
    
    /**
     * Setup seat grid RecyclerView
     */
    private void setupSeatGrid() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, SEAT_COLUMNS);
        rvSeatGrid.setLayoutManager(layoutManager);
        
        seatList = new ArrayList<>();
        seatAdapter = new SeatAdapter(this, seatList, this);
        rvSeatGrid.setAdapter(seatAdapter);
    }
    
    /**
     * Load seat data - Generate 5 rows x 4 columns grid
     */
    private void loadSeatData() {
        seatList.clear();
        
        // Generate 5 rows (1-5) x 4 columns (A-D)
        String[] columns = {"A", "B", "C", "D"};
        
        for (int row = 1; row <= 5; row++) {
            for (String col : columns) {
                String seatNumber = row + col;
                
                // Position 3C is driver seat (SOPIR)
                if (seatNumber.equals("3C")) {
                    Seat driverSeat = new Seat(seatNumber, true);
                    seatList.add(driverSeat);
                } else {
                    Seat seat = new Seat(seatNumber, Seat.SeatStatus.AVAILABLE);
                    seatList.add(seat);
                }
            }
        }
        
        // Load booked seats from Firestore
        loadBookedSeatsFromFirestore();
    }
    
    /**
     * Load booked seats dari Firestore real-time
     */
    private void loadBookedSeatsFromFirestore() {
        if (selectedBus.getId() == null) {
            Log.w(TAG, "Bus ID is null, using generated seats");
            seatAdapter.updateSeats(seatList);
            return;
        }
        
        // Listen to bus document untuk kursi yang sudah dipesan
        seatListener = db.collection("buses")
                .document(selectedBus.getId())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error listening to seat updates", error);
                        return;
                    }
                    
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Get booked seats list (jika ada field bookedSeats)
                        List<String> bookedSeats = (List<String>) documentSnapshot.get("bookedSeats");
                        
                        if (bookedSeats != null) {
                            // Update seat status
                            for (Seat seat : seatList) {
                                if (bookedSeats.contains(seat.getSeatNumber()) && 
                                    seat.getStatus() != Seat.SeatStatus.SELECTED) {
                                    seat.setStatus(Seat.SeatStatus.BOOKED);
                                }
                            }
                        }
                        
                        seatAdapter.updateSeats(seatList);
                        Log.d(TAG, "Seats updated from Firestore");
                    }
                });
    }
    
    /**
     * Setup button listeners
     */
    private void setupListeners() {
        // Continue button - navigate to Booking Summary
        btnContinue.setOnClickListener(v -> {
            List<Seat> selectedSeats = getSelectedSeats();
            
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Pilih minimal 1 kursi", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Navigate to BookingSummaryActivity
            Intent intent = new Intent(this, BookingSummaryActivity.class);
            intent.putExtra("bus", selectedBus);
            intent.putExtra("selectedSeats", new ArrayList<>(selectedSeats));
            intent.putExtra("totalPrice", calculateTotalPrice());
            startActivity(intent);
        });
    }
    
    /**
     * Handle seat click dari adapter
     */
    @Override
    public void onSeatClicked(Seat seat, int position) {
        // Toggle selection
        seat.toggleSelection();
        
        // Update UI
        seatAdapter.notifyItemChanged(position);
        updateBottomBar();
        
        Log.d(TAG, "Seat " + seat.getSeatNumber() + " clicked - Status: " + seat.getStatus());
    }
    
    /**
     * Update bottom summary bar
     */
    private void updateBottomBar() {
        List<Seat> selectedSeats = getSelectedSeats();
        
        if (selectedSeats.isEmpty()) {
            tvSelectedSeats.setText("-");
            tvTotalPrice.setText("Rp 0");
            btnContinue.setEnabled(false);
        } else {
            // Show selected seat numbers
            String seatNumbers = selectedSeats.stream()
                    .map(Seat::getSeatNumber)
                    .collect(Collectors.joining(", "));
            tvSelectedSeats.setText(seatNumbers);
            
            // Show total price
            long totalPrice = calculateTotalPrice();
            tvTotalPrice.setText(formatCurrency(totalPrice));
            
            btnContinue.setEnabled(true);
        }
    }
    
    /**
     * Get list of selected seats
     */
    private List<Seat> getSelectedSeats() {
        return seatList.stream()
                .filter(Seat::isSelected)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate total price based on selected seats
     */
    private long calculateTotalPrice() {
        int selectedCount = (int) seatList.stream()
                .filter(Seat::isSelected)
                .count();
        return selectedCount * pricePerSeat;
    }
    
    /**
     * Format price ke format Rupiah
     */
    private String formatCurrency(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formatted = formatter.format(amount);
        // Remove decimal part untuk Rupiah
        return formatted.replace(",00", "");
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove Firestore listener
        if (seatListener != null) {
            seatListener.remove();
        }
        Log.d(TAG, "SeatSelectionActivity destroyed");
    }
}

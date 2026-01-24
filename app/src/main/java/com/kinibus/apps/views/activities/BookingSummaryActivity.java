package com.kinibus.apps.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Booking;
import com.kinibus.apps.models.Bus;
import com.kinibus.apps.models.Seat;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * BookingSummaryActivity - Ringkasan pemesanan sebelum payment
 * Shows:
 * - Journey details (departure, arrival, date, time)
 * - Bus information
 * - Selected seats
 * - Price breakdown
 * - Total payment
 */
public class BookingSummaryActivity extends AppCompatActivity {
    
    private static final String TAG = "BookingSummaryActivity";
    
    // UI Components
    private TextView tvDeparture, tvArrival, tvDepartureTime, tvArrivalTime, tvDate;
    private TextView tvBusName, tvBusPlate, tvSeats, tvTotalPrice, tvServiceFee, tvFinalPrice;
    private MaterialButton btnProceed;
    
    // Data
    private Bus selectedBus;
    private ArrayList<Seat> selectedSeats;
    private long totalPrice;
    private long serviceFee = 5000; // Service fee Rp 5.000
    
    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        
        // Get data from intent
        selectedBus = (Bus) getIntent().getSerializableExtra("bus");
        selectedSeats = (ArrayList<Seat>) getIntent().getSerializableExtra("selectedSeats");
        totalPrice = getIntent().getLongExtra("totalPrice", 0);
        
        if (selectedBus == null || selectedSeats == null || selectedSeats.isEmpty()) {
            Toast.makeText(this, "Error: Booking data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize views
        initializeViews();
        
        // Display booking data
        displayBookingData();
        
        // Setup listeners
        setupListeners();
        
        Log.d(TAG, "BookingSummaryActivity created - Seats: " + selectedSeats.size());
    }
    
    private void initializeViews() {
        // Journey details
        tvDeparture = findViewById(R.id.tv_departure);
        tvArrival = findViewById(R.id.tv_arrival);
        tvDepartureTime = findViewById(R.id.tv_departure_time);
        tvArrivalTime = findViewById(R.id.tv_arrival_time);
        tvDate = findViewById(R.id.tv_date);
        
        // Bus info
        tvBusName = findViewById(R.id.tv_bus_name);
        tvBusPlate = findViewById(R.id.tv_bus_plate);
        tvSeats = findViewById(R.id.tv_seats);
        
        // Pricing
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvServiceFee = findViewById(R.id.tv_service_fee);
        tvFinalPrice = findViewById(R.id.tv_final_price);
        
        // Button
        btnProceed = findViewById(R.id.btn_proceed_payment);
        
        // Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ringkasan Pemesanan");
        }
    }
    
    private void displayBookingData() {
        // Journey info
        tvDeparture.setText(selectedBus.getKeberangkatan());
        tvArrival.setText(selectedBus.getTujuan());
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", new Locale("id", "ID"));
        
        if (selectedBus.getWaktuKeberangkatan() != null) {
            tvDepartureTime.setText(timeFormat.format(selectedBus.getWaktuKeberangkatan()) + " WIB");
            tvDate.setText(dateFormat.format(selectedBus.getWaktuKeberangkatan()));
        }
        
        if (selectedBus.getWaktuTiba() != null) {
            tvArrivalTime.setText(timeFormat.format(selectedBus.getWaktuTiba()) + " WIB");
        }
        
        // Bus info
        tvBusName.setText(selectedBus.getNama());
        
        // Plate number - use from bus model if available, otherwise use generic format
        String plateNumber = selectedBus.getId() != null ? 
            "Plat: " + generatePlateNumber(selectedBus.getId()) : "Plat: -";
        tvBusPlate.setText(plateNumber);
        
        // Seats
        String seatNumbers = selectedSeats.stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(", "));
        tvSeats.setText(seatNumbers);
        
        // Pricing
        tvTotalPrice.setText(formatCurrency(totalPrice));
        tvServiceFee.setText(formatCurrency(serviceFee));
        
        long finalPrice = totalPrice + serviceFee;
        tvFinalPrice.setText(formatCurrency(finalPrice));
    }
    
    private void setupListeners() {
        btnProceed.setOnClickListener(v -> {
            processPayment();
        });
    }
    
    /**
     * Process payment simulation dan save booking ke Firestore
     */
    private void processPayment() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Anda harus login terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Generate booking code
        String bookingCode = generateBookingCode();
        
        // Create booking object
        List<String> seatNumbers = selectedSeats.stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.toList());
        
        Booking booking = new Booking();
        booking.setUserId(currentUser.getUid());
        booking.setBusId(selectedBus.getId());
        booking.setBusName(selectedBus.getNama());
        booking.setDeparture(selectedBus.getKeberangkatan());
        booking.setDestination(selectedBus.getTujuan());
        booking.setDepartureDate(selectedBus.getWaktuKeberangkatan());
        booking.setDepartureTime(new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(selectedBus.getWaktuKeberangkatan()) + " WIB");
        booking.setPrice(totalPrice + serviceFee);
        booking.setPassengerCount(selectedSeats.size());
        booking.setSelectedSeats(seatNumbers);
        booking.setBookingStatus("confirmed");
        booking.setBookingCode(bookingCode);
        booking.setCreatedAt(new Date());
        booking.setUpdatedAt(new Date());
        
        // Save to Firestore
        saveBookingToFirestore(booking, bookingCode);
    }
    
    private void saveBookingToFirestore(Booking booking, String bookingCode) {
        db.collection("penyewaan")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    String bookingId = documentReference.getId();
                    booking.setBookingId(bookingId);
                    
                    Log.d(TAG, "Booking saved successfully - Code: " + bookingCode);
                    
                    // Navigate to Payment Confirmation
                    Intent intent = new Intent(this, PaymentConfirmationActivity.class);
                    intent.putExtra("booking", booking);
                    intent.putExtra("bookingCode", bookingCode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving booking", e);
                    Toast.makeText(this, "Gagal menyimpan booking: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
    
    /**
     * Generate unique booking code (GB-XXXXXX format)
     */
    private String generateBookingCode() {
        Random random = new Random();
        int randomNum = 100000 + random.nextInt(900000); // 6 digit number
        return "GB-" + randomNum;
    }
    
    /**
     * Generate plate number from bus ID untuk display purposes
     * Format: B XXXX YZ (where XXXX is derived from bus ID hash)
     */
    private String generatePlateNumber(String busId) {
        if (busId == null || busId.isEmpty()) {
            return "-";
        }
        
        // Generate 4-digit number from bus ID hashcode
        int hash = Math.abs(busId.hashCode());
        int number = 1000 + (hash % 9000); // 1000-9999
        
        // Generate 2 letters from bus ID
        char letter1 = (char) ('A' + (hash % 26));
        char letter2 = (char) ('A' + ((hash / 26) % 26));
        
        return "B " + number + " " + letter1 + letter2;
    }
    
    private String formatCurrency(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount).replace(",00", "");
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

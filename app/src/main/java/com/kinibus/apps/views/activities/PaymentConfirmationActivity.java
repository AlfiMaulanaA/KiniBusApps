package com.kinibus.apps.views.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Booking;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * PaymentConfirmationActivity - Success screen setelah payment
 * Displays:
 * - Success message
 * - Booking code (copyable)
 * - Journey map illustration
 * - Date & time details
 * - Navigate to E-Ticket
 */
public class PaymentConfirmationActivity extends AppCompatActivity {
    
    private static final String TAG = "PaymentConfirmation";
    
    // UI Components
    private TextView tvBookingCode, tvOriginCity, tvOriginTerminal;
    private TextView tvDestinationCity, tvDestinationTerminal, tvDate, tvTime;
    private ImageButton btnClose, btnCopyCode;
    private MaterialButton btnViewTicket;
    private TextView tvBackHome;
    
    // Data
    private Booking booking;
    private String bookingCode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        
        // Get data from intent
        booking = (Booking) getIntent().getSerializableExtra("booking");
        bookingCode = getIntent().getStringExtra("bookingCode");
        
        if (booking == null || bookingCode == null) {
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
        
        Log.d(TAG, "Payment confirmation displayed - Code: " + bookingCode);
    }
    
    private void initializeViews() {
        // Booking code
        tvBookingCode = findViewById(R.id.tv_booking_code);
        btnCopyCode = findViewById(R.id.btn_copy_code);
        
        // Journey info
        tvOriginCity = findViewById(R.id.tv_origin_city);
        tvOriginTerminal = findViewById(R.id.tv_origin_terminal);
        tvDestinationCity = findViewById(R.id.tv_destination_city);
        tvDestinationTerminal = findViewById(R.id.tv_destination_terminal);
        
        // Date & time
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        
        // Buttons
        btnClose = findViewById(R.id.btn_close);
        btnViewTicket = findViewById(R.id.btn_view_ticket);
        tvBackHome = findViewById(R.id.tv_back_home);
    }
    
    private void displayBookingData() {
        // Booking code
        tvBookingCode.setText(bookingCode);
        
        // Journey details
        tvOriginCity.setText(booking.getDeparture());
        tvOriginTerminal.setText("Terminal " + booking.getDeparture());
        tvDestinationCity.setText(booking.getDestination());
        tvDestinationTerminal.setText("Terminal " + booking.getDestination());
        
        // Date & time
        if (booking.getDepartureDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy", 
                    new Locale("id", "ID"));
            tvDate.setText(dateFormat.format(booking.getDepartureDate()));
        }
        
        if (booking.getDepartureTime() != null) {
            tvTime.setText(booking.getDepartureTime());
        }
    }
    
    private void setupListeners() {
        // Close button - back to home
        btnClose.setOnClickListener(v -> navigateToHome());
        
        // Copy booking code
        btnCopyCode.setOnClickListener(v -> copyBookingCode());
        
        // View E-Ticket button
        btnViewTicket.setOnClickListener(v -> {
            Intent intent = new Intent(this, ETicketActivity.class);
            intent.putExtra("booking", booking);
            intent.putExtra("bookingCode", bookingCode);
            startActivity(intent);
        });
        
        // Back to home link
        tvBackHome.setOnClickListener(v -> navigateToHome());
    }
    
    /**
     * Copy booking code ke clipboard
     */
    private void copyBookingCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Booking Code", bookingCode);
        clipboard.setPrimaryClip(clip);
        
        Toast.makeText(this, "Kode booking disalin: " + bookingCode, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Booking code copied: " + bookingCode);
    }
    
    /**
     * Navigate to home (DashboardActivity)
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        navigateToHome();
    }
}

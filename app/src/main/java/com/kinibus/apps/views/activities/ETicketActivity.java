package com.kinibus.apps.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Booking;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * ETicketActivity - Display E-Ticket dengan QR code
 * Features:
 * - QR code generation dari booking code
 * - Complete journey details
 * - Bus information
 * - Seat assignments
 * - Download/Share functionality
 */
public class ETicketActivity extends AppCompatActivity {
    
    private static final String TAG = "ETicketActivity";
    private static final int QR_CODE_SIZE = 500; // pixels
    
    // UI Components
    private ImageView imgQrCode;
    private TextView tvBookingCodeHeader, tvOrigin, tvDestination;
    private TextView tvDate, tvTime, tvBusName, tvSeats, tvPassengerCount;
    private MaterialButton btnDownload, btnShare;
    private TextView tvClose;
    
    // Data
    private Booking booking;
    private String bookingCode;
    private Bitmap qrCodeBitmap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);
        
        // Get data from intent
        booking = (Booking) getIntent().getSerializableExtra("booking");
        bookingCode = getIntent().getStringExtra("bookingCode");
        
        if (booking == null || bookingCode == null) {
            Toast.makeText(this, "Error: Ticket data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize views
        initializeViews();
        
        // Generate QR code
        generateQRCode();
        
        // Display ticket data
        displayTicketData();
        
        // Setup listeners
        setupListeners();
        
        Log.d(TAG, "E-Ticket displayed - Code: " + bookingCode);
    }
    
    private void initializeViews() {
        // QR Code
        imgQrCode = findViewById(R.id.img_qr_code);
        
        // Header
        tvBookingCodeHeader = findViewById(R.id.tv_booking_code_header);
        
        // Journey details
        tvOrigin = findViewById(R.id.tv_origin);
        tvDestination = findViewById(R.id.tv_destination);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        
        // Bus details
        tvBusName = findViewById(R.id.tv_bus_name);
        tvSeats = findViewById(R.id.tv_seats);
        tvPassengerCount = findViewById(R.id.tv_passenger_count);
        
        // Buttons
        btnDownload = findViewById(R.id.btn_download);
        btnShare = findViewById(R.id.btn_share);
        tvClose = findViewById(R.id.tv_close);
        
        // Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("E-Tiket");
        }
    }
    
    /**
     * Generate QR code dari booking code menggunakan ZXing
     */
    private void generateQRCode() {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            
            // Create bit matrix
            BitMatrix bitMatrix = writer.encode(
                    bookingCode,
                    BarcodeFormat.QR_CODE,
                    QR_CODE_SIZE,
                    QR_CODE_SIZE
            );
            
            // Convert bit matrix to bitmap
            qrCodeBitmap = Bitmap.createBitmap(
                    QR_CODE_SIZE,
                    QR_CODE_SIZE,
                    Bitmap.Config.RGB_565
            );
            
            for (int x = 0; x < QR_CODE_SIZE; x++) {
                for (int y = 0; y < QR_CODE_SIZE; y++) {
                    qrCodeBitmap.setPixel(
                            x,
                            y,
                            bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE
                    );
                }
            }
            
            // Display QR code
            imgQrCode.setImageBitmap(qrCodeBitmap);
            
            Log.d(TAG, "QR code generated successfully");
            
        } catch (WriterException e) {
            Log.e(TAG, "Error generating QR code", e);
            Toast.makeText(this, "Gagal generate QR code", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Display ticket data
     */
    private void displayTicketData() {
        // Booking code
        tvBookingCodeHeader.setText(bookingCode);
        
        // Journey details
        tvOrigin.setText(booking.getDeparture());
        tvDestination.setText(booking.getDestination());
        
        // Date formatting
        if (booking.getDepartureDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "EEEE, dd MMM yyyy",
                    new Locale("id", "ID")
            );
            tvDate.setText(dateFormat.format(booking.getDepartureDate()));
        }
        
        // Time
        if (booking.getDepartureTime() != null) {
            tvTime.setText(booking.getDepartureTime());
        }
        
        // Bus details
        tvBusName.setText(booking.getBusName());
        
        // Seats - join list dengan comma
        if (booking.getSelectedSeats() != null && !booking.getSelectedSeats().isEmpty()) {
            String seats = String.join(", ", booking.getSelectedSeats());
            tvSeats.setText(seats);
        }
        
        // Passenger count
        int count = booking.getPassengerCount();
        tvPassengerCount.setText(count + " orang");
    }
    
    /**
     * Setup button listeners
     */
    private void setupListeners() {
        // Download button - TODO: Implement PDF download
        btnDownload.setOnClickListener(v -> {
            Toast.makeText(this, "Download feature coming soon", Toast.LENGTH_SHORT).show();
            // TODO: Generate PDF dengan iText atau Android PdfDocument
        });
        
        // Share button - Share booking code
        btnShare.setOnClickListener(v -> {
            shareTicket();
        });
        
        // Close button
        tvClose.setOnClickListener(v -> {
            finish();
        });
    }
    
    /**
     * Share ticket via share intent
     */
    private void shareTicket() {
        String shareText = "KiniBus E-Ticket\n\n" +
                "Booking Code: " + bookingCode + "\n" +
                "Bus: " + booking.getBusName() + "\n" +
                "Dari: " + booking.getDeparture() + "\n" +
                "Ke: " + booking.getDestination() + "\n" +
                "Tanggal: " + (booking.getDepartureDate() != null ? 
                        new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                .format(booking.getDepartureDate()) : "-") + "\n" +
                "Jam: " + booking.getDepartureTime() + "\n" +
                "Kursi: " + String.join(", ", booking.getSelectedSeats()) + "\n\n" +
                "Terima kasih telah menggunakan KiniBus!";
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "KiniBus E-Ticket");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        
        startActivity(Intent.createChooser(shareIntent, "Bagikan tiket via"));
        
        Log.d(TAG, "Share ticket initiated");
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

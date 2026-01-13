package com.kinibus.apps;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.ListenerRegistration;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailKursiActivity extends AppCompatActivity {

    private BusRepository busRepository;
    private ListenerRegistration busDetailListener;
    private String busId;
    private int numPassengers = 1;
    private Bus currentBus;

    private TextView tvBusName, tvBusPrice, tvDeparture, tvArrival, tvSeatsLeft;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kursi);

        busRepository = new BusRepository();
        busId = getIntent().getStringExtra("BUS_ID");
        String passengersStr = getIntent().getStringExtra("PASSENGERS");

        if (passengersStr != null && !passengersStr.isEmpty()) {
            try {
                numPassengers = Integer.parseInt(passengersStr.replaceAll("\\D+", ""));
            } catch (NumberFormatException e) {
                numPassengers = 1;
            }
        }

        initializeViews();

        if (busId != null) {
            listenForBusDetails();
        }

        btnBook.setOnClickListener(v -> bookSeat());
    }

    private void initializeViews() {
        tvBusName = findViewById(R.id.tv_bus_name_detail);
        tvBusPrice = findViewById(R.id.tv_bus_price_detail);
        tvDeparture = findViewById(R.id.tv_departure_detail);
        tvArrival = findViewById(R.id.tv_arrival_detail);
        tvSeatsLeft = findViewById(R.id.tv_seats_left_detail);
        btnBook = findViewById(R.id.btn_book);
    }

    private void listenForBusDetails() {
        busDetailListener = busRepository.getBusDetails(busId, (snapshot, error) -> {
            if (error != null) {
                Toast.makeText(this, "Failed to load bus details.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                currentBus = snapshot.toObject(Bus.class);
                if (currentBus != null) {
                    updateUI();
                }
            } else {
                Toast.makeText(this, "Bus not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tvBusName.setText(currentBus.getNama());
        tvBusPrice.setText(String.format(Locale.getDefault(), "Rp %,d", currentBus.getHarga()));
        
        String departureInfo = "";
        if(currentBus.getWaktuKeberangkatan() != null) {
            departureInfo = "Berangkat: " + currentBus.getKeberangkatan() + " (" + timeFormat.format(currentBus.getWaktuKeberangkatan()) + ")";
        }
        tvDeparture.setText(departureInfo);

        String arrivalInfo = "";
        if(currentBus.getWaktuTiba() != null) {
             arrivalInfo = "Tiba: " + currentBus.getTujuan() + " (" + timeFormat.format(currentBus.getWaktuTiba()) + ")";
        }
        tvArrival.setText(arrivalInfo);
        
        tvSeatsLeft.setText("Sisa Kursi: " + currentBus.getKursiTersedia());

        if (currentBus.isTersedia() && currentBus.getKursiTersedia() >= numPassengers) {
            btnBook.setEnabled(true);
            btnBook.setText("Book for " + numPassengers + " Orang");
        } else {
            btnBook.setEnabled(false);
            if (!currentBus.isTersedia()) {
                btnBook.setText("Not Available");
            } else {
                btnBook.setText("Kursi tidak cukup");
            }
        }
    }

    private void bookSeat() {
        if (currentBus != null && currentBus.isTersedia() && currentBus.getKursiTersedia() >= numPassengers) {
            busRepository.bookSeat(busId, numPassengers).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailKursiActivity.this, "Booking successful!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetailKursiActivity.this, "Booking failed. Kursi mungkin sudah dipesan.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (busDetailListener != null) {
            busDetailListener.remove();
        }
    }
}
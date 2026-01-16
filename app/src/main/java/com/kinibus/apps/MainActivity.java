package com.kinibus.apps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TicketAdapter adapter;
    private List<Ticket> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout XML 2 (History)

        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView (Pastikan Anda mengubah ScrollView di XML menjadi RecyclerView)
        recyclerView = findViewById(R.id.rvTickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketList = new ArrayList<>();
        adapter = new TicketAdapter(this, ticketList);
        recyclerView.setAdapter(adapter);

        // Setup Bottom Nav
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_ticket) {
                // Already here
                return true;
            } else {
                Toast.makeText(this, "Fitur ini belum tersedia, kembali ke Tiket.", Toast.LENGTH_SHORT).show();
                return false; // Tetap di tab ticket
            }
        });

        // Check Auth
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            signInAnonymously();
        } else {
            loadTickets(currentUser.getUid());
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        loadTickets(user.getUid());
                    } else {
                        Toast.makeText(MainActivity.this, "Auth Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadTickets(String userId) {
        // Path: users/{userId}/tickets
        db.collection("users").document(userId).collection("tickets")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ticketList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ticket ticket = document.toObject(Ticket.class);
                            ticket.setId(document.getId());
                            ticketList.add(ticket);
                        }

                        if (ticketList.isEmpty()) {
                            seedDummyData(userId);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal memuat data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void seedDummyData(String userId) {
        // Data Dummy 1
        Map<String, Object> t1 = new HashMap<>();
        t1.put("bus_nama", "KiniBus Shuttle");
        t1.put("jenis", "Executive Class");
        t1.put("seat", "5D");
        t1.put("rute_awal", "Bandung (Pasteur)");
        t1.put("rute_akhir", "Jakarta (Gambir)");
        t1.put("jam_berangkat", "08:00");
        t1.put("jam_sampai", "11:30");
        t1.put("tanggal_sewa", "2023-10-12");
        t1.put("status", "E_TIKET_TERBIT");
        t1.put("ref_code", "KNB-88219");
        t1.put("harga", 120000);
        t1.put("penumpang", "Rahmat Santoso");
        t1.put("created_at", FieldValue.serverTimestamp());

        db.collection("users").document(userId).collection("tickets").add(t1);

        // Data Dummy 2
        Map<String, Object> t2 = new HashMap<>();
        t2.put("bus_nama", "Rosalia Indah");
        t2.put("jenis", "Super Top SHD");
        t2.put("seat", "2A");
        t2.put("rute_awal", "Jakarta");
        t2.put("rute_akhir", "Semarang");
        t2.put("jam_berangkat", "19:00");
        t2.put("jam_sampai", "04:00");
        t2.put("tanggal_sewa", "2023-11-05");
        t2.put("status", "MENUNGGU_BAYAR");
        t2.put("ref_code", "ROS-99123");
        t2.put("harga", 250000);
        t2.put("penumpang", "Rahmat Santoso");
        t2.put("created_at", FieldValue.serverTimestamp());

        db.collection("users").document(userId).collection("tickets").add(t2)
                .addOnSuccessListener(doc -> loadTickets(userId)); // Reload setelah seed
    }
}
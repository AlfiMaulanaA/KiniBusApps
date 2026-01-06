# Integrasi Firebase untuk KiniBus Android App

Panduan lengkap untuk mengganti SQLite lokal dengan Firebase (Firestore + Authentication) pada aplikasi KiniBus.

## 📋 Daftar Isi
- [Persiapan](#-persiapan)
- [Setup Firebase Project](#-setup-firebase-project)
- [Konfigurasi Android Project](#-konfigurasi-android-project)
- [Implementasi Authentication](#-implementasi-authentication)
- [Setup Firestore Database](#-setup-firestore-database)
- [Model Data & Repository](#-model-data--repository)
- [Implementasi CRUD Operations](#-implementasi-crud-operations)
- [Real-time Updates](#-real-time-updates)
- [Testing & Deployment](#-testing--deployment)
- [Troubleshooting](#-troubleshooting)

## 🔧 Persiapan

### Prasyarat
- Android Studio versi Arctic Fox atau lebih baru
- Google account untuk Firebase Console
- Project KiniBus sudah ter-setup

### Dependencies yang Dibutuhkan
Tambahkan ke `app/build.gradle.kts`:
```kotlin
dependencies {
    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Firebase UI (optional, untuk auth UI)
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    // Google Play Services (untuk auth)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
}
```

## 🚀 Setup Firebase Project

### Langkah 1: Buat Firebase Project
1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik "Create a project" atau "Add project"
3. Masukkan nama project: `KiniBus-Android`
4. Enable Google Analytics (opsional)
5. Pilih Google Analytics account
6. Klik "Create project"

### Langkah 2: Tambahkan Android App
1. Di project overview, klik ikon Android
2. Masukkan package name: `com.example.myapplication`
3. Masukkan app nickname: `KiniBus App`
4. Download `google-services.json`
5. Pindahkan file ke folder `app/`

### Langkah 3: Enable Services
1. **Authentication**:
   - Pergi ke Authentication > Sign-in method
   - Enable Email/Password
   - Enable Google (opsional)

2. **Firestore Database**:
   - Pergi ke Firestore Database
   - Klik "Create database"
   - Pilih "Start in test mode" (untuk development)
   - Pilih lokasi: `asia-southeast1` (Singapore)

## ⚙️ Konfigurasi Android Project

### Langkah 1: Update build.gradle.kts (Project level)
```kotlin
plugins {
    // ... existing plugins
    id("com.google.gms.google-services") version "4.4.0" apply false
}
```

### Langkah 2: Update build.gradle.kts (App level)
```kotlin
plugins {
    // ... existing plugins
    id("com.google.gms.google-services")
}

dependencies {
    // ... existing dependencies

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Optional: Firebase UI
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
}
```

### Langkah 3: Update AndroidManifest.xml
Tambahkan permissions:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Internet permission untuk Firebase -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application>
        <!-- ... existing config -->
    </application>
</manifest>
```

## 🔐 Implementasi Authentication

### Buat FirebaseAuthHelper Class
```java
package com.example.myapplication.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

public class FirebaseAuthHelper {
    private FirebaseAuth mAuth;

    public FirebaseAuthHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Register user
    public Task<AuthResult> registerUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    // Login user
    public Task<AuthResult> loginUser(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    // Get current user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Logout
    public void logout() {
        mAuth.signOut();
    }

    // Password reset
    public Task<Void> resetPassword(String email) {
        return mAuth.sendPasswordResetEmail(email);
    }
}
```

### Update LoginActivity.java
```java
public class LoginActivity extends AppCompatActivity {
    private FirebaseAuthHelper authHelper;
    private EditText emailEdit, passwordEdit;
    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authHelper = new FirebaseAuthHelper();
        initializeViews();

        loginBtn.setOnClickListener(v -> loginUser());
        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void initializeViews() {
        emailEdit = findViewById(R.id.email_edit);
        passwordEdit = findViewById(R.id.password_edit);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
    }

    private void loginUser() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authHelper.loginUser(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Login berhasil
                    FirebaseUser user = authHelper.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Navigate ke Dashboard
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                } else {
                    // Login gagal
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void registerUser() {
        // Implementasi registrasi mirip dengan login
        // Gunakan authHelper.registerUser()
    }
}
```

## 🗄️ Setup Firestore Database

### Struktur Database Firestore
```
/kiniBus/
├── users/{userId}
│   ├── email: string
│   ├── nama: string
│   ├── createdAt: timestamp
│   └── profileImage: string (optional)
├── buses/
│   ├── {busId}/
│   │   ├── nama: string
│   │   ├── jenis: string
│   │   ├── kapasitas: number
│   │   ├── hargaPerHari: number
│   │   ├── gambar: string
│   │   └── tersedia: boolean
├── penyewaan/
│   ├── {penyewaanId}/
│   │   ├── userId: reference
│   │   ├── busId: reference
│   │   ├── tanggalSewa: timestamp
│   │   ├── durasi: number
│   │   ├── totalBiaya: number
│   │   └── status: string ("pending", "confirmed", "completed", "cancelled")
└── eTickets/
    ├── {ticketId}/
    │   ├── penyewaanId: reference
    │   ├── qrCode: string
    │   ├── pdfUrl: string
    │   └── createdAt: timestamp
```

### Buat FirestoreHelper Class
```java
package com.example.myapplication.helpers;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;

public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    // Collection references
    public CollectionReference getUsersCollection() {
        return db.collection("users");
    }

    public CollectionReference getBusesCollection() {
        return db.collection("buses");
    }

    public CollectionReference getPenyewaanCollection() {
        return db.collection("penyewaan");
    }

    public CollectionReference getETicketsCollection() {
        return db.collection("eTickets");
    }

    // Generic methods
    public Task<Void> addDocument(String collection, String documentId, Object data) {
        return db.collection(collection).document(documentId).set(data);
    }

    public Task<Void> updateDocument(String collection, String documentId, Object data) {
        return db.collection(collection).document(documentId).update(data);
    }

    public Task<Void> deleteDocument(String collection, String documentId) {
        return db.collection(collection).document(documentId).delete();
    }

    public Task<DocumentSnapshot> getDocument(String collection, String documentId) {
        return db.collection(collection).document(documentId).get();
    }

    public Task<QuerySnapshot> getCollection(String collection) {
        return db.collection(collection).get();
    }
}
```

## 📊 Model Data & Repository

### Buat Model Classes
```java
// User.java
package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class User {
    @DocumentId
    private String id;
    private String email;
    private String nama;
    private String profileImage;

    @ServerTimestamp
    private Date createdAt;

    // Constructors, getters, setters
    public User() {}

    public User(String email, String nama) {
        this.email = email;
        this.nama = nama;
    }

    // Getters and setters...
}

// Bus.java
package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentId;

public class Bus {
    @DocumentId
    private String id;
    private String nama;
    private String jenis;
    private int kapasitas;
    private double hargaPerHari;
    private String gambar;
    private boolean tersedia;

    // Constructors, getters, setters
    public Bus() {}

    public Bus(String nama, String jenis, int kapasitas, double hargaPerHari, String gambar) {
        this.nama = nama;
        this.jenis = jenis;
        this.kapasitas = kapasitas;
        this.hargaPerHari = hargaPerHari;
        this.gambar = gambar;
        this.tersedia = true;
    }

    // Getters and setters...
}

// Penyewaan.java
package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Penyewaan {
    @DocumentId
    private String id;
    private DocumentReference userId;
    private DocumentReference busId;
    private Date tanggalSewa;
    private int durasi;
    private double totalBiaya;
    private String status;

    @ServerTimestamp
    private Date createdAt;

    // Constructors, getters, setters
    public Penyewaan() {}

    public Penyewaan(DocumentReference userId, DocumentReference busId,
                     Date tanggalSewa, int durasi, double totalBiaya) {
        this.userId = userId;
        this.busId = busId;
        this.tanggalSewa = tanggalSewa;
        this.durasi = durasi;
        this.totalBiaya = totalBiaya;
        this.status = "pending";
    }

    // Getters and setters...
}
```

### Buat Repository Classes
```java
// UserRepository.java
package com.example.myapplication.repositories;

import com.example.myapplication.helpers.FirestoreHelper;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {
    private FirestoreHelper firestoreHelper;

    public UserRepository() {
        firestoreHelper = new FirestoreHelper();
    }

    public Task<Void> createUser(String userId, User user) {
        return firestoreHelper.addDocument("users", userId, user);
    }

    public Task<Void> updateUser(String userId, User user) {
        return firestoreHelper.updateDocument("users", userId, user);
    }

    public Task<QuerySnapshot> getAllUsers() {
        return firestoreHelper.getCollection("users");
    }
}

// BusRepository.java
package com.example.myapplication.repositories;

import com.example.myapplication.helpers.FirestoreHelper;
import com.example.myapplication.models.Bus;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class BusRepository {
    private FirestoreHelper firestoreHelper;

    public BusRepository() {
        firestoreHelper = new FirestoreHelper();
    }

    public Task<Void> addBus(Bus bus) {
        DocumentReference docRef = firestoreHelper.getBusesCollection().document();
        bus.setId(docRef.getId());
        return docRef.set(bus);
    }

    public Task<QuerySnapshot> getAllBuses() {
        return firestoreHelper.getBusesCollection().get();
    }

    public Task<QuerySnapshot> getAvailableBuses() {
        return firestoreHelper.getBusesCollection()
            .whereEqualTo("tersedia", true)
            .get();
    }

    public Task<Void> updateBus(String busId, Bus bus) {
        return firestoreHelper.updateDocument("buses", busId, bus);
    }
}
```

## 🔄 Implementasi CRUD Operations

### Contoh Penggunaan di Activity
```java
// Di DashboardActivity.java
public class DashboardActivity extends AppCompatActivity {
    private BusRepository busRepository;
    private RecyclerView busRecyclerView;
    private BusAdapter busAdapter;
    private List<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        busRepository = new BusRepository();
        initializeViews();
        loadBuses();
    }

    private void initializeViews() {
        busRecyclerView = findViewById(R.id.bus_recycler_view);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        busList = new ArrayList<>();
        busAdapter = new BusAdapter(busList);
        busRecyclerView.setAdapter(busAdapter);
    }

    private void loadBuses() {
        busRepository.getAllBuses()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    busList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Bus bus = document.toObject(Bus.class);
                        busList.add(bus);
                    }
                    busAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Failed to load buses", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
```

## 📡 Real-time Updates

### Implementasi Real-time Listener
```java
// Real-time listener untuk buses
private void listenToBusUpdates() {
    firestoreHelper.getBusesCollection()
        .addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            busList.clear();
            for (DocumentSnapshot doc : value) {
                Bus bus = doc.toObject(Bus.class);
                busList.add(bus);
            }
            busAdapter.notifyDataSetChanged();
        });
}
```

## 🧪 Testing & Deployment

### Testing
1. **Unit Test**: Test repository classes
2. **Integration Test**: Test Firebase connection
3. **UI Test**: Test authentication flow

### Deployment
1. Update security rules di Firestore
2. Enable App Check untuk security
3. Monitor usage di Firebase Console

### Contoh Firestore Security Rules
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    // Anyone can read buses, only admin can write
    match /buses/{busId} {
      allow read: if true;
      allow write: if request.auth != null && request.auth.token.admin == true;
    }

    // Users can read/write their own bookings
    match /penyewaan/{penyewaanId} {
      allow read, write: if request.auth != null &&
        resource.data.userId == /databases/$(database)/documents/users/$(request.auth.uid);
    }
  }
}
```

## 🔧 Troubleshooting

### Masalah Umum & Solusi

1. **Authentication Failed**
   - Pastikan google-services.json sudah benar
   - Check internet connection
   - Verify SHA-1 fingerprint di Firebase Console

2. **Firestore Permission Denied**
   - Update Firestore security rules
   - Pastikan user sudah login

3. **Real-time Updates Not Working**
   - Check Firestore rules untuk read access
   - Ensure network connectivity

4. **Build Failed**
   - Clean project: Build > Clean Project
   - Invalidate caches: File > Invalidate Caches / Restart

### Debug Tips
- Gunakan Firebase Console untuk monitor real-time data
- Enable debug logging: `FirebaseFirestore.setLoggingEnabled(true);`
- Test dengan Firebase Emulator untuk development

## 📚 Referensi
- [Firebase Android Documentation](https://firebase.google.com/docs/android/setup)
- [Firestore Android Guide](https://firebase.google.com/docs/firestore/quickstart)
- [Firebase Authentication](https://firebase.google.com/docs/auth/android/start)

---

**Catatan**: Instruksi ini mengasumsikan familiarity dengan Android development. Untuk pemula, ikuti tutorial Firebase Android secara bertahap.
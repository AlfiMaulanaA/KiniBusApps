# 🛠️ Panduan Setup Firebase untuk KiniBusApps

**Project Name**: KiniBusApps  
**Package Name**: `com.kinibus.apps`  
**App Name**: KiniBus Apps

Berdasarkan instruksi di `FIREBASE_INTEGRATION.md`, berikut adalah panduan **SUDAH DISESUAIKAN** dengan konfigurasi project saat ini agar tidak salah config.

## 🔧 Konfigurasi Firebase Console Step-by-Step

### **Langkah 1: Buat Project Firebase**
1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Create a project"** atau **"Add project"**
3. Masukkan nama project: `KiniBusApps`
4. **PENTING**: Pastikan project ID unik (akan generate otomatis, misal: `kinibusapps-12345`)
5. Enable Google Analytics: **Ya** (untuk tracking penggunaan)
6. Pilih Google Analytics account (atau buat baru)
7. Klik **"Create project"** - tunggu beberapa detik

### **Langkah 2: Tambahkan Android App** DONE
1. Di halaman project overview, klik ikon **Android** (hijau)
2. **Android package name**: `com.kinibus.apps`
   - ✅ **SUDAH SESUAI** dengan `namespace` di `app/build.gradle.kts`
   - ⚠️ **PASTIKAN BENAR**: Jangan gunakan `com.example.myapplication`
3. **App nickname**: `KiniBus Apps`
4. **Debug signing certificate SHA-1** (opsional untuk sekarang):
   - Bisa diisi nanti jika perlu Google Sign-In
5. Klik **"Register app"**

### **Langkah 3: Download google-services.json** DONE
1. Firebase akan generate file `google-services.json`
2. Klik **"Download google-services.json"**
3. **PENTING**: Pindahkan file ini ke folder `app/` di project Android Anda
   ```
   KiniBusApps/
   ├── app/
   │   ├── google-services.json  ← TARUH DISINI ✅
   │   └── build.gradle.kts
   ```

### **Langkah 3.1: Update Gradle Files untuk Firebase**

#### **A. Update `build.gradle.kts` (Project/Root level)**
Tambahkan google-services plugin:
```kotlin
// build.gradle.kts (di root project)
plugins {
    alias(libs.plugins.android.application) apply false
    // Tambahkan baris ini:
    id("com.google.gms.google-services") version "4.4.4" apply false
}
```

#### **B. Update `app/build.gradle.kts` (App level)**
Tambahkan plugins dan dependencies:
```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    // Tambahkan baris ini:
    id("com.google.gms.google-services")
}

dependencies {
    // Dependencies yang sudah ada...
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Tambahkan Firebase dependencies:
    // Firebase BoM (Bill of Materials) - versi terbaru
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Optional: Analytics
    implementation("com.google.firebase:firebase-analytics")
}
```

### **Langkah 4: Enable Authentication**
1. Di sidebar kiri, klik **"Authentication"**
2. Pergi ke tab **"Sign-in method"**
3. Enable **"Email/Password"**:
   - Klik pada provider "Email/Password"
   - Toggle **"Enable"**
   - Klik **"Save"**
4. **Opsional - Enable Google Sign-In**:
   - Klik "Google"
   - Toggle Enable
   - **Project public-facing name**: `KiniBus Apps` (nama app yang terlihat user)
   - **Project support email**: `alfimaulanaa@gmail.com` (email support untuk OAuth consent screen)
   - Klik "Save"
   - **PENTING**: Setelah enable Google Sign-In, Firebase akan membuat OAuth clients baru
   - **LANGKAH TAMBAHAN**: Download ulang `google-services.json` dan replace yang lama
   - **INSTRUKSI KHUSUS**: Setelah enable Google Sign-In, ikuti langkah berikut:

### **Langkah 4.1: Download Ulang google-services.json (SETELAH ENABLE GOOGLE SIGN-IN)**
1. **Setelah klik "Save"** pada Google Sign-In, Firebase akan menampilkan pesan:
   ```
   "Download latest configuration file
   Enabling Google sign-in for the first time creates new OAuth clients..."
   ```

2. **Klik "Continue"** atau **"Download latest configuration file"**

3. **Provide SHA-1 fingerprint**:
   - Di terminal, jalankan:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
   - **SHA-1 fingerprint untuk project ini**: `06:EE:A3:60:28:92:78:C8:5C:F2:12:5D:50:8A:A4:C7:1C:74:9A:0A`
   - **MENU FIREBASE CONSOLE**:
     1. Klik ikon ⚙️ **"Project settings"** (di sidebar kiri atas)
     2. Klik tab **"Your apps"**
     3. Cari app **"KiniBus Apps"** (com.kinibus.apps)
     4. Di bagian **"SHA certificate fingerprints"**, klik **"Add fingerprint"**
     5. Paste SHA-1: `06:EE:A3:60:28:92:78:C8:5C:F2:12:5D:50:8A:A4:C7:1C:74:9A:0A`
     6. Klik **"Save"**
   - ✅ **SUDAH DIDAPATKAN**: Copy dari output command di atas

4. **Download google-services.json yang baru**:
   - Klik "Download google-services.json"
   - File ini sekarang berisi konfigurasi OAuth untuk Google Sign-In

5. **Replace file lama**:
   - Pindahkan file baru ke `app/google-services.json` (replace yang lama)
   - Pastikan file terbaru sudah di folder yang benar

6. **Rebuild project**:
   ```bash
   ./gradlew clean build
   ```

   **CATATAN**: Jika muncul warning "build file has been changed and may need reload":
   - Di Android Studio: **File → Sync Project with Gradle Files**
   - Di VS Code: **Reload Window** atau **Developer: Reload Window**
   - Tunggu sampai sync selesai (ada progress bar di bawah)

**PENTING**: Tanpa SHA-1 fingerprint dan google-services.json terbaru, Google Sign-In tidak akan berfungsi!

### **Langkah 5: Setup Firestore Database**
1. Di sidebar kiri, klik **"Firestore Database"**
2. Klik **"Create database"**
3. **Pilih Edition Database**:
   - **Standard Edition** ✅ (REKOMENDASI untuk KiniBus)
     - Cocok untuk development dan small-medium apps
     - Free tier: 1GB storage, 50K reads/day
     - Fitur lengkap untuk mobile apps
   - **Enterprise Edition** (untuk production skala besar)
     - Untuk enterprise dengan traffic tinggi
     - Fitur advanced: Multi-region, SLA 99.999%
     - Biaya lebih tinggi
4. Pilih **"Start in test mode"** (untuk development)
   - **Catatan**: Untuk production, pilih "Start in production mode" dan setup security rules
5. Pilih lokasi database: **asia-southeast1 (Singapore)** - lebih dekat untuk Indonesia
6. Klik **"Done"**

**CATATAN PENTING**: Di Firestore, **TIDAK PERLU** membuat table/collection secara manual!
- Collection akan dibuat otomatis saat pertama kali menulis data
- Table schema ditentukan oleh kode aplikasi (model classes)
- Data awal bisa ditambahkan melalui kode atau Firebase Console

### **Langkah 6: Setup Security Rules (Production)**
1. Di Firestore Database, klik tab **"Rules"**
2. **UNTUK TESTING**: Gunakan rules test mode sederhana dulu:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       // Allow all operations for testing
       match /{document=**} {
         allow read, write: if true;
       }
     }
   }
   ```
   *Rules ini untuk development testing. Ganti dengan rules aman untuk production.*

3. **ATAU gunakan rules production** (jika sudah siap):
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       // Users dapat read/write data mereka sendiri
       match /users/{userId} {
         allow read, write: if request.auth != null && request.auth.uid == userId;
       }

       // Semua orang bisa read buses, hanya admin yang bisa write
       match /buses/{busId} {
         allow read: if true;
         allow write: if request.auth != null && request.auth.token.admin == true;
       }

       // Users dapat read/write booking mereka sendiri
       match /penyewaan/{penyewaanId} {
         allow read, write: if request.auth != null &&
           resource.data.userId == /databases/$(database)/documents/users/$(request.auth.uid);
       }

       // E-tickets read/write berdasarkan penyewaan
       match /eTickets/{ticketId} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```
4. **Pilih Mode Rules**:
   - **Development & Test**: Allow semua read/write (untuk development)
   - **Publish**: Gunakan custom rules yang telah dibuat (untuk production)
5. **Untuk testing**: Klik **"Publish"** ✅ dengan rules test mode

### **Langkah 7: Konfigurasi App Check (Security)**
1. Di sidebar kiri, klik **"App Check"**
2. Klik **"Get started"**
3. **Register App untuk App Check**:
   - Akan muncul pesan: *"Start protecting access to APIs by registering apps with App Check. Registration identifies incoming requests from your app."*
   - Klik tombol **"Register"** atau **"Add app"** untuk app **"KiniBus Apps"**
4. **Pilih Protection Method**:
   - **Play Integrity** ✅ (PILIH INI - recommended untuk Android)
     - Verifikasi integritas app menggunakan Google Play Services
     - Protection terbaik untuk production
   - Klik **"Register"**

5. **Isi SHA-256 Fingerprint** (REQUIRED untuk Play Integrity):
   - Firebase akan minta SHA-256 fingerprint setelah pilih Play Integrity
   - **SHA-256 fingerprint untuk project ini**:
     ```
     45:A1:BB:5C:87:32:24:8E:59:B7:47:F4:AB:12:1D:0F:E5:79:1A:EF:11:7B:E6:B9:BB:F5:33:99:7A:15:41:FD
     ```
   - Copy dari output `keytool` command di atas
   - Paste ke field SHA-256 di Firebase Console

5. **Konfigurasi Token Settings** (Opsional):
   - **Token time to live**: Default 1 jam (3600 detik)
     - Waktu token App Check valid
     - Recommended: 1 jam untuk balance security vs performance
   - **Advanced settings**: Default saja untuk development

6. **Konfirmasi Setup**:
   - App Check akan mengaktifkan protection untuk:
     - ✅ Cloud Firestore (pencegah spam reads/writes)
     - ✅ Firebase Authentication (pencegah abuse auth)
     - ✅ Firebase Realtime Database (jika ada)
   - Status akan berubah ke **"Enforced"** atau **"Unenforced"** (development mode)

7. **Catatan untuk Development**:
   - Di development: App Check dalam mode **"Unenforced"** (tidak block request)
   - Di production: Bisa enable **"Enforce"** untuk strict security

### **Langkah 8: Test Koneksi**
1. **Buka Project Settings**:
   - Di Firebase Console, klik ikon ⚙️ **"Project settings"** (roda gigi) di sidebar kiri **ATAS**
   - Atau klik project name di top bar → **"Project settings"**

2. **Tab "General"** - cek:
   - Project name: `KiniBusApps`
   - Package name: `com.kinibus.apps`
   - google-services.json status

3. **Tab "Your apps"** - pastikan:
   - Android app terdaftar dengan status **"Registered"** ✅
   - Package name: `com.kinibus.apps`
   - SHA-1 fingerprint sudah terdaftar

4. **Buka Firestore Database** untuk monitor data:
   - Klik **"Firestore Database"** di sidebar kiri
   - **Tab "Data"**: Kosong ✅ (normal - akan terisi saat app menulis data)
   - **Tab "Rules"**: Sudah ada custom security rules ✅
   - **Tab "Indexes"**: Default indexes (akan bertambah otomatis)

### **🎉 SETUP FIREBASE 100% COMPLETE & TESTED!**

**Semua komponen Firebase sudah ter-setup dan ter-test dengan SUKSES:**
- ✅ Project: `KiniBusApps`
- ✅ Authentication: Email/Password + Google Sign-In
- ✅ Firestore: Database connected & accessible
- ✅ App Check: Play Integrity protection aktif
- ✅ google-services.json: Latest version verified
- ✅ **FirebaseTestActivity**: All tests PASSED ✅

**STATUS AKHIR:**
```
🔄 Testing Firebase Connection...
✅ Firebase App: [DefaultFirebaseApp]
✅ Firestore: Connected successfully
ℹ️ Auth: No user logged in (normal for test)
📱 Package: com.kinibus.apps
🎉 Firebase Test Complete!
```

**LANGKAH SELANJUTNYA - READY UNTUK DEVELOPMENT:**
1. ✅ **Firebase Connection Confirmed** - Siap digunakan
2. 🔄 **Implementasi Fitur KiniBus** - Mulai coding!
3. 🔄 **Push ke GitHub** - Version control

## ⚠️ Troubleshooting Konfigurasi

### Jika App Tidak Connect:
- **Cek google-services.json**: Pastikan di folder `app/`, bukan `app/src/main/`
- **Package name**: Harus `com.kinibus.apps` (sudah sesuai dengan project)
- **Internet permission**: Sudah ada di AndroidManifest.xml
- **SHA-1 fingerprint**: Jika pakai Google Sign-In, perlu setup SHA-1

### SHA-1 Fingerprint (untuk Google Sign-In):
```bash
# Di terminal Android Studio atau command line
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Jika Authentication Gagal:
- **Email/Password enabled**: Cek di Authentication > Sign-in method
- **Network**: Pastikan device/emulator punya internet
- **google-services.json**: Valid dan up-to-date
- **Package name match**: Pastikan di Firebase Console sama dengan `com.kinibus.apps`

## ✅ Checklist Setup Firebase

- [ ] Firebase project `KiniBusApps` created
- [ ] Android app dengan package `com.kinibus.apps` ditambahkan
- [ ] google-services.json downloaded dan placed di `app/` folder
- [ ] Authentication: Email/Password enabled
- [ ] Firestore Database created (test mode)
- [ ] Security rules configured (jika production)
- [ ] Dependencies ditambahkan ke build.gradle.kts
- [ ] Test build berhasil

## 📊 Monitoring & Analytics

### Setup Analytics:
1. Di sidebar kiri, klik **"Analytics"**
2. Lihat dashboard untuk user engagement
3. Setup custom events untuk tracking fitur app

### Crashlytics (untuk error reporting):
1. Di sidebar kiri, klik **"Crashlytics"**
2. Ikuti setup wizard
3. Tambahkan dependency di build.gradle

## 🧪 Testing Firebase Setup

### Menggunakan FirebaseTestActivity
Setelah setup Firebase selesai, Anda bisa test koneksi dengan activity khusus:

1. **Jalankan FirebaseTestActivity**:
   ```java
   // Di MainActivity.java, tambahkan untuk test:
   startActivity(new Intent(this, FirebaseTestActivity.class));
   ```

2. **Atau update AndroidManifest.xml** untuk menjadikan test activity sebagai launcher:
   ```xml
   <activity
       android:name=".FirebaseTestActivity"
       android:exported="true">
       <intent-filter>
           <action android:name="android.intent.action.MAIN" />
           <category android:name="android.intent.category.LAUNCHER" />
       </intent-filter>
   </activity>
   ```

3. **Apa yang di-test**:
   - ✅ Firebase App initialization
   - ✅ Firestore connection
   - ✅ Authentication status
   - 📱 Package name verification

### Build & Test
```bash
./gradlew assembleDebug
# Jika berhasil, install ke device/emulator
```

**Expected Result**: Semua status ✅ hijau berarti Firebase setup berhasil!

## 📞 Bantuan & Troubleshooting

Jika masih ada masalah:
- Pastikan `google-services.json` di folder `app/`
- Cek package name di Firebase Console: `com.kinibus.apps`
- Pastikan internet permission ada
- Lihat logcat untuk error details

**Butuh bantuan implementasi code?** Toggle ke Act Mode untuk melanjutkan development fitur Firebase.

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

### **Langkah 2: Tambahkan Android App**
1. Di halaman project overview, klik ikon **Android** (hijau)
2. **Android package name**: `com.kinibus.apps`
   - ✅ **SUDAH SESUAI** dengan `namespace` di `app/build.gradle.kts`
   - ⚠️ **PASTIKAN BENAR**: Jangan gunakan `com.example.myapplication`
3. **App nickname**: `KiniBus Apps`
4. **Debug signing certificate SHA-1** (opsional untuk sekarang):
   - Bisa diisi nanti jika perlu Google Sign-In
5. Klik **"Register app"**

### **Langkah 3: Download google-services.json**
1. Firebase akan generate file `google-services.json`
2. Klik **"Download google-services.json"**
3. **PENTING**: Pindahkan file ini ke folder `app/` di project Android Anda
   ```
   KiniBusApps/
   ├── app/
   │   ├── google-services.json  ← TARUH DISINI
   │   └── build.gradle.kts
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
   - Masukkan project support email
   - Klik "Save"

### **Langkah 5: Setup Firestore Database**
1. Di sidebar kiri, klik **"Firestore Database"**
2. Klik **"Create database"**
3. Pilih **"Start in test mode"** (untuk development)
   - **Catatan**: Untuk production, pilih "Start in production mode" dan setup security rules
4. Pilih lokasi database: **asia-southeast1 (Singapore)** - lebih dekat untuk Indonesia
5. Klik **"Done"**

### **Langkah 6: Setup Security Rules (Production)**
1. Di Firestore Database, klik tab **"Rules"**
2. Ganti rules default dengan:
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
3. Klik **"Publish"**

### **Langkah 7: Konfigurasi App Check (Security)**
1. Di sidebar kiri, klik **"App Check"**
2. Klik **"Get started"**
3. Pilih **"Play Integrity"** untuk Android
4. Ikuti instruksi untuk setup

### **Langkah 8: Test Koneksi**
1. Di Firebase Console, bisa lihat **"Project settings"**
2. Tab **"General"** - cek package name dan google-services.json
3. Tab **"Your apps"** - pastikan Android app terdaftar
4. Buka **"Realtime Database"** atau **"Firestore"** untuk monitor data

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

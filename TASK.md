Berdasarkan instruksi di `FIREBASE_INTEGRATION.md`, berikut adalah panduan detail cara konfigurasi di Firebase Console untuk aplikasi KiniBus:

## 🔧 Konfigurasi Firebase Console Step-by-Step

### **Langkah 1: Buat Project Firebase**
1. Buka [Firebase Console](https://console.firebase.google.com/)
2. Klik **"Create a project"** atau **"Add project"**
3. Masukkan nama project: `KiniBus-Android`
4. **PENTING**: Pastikan project ID unik (akan generate otomatis)
5. Enable Google Analytics: **Ya** (untuk tracking penggunaan)
6. Pilih Google Analytics account (atau buat baru)
7. Klik **"Create project"** - tunggu beberapa detik

### **Langkah 2: Tambahkan Android App**
1. Di halaman project overview, klik ikon **Android** (hijau)
2. **Android package name**: `com.example.myapplication`
   - Ini harus sama dengan `namespace` di `app/build.gradle.kts`
3. **App nickname**: `KiniBus App`
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
- **Package name**: Harus sama persis dengan di AndroidManifest.xml
- **Internet permission**: Pastikan ada di AndroidManifest.xml
- **SHA-1 fingerprint**: Jika pakai Google Sign-In, perlu setup SHA-1

### SHA-1 Fingerprint (untuk Google Sign-In):
```bash
# Di terminal Android Studio
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### Jika Authentication Gagal:
- **Email/Password enabled**: Cek di Authentication > Sign-in method
- **Network**: Pastikan device/emulator punya internet
- **google-services.json**: Valid dan up-to-date

## 📊 Monitoring & Analytics

### Setup Analytics:
1. Di sidebar kiri, klik **"Analytics"**
2. Lihat dashboard untuk user engagement
3. Setup custom events untuk tracking fitur app

### Crashlytics (untuk error reporting):
1. Di sidebar kiri, klik **"Crashlytics"**
2. Ikuti setup wizard
3. Tambahkan dependency di build.gradle

Apakah Anda ingin saya buatkan script atau code untuk test koneksi Firebase setelah setup, atau ada langkah spesifik yang bingung? Jika perlu implementasi code, kita bisa toggle ke Act Mode.
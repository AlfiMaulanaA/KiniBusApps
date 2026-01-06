# KiniBus - Aplikasi Sewa Bus Berbasis Android

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)

**KiniBus** adalah aplikasi mobile Android Native untuk layanan penyewaan bus yang dirancang khusus untuk kemudahan pengguna dalam mencari, memesan, dan mengelola penyewaan bus. Aplikasi ini dikembangkan menggunakan bahasa pemrograman Java dengan framework Android Studio, memanfaatkan SQLite sebagai database lokal yang disimulasikan sebagai sistem berbasis cloud untuk memberikan pengalaman pengguna yang mirip dengan aplikasi online nyata.

## 📋 Daftar Isi
- [Fitur Utama](#-fitur-utama)
- [Teknologi Digunakan](#-teknologi-digunakan)
- [Struktur Proyek](#-struktur-proyek)
- [Instalasi & Setup](#-instalasi--setup)
- [Penggunaan](#-penggunaan)
- [Database Schema](#-database-schema)
- [Screenshot](#-screenshot)
- [Kontribusi](#-kontribusi)
- [Lisensi](#-lisensi)

## 🚀 Fitur Utama

### 🔐 Autentikasi & Akun
- **Login & Registrasi**: Sistem autentikasi dengan validasi email dan password
- **Lupa Password**: Reset password simulasi melalui email
- **Manajemen Profil**: Update informasi pengguna

### 🚌 Pencarian & Pemesanan Bus
- **Pencarian Bus**: Filter berdasarkan jenis bus, kapasitas, tanggal, dan lokasi
- **Detail Bus**: Informasi lengkap spesifikasi, harga, dan ketersediaan
- **Konfirmasi Penyewaan**: Form pemesanan dengan kalkulasi biaya otomatis
- **Simulasi Pembayaran**: Proses pembayaran dummy dengan berbagai metode

### 🎫 E-Tiket Digital
- **Generate E-Tiket**: Tiket digital dengan QR code untuk verifikasi
- **Download PDF**: E-tiket dapat diunduh sebagai file PDF
- **Riwayat Penyewaan**: Tracking semua pemesanan dan status

### 📊 Dashboard & Manajemen
- **Dashboard Utama**: Tampilan bus populer dan menu navigasi
- **Riwayat Transaksi**: Daftar lengkap penyewaan sebelumnya
- **Notifikasi Simulasi**: Update status penyewaan secara real-time

## 🛠 Teknologi Digunakan

- **Bahasa Pemrograman**: Java 11
- **Framework**: Android Native (Android Studio)
- **Database**: SQLite (lokal dengan simulasi online)
- **UI/UX**: Material Design 3, RecyclerView, Fragments
- **Build Tool**: Gradle (Kotlin DSL)
- **Versi Android**: Minimum SDK 24 (Android 7.0), Target SDK 36
- **Library Tambahan**:
  - ZXing (untuk QR code generation)
  - Android PdfDocument (untuk PDF e-tiket)

## 📁 Struktur Proyek

```
KiniBusApps/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/myapplication/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── SplashActivity.java
│   │   │   │   ├── LoginActivity.java
│   │   │   │   ├── DashboardActivity.java
│   │   │   │   └── ...
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── values/
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/
├── image/ (assets gambar bus)
├── BRIEF.md (dokumentasi detail proyek)
├── TASK.md (daftar tugas development)
├── ERROR.md (log error)
└── README.md
```

## 📦 Instalasi & Setup

### Prasyarat
- **Android Studio**: Versi Arctic Fox atau lebih baru
- **JDK**: Java Development Kit 11
- **Android SDK**: API level 24+
- **Git**: Untuk cloning repository

### Langkah Instalasi

1. **Clone Repository**
   ```bash
   git clone https://github.com/AlfiMaulanaA/KiniBusApps.git
   cd KiniBusApps
   ```

2. **Buka di Android Studio**
   - Launch Android Studio
   - Pilih "Open an existing Android Studio project"
   - Navigasi ke folder `KiniBusApps` dan pilih `build.gradle.kts`

3. **Sync Project**
   - Android Studio akan otomatis sync dependencies
   - Pastikan semua library terinstall dengan benar

4. **Build & Run**
   - Connect device Android atau setup emulator
   - Klik tombol "Run" (Shift + F10)
   - Aplikasi akan terinstall dan berjalan di device

### Konfigurasi Database
Aplikasi menggunakan SQLite lokal yang otomatis dibuat saat pertama kali dijalankan. Tidak perlu setup database manual karena disimulasikan sebagai sistem online.

## 🎯 Penggunaan

1. **Splash Screen**: Aplikasi akan menampilkan logo selama 2-3 detik
2. **Login/Registrasi**: Masuk dengan akun atau daftar baru
3. **Dashboard**: Jelajahi menu utama aplikasi
4. **Cari Bus**: Gunakan filter untuk mencari bus yang diinginkan
5. **Pemesanan**: Pilih bus, isi detail, dan lakukan pembayaran simulasi
6. **E-Tiket**: Dapatkan tiket digital dengan QR code

## 🗄 Database Schema

Aplikasi menggunakan SQLite dengan struktur tabel berikut:

### Tabel User
```sql
CREATE TABLE User (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    nama TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### Tabel Bus
```sql
CREATE TABLE Bus (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama TEXT NOT NULL,
    jenis TEXT,
    kapasitas INTEGER,
    harga_per_hari REAL,
    gambar TEXT,
    last_sync DATETIME
);
```

### Tabel Penyewaan
```sql
CREATE TABLE Penyewaan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    bus_id INTEGER,
    tanggal_sewa DATE,
    durasi INTEGER,
    total_biaya REAL,
    status TEXT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (bus_id) REFERENCES Bus(id)
);
```

### Tabel E_Tiket
```sql
CREATE TABLE E_Tiket (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    penyewaan_id INTEGER,
    qr_code TEXT,
    pdf_path TEXT,
    FOREIGN KEY (penyewaan_id) REFERENCES Penyewaan(id)
);
```

## 📸 Screenshot

*(Screenshot akan ditambahkan setelah implementasi UI selesai)*

- Splash Screen
- Login Page
- Dashboard
- Pencarian Bus
- Detail Bus
- Konfirmasi Pembayaran
- E-Tiket Digital

## 🤝 Kontribusi

Kontribusi untuk pengembangan KiniBus sangat diterima! Silakan ikuti langkah berikut:

1. Fork repository ini
2. Buat branch fitur baru (`git checkout -b feature/AmazingFeature`)
3. Commit perubahan (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

### Panduan Kontribusi
- Ikuti standar coding Java Android
- Pastikan semua fitur ditest pada device dan emulator
- Update dokumentasi jika diperlukan
- Gunakan commit message yang deskriptif

## 📄 Lisensi

Proyek ini menggunakan lisensi MIT - lihat file [LICENSE](LICENSE) untuk detail lebih lanjut.

## 📞 Kontak

**Pengembang**: Alfi Maulana A

**Email**: [alfimaulanaa@gmail.com](mailto:alfimaulanaa@gmail.com)

**LinkedIn**: [Alfi Maulana A](https://linkedin.com/in/alfimaulanaa)

**GitHub**: [@AlfiMaulanaA](https://github.com/AlfiMaulanaA)

---

⭐ **Star** repository ini jika Anda menemukannya berguna!

*Proyek ini dikembangkan sebagai bagian dari pembelajaran pengembangan aplikasi mobile Android.*
# 🌱 KiniBusApps Database Seeding Guide

Script untuk mengisi data awal (seeding) ke Firebase Firestore untuk aplikasi KiniBusApps.

## 📋 Prerequisites

### 1. Node.js Installation
Pastikan Node.js versi 14 atau lebih baru terinstall:
```bash
node --version  # Should show v14.0.0 or higher
npm --version   # Should show 6.0.0 or higher
```

### 2. Firebase Service Account Key
Download `serviceAccountKey.json` dari Firebase Console:

1. **Buka Firebase Console** → Pilih project KiniBusApps
2. **Klik ikon ⚙️ (Settings)** → **Project Settings**
3. **Tab "Service Accounts"**
4. **Klik "Generate new private key"**
5. **Simpan file** sebagai `serviceAccountKey.json` di folder yang sama dengan `seed_database.js`

### 3. Firebase Project Configuration
Update `databaseURL` di `seed_database.js`:
```javascript
admin.initializeApp({
  credential: admin.credential.cert('./serviceAccountKey.json'),
  databaseURL: 'https://YOUR_PROJECT_ID.firebaseio.com' // Ganti dengan Project ID Anda
});
```

## 🚀 Installation & Setup

### 1. Install Dependencies
```bash
npm install
```

### 2. Setup Service Account Key
Pastikan `serviceAccountKey.json` ada di folder yang sama dengan script.

### 3. Configure Project ID
Edit `seed_database.js` dan ganti `YOUR_PROJECT_ID` dengan Project ID Firebase Anda:
```javascript
databaseURL: 'https://kinibusapps-12345.firebaseio.com'
```

## 📊 Data Overview

Script ini akan mengisi data berikut:

### 🚌 Bus Routes (21 Routes)
- **Jakarta → Bandung**: 4 routes (Executive, Business, Economic, Night)
- **Jakarta → Bogor**: 3 routes
- **Jakarta → Cirebon**: 3 routes
- **Bandung → Yogyakarta**: 3 routes
- **Surabaya → Malang**: 3 routes
- **Additional Routes**: Jakarta→Semarang, Bandung→Surabaya, Yogya→Bali

### 👥 Sample Users (2 Users)
- Admin user untuk testing
- Regular user untuk testing

### 🎫 Bus Classes & Pricing
- **Executive**: Rp 85,000 - Rp 280,000 (Premium amenities)
- **Business**: Rp 70,000 - Rp 220,000 (Business comfort)
- **Economic**: Rp 55,000 - Rp 160,000 (Budget friendly)

## 🎯 Usage

### Basic Seeding
```bash
npm run seed
# atau
node seed_database.js
```

### Clear Existing Data (Optional)
**⚠️ WARNING: This will delete ALL existing data!**
```bash
npm run clear
```

### Expected Output
```
🚀 Starting database seeding...
📋 Seeding bus data...
✅ Successfully seeded 21 bus records
👤 Seeding user data...
✅ Successfully seeded 2 user records
🎉 Database seeding completed successfully!

📊 Summary:
   • 21 bus routes across 7 routes
   • 8 Executive buses
   • 7 Business buses
   • 6 Economic buses
   • 2 sample users

🎯 Next steps:
1. Run your Android app
2. Test the bus listing in Dashboard
3. Try filtering by class and sorting
4. Test booking functionality
```

## 🔍 Data Structure

### Bus Document Structure
```json
{
  "nama": "KiniShuttle Executive",
  "jenis": "EXECUTIVE",
  "kapasitas": 50,
  "harga": 150000,
  "keberangkatan": "Kp. Rambutan",
  "tujuan": "Lw. Panjang",
  "waktuKeberangkatan": "Timestamp",
  "waktuTiba": "Timestamp",
  "durasi": "3h 30m",
  "kursiTersedia": 45,
  "tersedia": true,
  "fasilitas": ["AC", "WiFi", "USB Charging"],
  "deskripsi": "Luxury executive bus...",
  "createdAt": "Timestamp",
  "updatedAt": "Timestamp"
}
```

### User Document Structure
```json
{
  "email": "user@kinibus.com",
  "nama": "Test User",
  "nomorTelepon": "+6281234567891",
  "profileImageUrl": "",
  "isActive": true,
  "createdAt": "Timestamp",
  "updatedAt": "Timestamp"
}
```

## 🧪 Testing the Seeded Data

### 1. Default App Route
Aplikasi akan menampilkan route **Kp. Rambutan → Lw. Panjang** pada tanggal **13 Jan 2026**

### 2. Available Buses
- KiniShuttle Executive (06:00)
- Bandung Premium Express (07:15)
- Economy Rider Plus (08:30)
- Bandung Night Express (22:00)

### 3. Test Filtering
- **Class Filter**: Executive, Business, Economic
- **Sort Options**: Cheapest, Earliest

### 4. Test Booking Flow
1. Pilih bus → Detail screen
2. Klik "Book" → Seat reservation
3. Check Firestore untuk data booking

## 🔧 Troubleshooting

### Error: "FirebaseApp name [DEFAULT] already exists"
- Pastikan hanya ada 1 instance Firebase app
- Restart script atau clear cache

### Error: "Invalid credentials"
- Periksa `serviceAccountKey.json` path dan content
- Pastikan file tidak corrupted

### Error: "Permission denied"
- Periksa Firestore Security Rules
- Pastikan service account punya akses write

### Error: "Project not found"
- Periksa `databaseURL` di script
- Pastikan Project ID benar

## 📝 Customization

### Add More Routes
Edit array `busSeedData` di `seed_database.js`:

```javascript
{
  nama: "Your Bus Name",
  jenis: "EXECUTIVE", // or BUSINESS/ECONOMIC
  kapasitas: 50,
  harga: 150000,
  keberangkatan: "Origin City",
  tujuan: "Destination City",
  waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:00:00Z")),
  waktuTiba: timestamp.fromDate(new Date("2026-01-13T11:30:00Z")),
  durasi: "3h 30m",
  kursiTersedia: 45,
  tersedia: true,
  fasilitas: ["AC", "WiFi"],
  deskripsi: "Your description"
}
```

### Modify Pricing
Sesuaikan `harga` field sesuai kebutuhan bisnis.

### Add More Users
Tambahkan ke array `userSeedData`.

## 🎉 Success Checklist

Setelah running script:
- ✅ Firebase Console menampilkan 21 bus documents
- ✅ Firebase Console menampilkan 2 user documents
- ✅ Android app menampilkan daftar bus
- ✅ Filter dan sort berfungsi
- ✅ Booking flow bekerja
- ✅ Real-time updates aktif

## 📞 Support

Jika ada masalah:
1. Check Firebase Console logs
2. Verify service account permissions
3. Ensure correct project configuration
4. Test with smaller dataset first

**Happy seeding! 🚀**

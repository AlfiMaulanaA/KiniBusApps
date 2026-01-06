# KiniBus - Aplikasi Sewa Bus Berbasis Android

## Ringkasan Proyek

**KiniBus** adalah aplikasi mobile Android Native yang dirancang untuk menyediakan layanan penyewaan bus secara mudah dan efisien. Aplikasi ini dikembangkan menggunakan bahasa pemrograman Java dengan framework Android Studio, bukan menggunakan framework cross-platform seperti Flutter (Dart), React Native, atau Ionic. Pendekatan Android Native memastikan performa optimal, akses penuh ke fitur perangkat Android, dan kesesuaian dengan praktikum pengembangan aplikasi mobile.

Aplikasi ini memfasilitasi pengguna dalam mencari, memesan, dan mengelola penyewaan bus untuk berbagai keperluan seperti perjalanan wisata, transportasi kelompok, atau kebutuhan bisnis. Karena ini adalah proyek simulasi, penyimpanan data menggunakan SQLite lokal yang disimulasikan sebagai sistem online melalui antarmuka yang menyerupai koneksi server, sehingga memberikan pengalaman pengguna yang mirip dengan aplikasi berbasis cloud tanpa memerlukan infrastruktur backend nyata.

Proyek ini memanfaatkan SQLite sebagai database lokal untuk menyimpan data pengguna, daftar bus, jadwal penyewaan, dan riwayat transaksi. Aplikasi ini mencakup fitur-fitur utama seperti autentikasi pengguna, pencarian dan pemilihan bus, simulasi pembayaran, serta manajemen e-tiket digital.

---

## 1. Analisa Konsep & Permasalahan

### 1.1 Permasalahan yang Diangkat

Permasalahan utama yang diatasi oleh aplikasi KiniBus adalah:

- **Keterbatasan Akses Penyewaan Bus**: Banyak penyedia layanan penyewaan bus masih menggunakan sistem manual atau platform terpisah, sehingga sulit bagi pengguna untuk membandingkan opsi dan melakukan pemesanan secara real-time.
- **Kurangnya Integrasi Data**: Informasi tentang ketersediaan bus, harga sewa, dan jadwal seringkali tersebar di berbagai sumber, menyebabkan inefisiensi dalam proses penyewaan.
- **Kesulitan dalam Manajemen Penyewaan**: Pengguna kesulitan melacak riwayat penyewaan, mengelola pembayaran, dan mendapatkan konfirmasi digital yang dapat diandalkan.
- **Kebutuhan akan Solusi Digital**: Di era digital saat ini, diperlukan aplikasi yang dapat memberikan pengalaman penyewaan bus yang seamless, aman, dan user-friendly.

**Analisa Kritis**:
- Permasalahan ini relevan dengan tren digitalisasi layanan transportasi di Indonesia, di mana penyewaan bus masih banyak dilakukan secara konvensional.
- Proyek ini cocok sebagai studi kasus dalam pengembangan sistem informasi berbasis mobile.
- Solusi yang ditawarkan dapat diukur melalui peningkatan efisiensi dan kepuasan pengguna.

**Nilai Akademik**: ⭐⭐⭐⭐⭐
Konsep masalah jelas, spesifik, dan solusinya dapat dievaluasi secara empiris.

---

## 2. Analisa Tujuan Pengembangan

### 2.1 Kesesuaian Tujuan dengan Latar Belakang

Tujuan pengembangan aplikasi KiniBus meliputi:

- **Menyediakan Platform Terintegrasi untuk Penyewaan Bus**: Mengintegrasikan data dari berbagai penyedia bus dalam satu aplikasi untuk memudahkan pencarian dan pemesanan.
- **Implementasi Sistem Pembayaran dan E-Tiket Digital**: Memungkinkan pengguna melakukan pembayaran simulasi dan mendapatkan tiket digital yang dapat diverifikasi.
- **Pengembangan Keterampilan Teknis**: Sebagai media pembelajaran untuk menguasai bahasa Java, database SQLite, dan desain UI/UX Android.
- **Simulasi Sistem Online**: Menggunakan SQLite lokal yang disimulasikan sebagai sistem berbasis cloud untuk memberikan pengalaman pengguna yang mirip dengan aplikasi online nyata.

**Catatan Akademik Penting**:
Tujuan pembelajaran teknis yang eksplisit memberikan nilai tambah dalam penilaian proyek akhir.

---

## 3. Analisa Ruang Lingkup Aplikasi

### 3.1 Kekuatan Ruang Lingkup

- Ruang lingkup aplikasi tidak terlalu luas, fokus pada fitur-fitur inti penyewaan bus.
- Sesuai untuk pengembangan tim kecil atau individu.
- Realistis untuk tingkat mahasiswa dengan pengetahuan dasar Android development.

### 3.2 Catatan Teknis

- Aplikasi menggunakan SQLite sebagai database lokal, namun disimulasikan sebagai sistem online melalui:
  - Antarmuka yang menampilkan indikator "sinkronisasi" atau "loading" untuk menyerupai koneksi server.
  - Penyimpanan data secara lokal yang diperlakukan sebagai cache dari "server online".
  - Simulasi delay waktu untuk operasi database agar terlihat seperti request API.

**Implikasi**:
Aplikasi ini adalah simulasi semi-online, ideal untuk proyek pembelajaran tanpa memerlukan server backend.

---

## 4. Analisa Alur Proses Aplikasi

### 4.1 Alur Proses Utama

Alur proses aplikasi KiniBus dirancang sebagai berikut:

```
Splash Screen → Login/Registrasi → Dashboard → Cari Bus → Pilih Bus → Konfirmasi Penyewaan → Simulasi Pembayaran → E-Tiket → Riwayat Penyewaan
```

Ini adalah alur standar untuk aplikasi penyewaan transportasi, memastikan kemudahan penggunaan dan logika yang linier.

### 4.2 Analisa UX

- Alur minim langkah yang tidak perlu, fokus pada efisiensi.
- Desain user-friendly dengan navigasi intuitif.
- Menggunakan fragment dan activity untuk transisi yang smooth.

---

## 5. Analisa Fitur Utama (Per Fitur)

### 5.1 Splash Screen

**Deskripsi Detail**:
Fitur Splash Screen adalah layar pembuka aplikasi yang ditampilkan selama 2-3 detik saat aplikasi pertama kali diluncurkan. Layar ini menampilkan logo KiniBus dan animasi loading sederhana untuk memberikan kesan profesional.

**Fungsi Teknis**:
- Menggunakan `Handler` untuk delay waktu.
- Transisi otomatis ke LoginActivity setelah timeout.
- Mencegah akses langsung ke halaman utama tanpa autentikasi.

**Implementasi**:
- Layout XML dengan ImageView untuk logo dan ProgressBar untuk loading.
- Kode Java menggunakan Thread atau Handler untuk simulasi loading.

**Nilai Akademik**: ⭐⭐⭐
Fitur sederhana namun penting untuk branding dan UX awal.

### 5.2 Login & Registrasi

**Deskripsi Detail**:
Fitur autentikasi pengguna yang memungkinkan login dengan email dan password, serta registrasi akun baru. Data pengguna disimpan di tabel SQLite lokal, disimulasikan sebagai sinkronisasi dengan server online.

**Fungsi Teknis**:
- Validasi input email dan password.
- Query SQLite untuk verifikasi kredensial.
- Simulasi koneksi online dengan dialog "Menyambungkan ke server...".
- Penyimpanan password dalam bentuk hash sederhana untuk keamanan dasar.

**Implementasi**:
- Activity dengan EditText untuk input, Button untuk submit.
- Database helper class untuk operasi CRUD pada tabel User.
- Intent untuk navigasi ke Dashboard setelah login berhasil.

**Kelebihan**: Mudah diimplementasikan, cocok untuk pembelajaran autentikasi lokal.
**Catatan Kritis**: Untuk simulasi online, tambahkan indikator koneksi yang selalu "berhasil" setelah delay.

**Nilai Akademik**: ⭐⭐⭐⭐
Dasar untuk sistem keamanan aplikasi.

### 5.3 Dashboard

**Deskripsi Detail**:
Halaman utama setelah login, menampilkan menu navigasi ke fitur-fitur utama seperti pencarian bus, riwayat penyewaan, dan profil pengguna. Menggunakan RecyclerView untuk menampilkan daftar bus populer atau terbaru.

**Fungsi Teknis**:
- Navigasi menggunakan BottomNavigationView atau DrawerLayout.
- Query SQLite untuk mengambil data bus terbaru.
- Simulasi update real-time dengan refresh button yang menjalankan query ulang.

**Implementasi**:
- Fragment untuk modularitas.
- Adapter untuk RecyclerView dengan data dari database.
- OnClickListener untuk navigasi ke activity lain.

**Kelebihan**: Pusat kontrol aplikasi, mudah dikembangkan untuk fitur tambahan.

**Nilai Akademik**: ⭐⭐⭐⭐⭐
Fitur kompleks yang melatih manajemen UI dan data.

### 5.4 Pencarian dan Pemilihan Bus

**Deskripsi Detail**:
Fitur untuk mencari bus berdasarkan kriteria seperti jenis bus, tanggal penyewaan, lokasi, dan kapasitas. Menampilkan daftar bus dengan detail harga, spesifikasi, dan gambar. Pengguna dapat memilih bus dan melanjutkan ke konfirmasi penyewaan.

**Fungsi Teknis**:
- Filter menggunakan Spinner dan EditText.
- Query SQLite dengan WHERE clause untuk pencarian.
- Sorting berdasarkan harga atau popularitas.
- Simulasi loading data dari "server" dengan ProgressDialog.

**Implementasi**:
- Activity dengan search form.
- RecyclerView untuk hasil pencarian.
- Intent dengan extra data untuk passing informasi bus ke activity berikutnya.

**Potensi Tantangan**: Optimasi query untuk performa.
**Nilai Akademik**: ⭐⭐⭐⭐⭐
Melatih keterampilan SQL dan UI dinamis.

### 5.5 Konfirmasi Penyewaan dan Detail Bus

**Deskripsi Detail**:
Halaman detail bus yang dipilih, menampilkan spesifikasi lengkap, syarat sewa, dan form konfirmasi penyewaan. Pengguna dapat memasukkan detail tambahan seperti tanggal sewa dan tujuan.

**Fungsi Teknis**:
- Parsing data dari Intent.
- Validasi input tanggal menggunakan DatePicker.
- Simulasi kalkulasi biaya sewa berdasarkan durasi.

**Implementasi**:
- Layout dengan TextView, ImageView, dan EditText.
- Button untuk lanjut ke pembayaran.

**Kelebihan**: Memastikan transparansi informasi sebelum transaksi.

### 5.6 Simulasi Pembayaran

**Deskripsi Detail**:
Fitur simulasi pembayaran yang menampilkan ringkasan biaya, metode pembayaran dummy (seperti transfer bank atau e-wallet), dan konfirmasi pembayaran. Tidak ada integrasi payment gateway nyata, hanya simulasi untuk alur lengkap.

**Fungsi Teknis**:
- Kalkulasi total biaya dengan logika sederhana.
- Simulasi proses pembayaran dengan delay dan dialog sukses.
- Penyimpanan data transaksi ke SQLite sebagai "riwayat".

**Implementasi**:
- Activity dengan summary layout.
- AlertDialog untuk konfirmasi.
- Update database setelah "pembayaran berhasil".

**Catatan Akademik**: Fokus pada alur bisnis, bukan integrasi teknis.
**Nilai Akademik**: ⭐⭐⭐⭐
Cocok untuk proyek simulasi.

### 5.7 E-Tiket Digital

**Deskripsi Detail**:
Fitur untuk menghasilkan tiket digital setelah pembayaran berhasil, berisi detail penyewaan, QR code untuk verifikasi, dan opsi download sebagai PDF. QR code dapat discan untuk konfirmasi di lokasi.

**Fungsi Teknis**:
- Generate QR code menggunakan library ZXing.
- Pembuatan PDF menggunakan Android PdfDocument API.
- Penyimpanan tiket sebagai file lokal atau database blob.

**Implementasi**:
- Activity dengan detail tiket dan QR ImageView.
- Button untuk download PDF ke storage eksternal.

**Kelebihan**: Menambah nilai praktis aplikasi.
**Nilai Akademik**: ⭐⭐⭐⭐⭐
Fitur advance untuk proyek mahasiswa.

### 5.8 Riwayat Penyewaan

**Deskripsi Detail**:
Halaman untuk melihat daftar penyewaan sebelumnya, dengan filter berdasarkan status (aktif, selesai, dibatalkan). Menampilkan detail setiap penyewaan dan opsi untuk melihat e-tiket ulang.

**Fungsi Teknis**:
- Query SQLite untuk mengambil data pemesanan berdasarkan user ID.
- RecyclerView dengan adapter untuk list riwayat.
- Simulasi status update secara lokal.

**Implementasi**:
- Fragment atau Activity dengan list view.
- OnClick untuk detail penyewaan.

**Kelebihan**: Membantu pengguna melacak aktivitas.
**Nilai Akademik**: ⭐⭐⭐⭐
Melatih query database kompleks.

### 5.9 Lupa Password

**Deskripsi Detail**:
Fitur untuk reset password melalui email simulasi. Pengguna memasukkan email, dan aplikasi menampilkan dialog sukses tanpa mengirim email nyata.

**Fungsi Teknis**:
- Validasi email format.
- Simulasi proses reset dengan delay.
- Update password di database jika diperlukan.

**Implementasi**:
- Activity sederhana dengan EditText dan Button.

**Catatan**: Simulasi penuh untuk keamanan.

---

## 6. Analisa Model Database

### 6.1 Struktur Database SQLite

Database menggunakan tabel-tabel berikut:

- **User**: Menyimpan data pengguna (id, email, password, nama, dll.)
- **Bus**: Daftar bus (id, nama, jenis, kapasitas, harga_per_hari, gambar)
- **Penyewaan**: Riwayat penyewaan (id, user_id, bus_id, tanggal_sewa, durasi, total_biaya, status)
- **E_Tiket**: Detail tiket (id, penyewaan_id, qr_code, pdf_path)

**Kelebihan**: Normalisasi baik, relasi jelas.

### 6.2 Simulasi Online

- Semua operasi database diperlakukan sebagai "sinkronisasi online".
- Tambahkan kolom `last_sync` untuk simulasi timestamp update.
- Indikator loading untuk setiap query agar terlihat seperti API call.

---

## 7. Analisa Pembagian Tugas Tim

Jika dikerjakan tim, tugas dapat dibagi:

- UI/UX Design: Desain layout dan navigasi.
- Backend Development: Implementasi database dan logika bisnis.
- Testing: Validasi fitur dan bug fixing.

---

## 8. Kelebihan Proyek

- Realistis dan sesuai kurikulum.
- Fokus pada pembelajaran teknis Android.
- Dapat dikembangkan menjadi aplikasi nyata dengan backend.

---

## 9. Kekurangan

- Tidak ada backend server nyata.
- Simulasi payment tidak akurat.
- Keterbatasan skalabilitas SQLite.

---

## 10. Rekomendasi Pengembangan

- Tambahkan hashing password yang lebih aman.
- Implementasi push notification untuk reminder.
- Integrasi dengan Firebase untuk versi online nyata.

---

## 11. Kesimpulan

KiniBus adalah proyek yang kuat secara konsep dan teknis, ideal untuk pembelajaran Android development dengan simulasi sistem online menggunakan SQLite lokal.

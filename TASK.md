Berikut saya buatkan **PROMPT LENGKAP, RAPIH, dan TERSTRUKTUR** yang bisa kamu gunakan sebagai **permintaan resmi pembuatan aplikasi UTS RentCar-Apps**.
Prompt ini sudah mencakup seluruh ketentuan dosen: Splash Screen, Login, Lupa Password, Dashboard dengan Fragment, UI Components lengkap, Dummy Data, dan Tema aplikasi.

---

# ✅ **PROMPT TERBAIK UNTUK MEMBUAT APLIKASI RENTCAR-APPS (UTS)**

**Buatkan sebuah aplikasi Android Mobile menggunakan Android Studio (Java) dengan detail ketentuan sebagai berikut:**

---

## **🟦 1. Aplikasi bertema “RentCar-Apps” (Aplikasi Rental Mobil)**

Tema tampilan harus modern dan konsisten, mencerminkan aplikasi rental mobil (warna biru/kuning/abu-abu, ikon mobil, dll).

---

## **🟦 2. Activity Splash Screen (Sebagai Launcher)**

* Tampilkan **logo aplikasi** berada di **tengah layar**.
* Menggunakan tema fullscreen.
* Setelah 2–3 detik → berpindah otomatis ke **Activity Login** (explicit intent).

---

## **🟦 3. Activity Login**

Wajib ada:

* TextView → “Silakan Login untuk masuk ke RentCar-Apps”
* Gambar/logo aplikasi
* Input **Nama / Username**
* Input **Kata Sandi / Password**
* Tombol **Login**
* Link **“Lupa Kata Sandi?”**

  * Link ini merupakan **Explicit Intent** menuju *Activity Lupa Password*.

Event tombol Login:

* Jika nama & password tidak kosong → masuk ke **Activity Dashboard (dalam Fragment)**.
* Dummy login (tanpa database).

---

## **🟦 4. Activity Lupa Kata Sandi**

Wajib berisi:

* Input Email
* Tombol **Tautkan**
* Link ke website kampus
  → gunakan **Implicit Intent** menuju situs:
  **[https://itts.ac.id](https://itts.ac.id)**

---

## **🟦 5. Activity Dashboard (Menggunakan Fragment)**

Activity ini menampilkan halaman Dashboard yang isinya:

### **Komponen yang WAJIB ada (semua dipakai & dibaca inputnya):**

* **TextView**
* **ImageView**
* **Button**
* **EditText**
* **ToggleButton**
* **CheckBox**
* **Switch**
* **RadioGroup + RadioButton** (misalnya pilihan tipe mobil / transmisi)
* **TimePicker** (waktu sewa)
* **DatePicker** (tanggal sewa)

### Aplikasi harus bisa:

* Membaca semua input user dari komponen di atas
* Menampilkan data hasil input (misalnya via Toast / TextView)

// Contoh data dummy mobil:

* Mobil 1: Avanza 2020 – Harga 350.000/hari – Garansi servis – Jaminan KTP
* Mobil 2: Xpander – Harga 450.000/hari – Garansi mesin – Jaminan SIM
* Tidak perlu database

Dashboard boleh menampilkan list mobil dummy menggunakan RecyclerView (opsional).

---

## **🟦 6. Fragment Requirement**

* Activity Dashboard **harus menggunakan Fragment** untuk menampilkan menu UI.
* Minimal 1 Fragment (boleh lebih).

---

## **🟦 7. Layout**

* Gunakan layouting yang bersih, responsif (ConstraintLayout / LinearLayout).
* UI rapi sesuai tema RentCar.

---

## **🟦 8. Output yang harus disediakan**

* **Project lengkap Android Studio**
* **Video penjelasan kode program**
  Berisi penjelasan sesuai poin:

  * Splash Screen
  * Login + Explicit Intent
  * Lupa Password + Implicit Intent
  * Dashboard + Fragment
  * Pembacaan semua komponen UI Android
  * Dummy data mobil

---

## **🟦 9. Tambahan**

* Semua data pengguna dan data mobil adalah **dummy data** (tanpa database).
* Pastikan aplikasi dapat dijalankan di HP Android.

---

# 🟩 **HASIL YANG SAYA HARAPKAN**

Dari prompt ini, berikan saya:

1. **Struktur project lengkap**
2. **Semua file Java (Activity, Fragment, Adapter jika perlu)**
3. **Semua file XML layout**
4. **Kode Splash Screen, Login, Lupa Password, Dashboard Fragment**
5. **Contoh dummy mobil**
6. **Cara menjalankan aplikasinya di HP Android**
7. **Penjelasan alur aplikasi**

---

Jika ada yang kurang, tolong tanyakan agar aplikasi sesuai ketentuan UTS.

---

## 📌 Kamu tinggal copy dan kirim prompt ini ke ChatGPT lain atau saya sendiri bisa buatkan seluruh project-nya.

Kalau kamu mau, saya bisa langsung:

### ✔ Membuatkan **seluruh source code lengkap**

### ✔ Beserta **struktur folder**, **layout XML**, **Activity**, **Fragment**

### ✔ Sampai apk final

Mau saya lanjut buatkan **projek lengkapnya sekarang**?

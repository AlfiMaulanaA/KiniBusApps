package com.kinibus.apps;

import com.google.firebase.firestore.DocumentId;
import java.util.Date;

public class Bus {
    @DocumentId
    private String id;
    private String nama;
    private String jenis;
    private long kapasitas;
    private long harga;
    private String keberangkatan;
    private String tujuan;
    private Date waktuKeberangkatan;
    private Date waktuTiba;
    private String durasi;
    private long kursiTersedia;
    private boolean tersedia;

    public Bus() {}

    public Bus(String nama, String jenis, long kapasitas, long harga, String keberangkatan, String tujuan, Date waktuKeberangkatan, Date waktuTiba, String durasi, long kursiTersedia) {
        this.nama = nama;
        this.jenis = jenis;
        this.kapasitas = kapasitas;
        this.harga = harga;
        this.keberangkatan = keberangkatan;
        this.tujuan = tujuan;
        this.waktuKeberangkatan = waktuKeberangkatan;
        this.waktuTiba = waktuTiba;
        this.durasi = durasi;
        this.kursiTersedia = kursiTersedia;
        this.tersedia = true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public long getKapasitas() { return kapasitas; }
    public void setKapasitas(long kapasitas) { this.kapasitas = kapasitas; }
    public long getHarga() { return harga; }
    public void setHarga(long harga) { this.harga = harga; }
    public String getKeberangkatan() { return keberangkatan; }
    public void setKeberangkatan(String keberangkatan) { this.keberangkatan = keberangkatan; }
    public String getTujuan() { return tujuan; }
    public void setTujuan(String tujuan) { this.tujuan = tujuan; }
    public Date getWaktuKeberangkatan() { return waktuKeberangkatan; }
    public void setWaktuKeberangkatan(Date waktuKeberangkatan) { this.waktuKeberangkatan = waktuKeberangkatan; }
    public Date getWaktuTiba() { return waktuTiba; }
    public void setWaktuTiba(Date waktuTiba) { this.waktuTiba = waktuTiba; }
    public String getDurasi() { return durasi; }
    public void setDurasi(String durasi) { this.durasi = durasi; }
    public long getKursiTersedia() { return kursiTersedia; }
    public void setKursiTersedia(long kursiTersedia) { this.kursiTersedia = kursiTersedia; }
    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

}
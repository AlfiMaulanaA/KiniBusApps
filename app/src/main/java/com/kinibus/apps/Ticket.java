package com.kinibus.apps;

import com.google.firebase.Timestamp;
import java.io.Serializable;

public class Ticket implements Serializable {
    private String id;
    private String bus_nama;
    private String jenis;
    private String seat;
    private String rute_awal;
    private String rute_akhir;
    private String jam_berangkat;
    private String jam_sampai;
    private String tanggal_sewa; // Format: YYYY-MM-DD
    private String status; // "MENUNGGU_BAYAR" atau "E_TIKET_TERBIT"
    private String ref_code;
    private double harga;
    private String penumpang;
    private Timestamp created_at;

    // Empty constructor required for Firestore
    public Ticket() {}

    public Ticket(String bus_nama, String jenis, String seat, String rute_awal, String rute_akhir,
                  String jam_berangkat, String jam_sampai, String tanggal_sewa, String status,
                  String ref_code, double harga, String penumpang) {
        this.bus_nama = bus_nama;
        this.jenis = jenis;
        this.seat = seat;
        this.rute_awal = rute_awal;
        this.rute_akhir = rute_akhir;
        this.jam_berangkat = jam_berangkat;
        this.jam_sampai = jam_sampai;
        this.tanggal_sewa = tanggal_sewa;
        this.status = status;
        this.ref_code = ref_code;
        this.harga = harga;
        this.penumpang = penumpang;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBus_nama() { return bus_nama; }
    public String getJenis() { return jenis; }
    public String getSeat() { return seat; }
    public String getRute_awal() { return rute_awal; }
    public String getRute_akhir() { return rute_akhir; }
    public String getJam_berangkat() { return jam_berangkat; }
    public String getJam_sampai() { return jam_sampai; }
    public String getTanggal_sewa() { return tanggal_sewa; }
    public String getStatus() { return status; }
    public String getRef_code() { return ref_code; }
    public double getHarga() { return harga; }
    public String getPenumpang() { return penumpang; }
    public Timestamp getCreated_at() { return created_at; }
}

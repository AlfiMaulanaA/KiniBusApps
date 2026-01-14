package com.kinibus.apps.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class User {
    @DocumentId
    private String id;
    private String email;
    private String nama;
    private String nomorTelepon;
    private String profileImageUrl;
    private boolean isActive;

    @ServerTimestamp
    private Date createdAt;

    @ServerTimestamp
    private Date updatedAt;

    // Default constructor untuk Firestore
    public User() {
        this.isActive = true;
    }

    // Constructor untuk registrasi
    public User(String email, String nama, String nomorTelepon) {
        this.email = email;
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.isActive = true;
    }

    // Constructor lengkap
    public User(String email, String nama, String nomorTelepon, String profileImageUrl) {
        this.email = email;
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.profileImageUrl = profileImageUrl;
        this.isActive = true;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", nama='" + nama + '\'' +
                ", nomorTelepon='" + nomorTelepon + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
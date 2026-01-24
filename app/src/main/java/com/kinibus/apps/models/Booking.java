package com.kinibus.apps.models;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Booking implements Serializable {
    @DocumentId
    private String id;
    private String userId;
    private String busId;
    private String busName;
    private String departure;
    private String destination;
    private Date departureDate;
    private String departureTime;
    private long price;
    private int passengerCount;
    private List<String> selectedSeats;
    private String bookingStatus; // "active", "completed", "cancelled"
    private String bookingId;
    private String bookingCode;     // GB-XXXXXX format for ticket
    private Date createdAt;
    private Date updatedAt;

    public Booking() {}

    public Booking(String userId, String busId, String busName, String departure, String destination,
                  Date departureDate, String departureTime, long price, int passengerCount,
                  List<String> selectedSeats) {
        this.userId = userId;
        this.busId = busId;
        this.busName = busName;
        this.departure = departure;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.price = price;
        this.passengerCount = passengerCount;
        this.selectedSeats = selectedSeats;
        this.bookingStatus = "active";
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBusId() { return busId; }
    public void setBusId(String busId) { this.busId = busId; }

    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }

    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Date getDepartureDate() { return departureDate; }
    public void setDepartureDate(Date departureDate) { this.departureDate = departureDate; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public List<String> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<String> selectedSeats) { this.selectedSeats = selectedSeats; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}

package com.kinibus.apps.models;

/**
 * Seat model representing individual bus seat
 * Used in seat selection grid
 */
public class Seat {
    private String seatNumber;      // "1A", "2B", etc.
    private SeatStatus status;      // AVAILABLE, SELECTED, BOOKED
    private boolean isDriverSeat;   // Special position for driver label
    private String passengerId;     // User ID who booked this seat (if booked)

    /**
     * Enum untuk seat status
     */
    public enum SeatStatus {
        AVAILABLE,  // Kursi tersedia untuk dipilih
        SELECTED,   // Kursi dipilih oleh user saat ini
        BOOKED      // Kursi sudah dipesan user lain
    }

    /**
     * Default constructor (required for Firestore)
     */
    public Seat() {
        this.status = SeatStatus.AVAILABLE;
        this.isDriverSeat = false;
    }

    /**
     * Constructor for creating seat
     */
    public Seat(String seatNumber, SeatStatus status) {
        this.seatNumber = seatNumber;
        this.status = status;
        this.isDriverSeat = false;
    }

    /**
     * Constructor for driver seat
     */
    public Seat(String seatNumber, boolean isDriverSeat) {
        this.seatNumber = seatNumber;
        this.isDriverSeat = isDriverSeat;
        this.status = SeatStatus.AVAILABLE;
    }

    // Getters and Setters
    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public boolean isDriverSeat() {
        return isDriverSeat;
    }

    public void setDriverSeat(boolean driverSeat) {
        isDriverSeat = driverSeat;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    /**
     * Check if seat is available for selection
     */
    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE && !isDriverSeat;
    }

    /**
     * Check if seat is selected
     */
    public boolean isSelected() {
        return status == SeatStatus.SELECTED;
    }

    /**
     * Toggle seat selection (available <-> selected)
     */
    public void toggleSelection() {
        if (status == SeatStatus.AVAILABLE) {
            status = SeatStatus.SELECTED;
        } else if (status == SeatStatus.SELECTED) {
            status = SeatStatus.AVAILABLE;
        }
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatNumber='" + seatNumber + '\'' +
                ", status=" + status +
                ", isDriverSeat=" + isDriverSeat +
                '}';
    }
}

package com.example.cinefast;

import java.util.List;

public class Booking {
    private String id;
    private String userId;
    private String movieTitle;
    private int seatCount;
    private List<String> selectedSeats;
    private double finalTotal;
    private long timestamp;

    public Booking() {
        // Required for Firebase
    }

    public Booking(String id, String userId, String movieTitle, int seatCount, List<String> selectedSeats, double finalTotal, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.seatCount = seatCount;
        this.selectedSeats = selectedSeats;
        this.finalTotal = finalTotal;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMovieTitle() { return movieTitle; }
    public int getSeatCount() { return seatCount; }
    public List<String> getSelectedSeats() { return selectedSeats; }
    public double getFinalTotal() { return finalTotal; }
    public long getTimestamp() { return timestamp; }
}

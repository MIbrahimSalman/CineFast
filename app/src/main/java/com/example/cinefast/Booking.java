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
    private long showTimestamp;
    private String movieDate;
    private String movieTime;

    public Booking() {
        // Required for Firebase
    }

    public Booking(String id, String userId, String movieTitle, int seatCount, List<String> selectedSeats, double finalTotal, long timestamp, long showTimestamp, String movieDate, String movieTime) {
        this.id = id;
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.seatCount = seatCount;
        this.selectedSeats = selectedSeats;
        this.finalTotal = finalTotal;
        this.timestamp = timestamp;
        this.showTimestamp = showTimestamp;
        this.movieDate = movieDate;
        this.movieTime = movieTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    
    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
    
    public List<String> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<String> selectedSeats) { this.selectedSeats = selectedSeats; }
    
    public double getFinalTotal() { return finalTotal; }
    public void setFinalTotal(double finalTotal) { this.finalTotal = finalTotal; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long getShowTimestamp() { return showTimestamp; }
    public void setShowTimestamp(long showTimestamp) { this.showTimestamp = showTimestamp; }

    public String getMovieDate() { return movieDate; }
    public void setMovieDate(String movieDate) { this.movieDate = movieDate; }

    public String getMovieTime() { return movieTime; }
    public void setMovieTime(String movieTime) { this.movieTime = movieTime; }
}

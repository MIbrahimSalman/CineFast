package com.example.cinefast;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.textMovieTitle.setText(booking.getMovieTitle());
        holder.textTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", booking.getFinalTotal()));
        
        String seatsStr = TextUtils.join(", ", booking.getSelectedSeats());
        holder.textSeats.setText("Seats (" + booking.getSeatCount() + "): " + seatsStr);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
        holder.textDate.setText("Booked on: " + sdf.format(new Date(booking.getTimestamp())));

        // Cancel logic (example: allowed within 24 hours)
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - booking.getTimestamp();
        long twentyFourHours = 24L * 60L * 60L * 1000L;

        if (diff > twentyFourHours) {
            holder.btnCancel.setEnabled(false);
            holder.btnCancel.setText("Cancellation Period Expired");
            holder.btnCancel.setAlpha(0.5f);
        } else {
            holder.btnCancel.setEnabled(true);
            holder.btnCancel.setText("Cancel Booking");
            holder.btnCancel.setAlpha(1.0f);
            holder.btnCancel.setOnClickListener(v -> {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookings")
                        .child(booking.getUserId())
                        .child(booking.getId());
                ref.removeValue().addOnSuccessListener(aVoid -> {
                    Toast.makeText(v.getContext(), "Booking cancelled", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(v.getContext(), "Failed to cancel", Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textMovieTitle, textSeats, textTotal, textDate;
        Button btnCancel;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textMovieTitle = itemView.findViewById(R.id.textMovieTitle);
            textSeats = itemView.findViewById(R.id.textSeats);
            textTotal = itemView.findViewById(R.id.textTotal);
            textDate = itemView.findViewById(R.id.textDate);
            btnCancel = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}

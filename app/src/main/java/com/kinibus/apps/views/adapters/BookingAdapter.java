package com.kinibus.apps.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Booking;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onBookingClicked(Booking booking);
    }

    public BookingAdapter(List<Booking> bookings, OnBookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_history, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void updateBookings(List<Booking> newBookings) {
        this.bookings = newBookings;
        notifyDataSetChanged();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRouteFrom, tvRouteTo, tvBusName, tvPrice, tvDateTime, tvSeats, tvStatus, tvBookingId;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteFrom = itemView.findViewById(R.id.tv_route_from);
            tvRouteTo = itemView.findViewById(R.id.tv_route_to);
            tvBusName = itemView.findViewById(R.id.tv_bus_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvSeats = itemView.findViewById(R.id.tv_seats);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvBookingId = itemView.findViewById(R.id.tv_booking_id);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onBookingClicked(bookings.get(position));
                }
            });
        }

        public void bind(Booking booking) {
            // Route
            tvRouteFrom.setText(booking.getDeparture());
            tvRouteTo.setText(booking.getDestination());

            // Bus name
            tvBusName.setText(booking.getBusName());

            // Price
            tvPrice.setText(String.format(Locale.getDefault(), "Rp %,d", booking.getPrice()));

            // Date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", new Locale("id", "ID"));
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String dateStr = dateFormat.format(booking.getDepartureDate());
            String timeStr = booking.getDepartureTime();
            tvDateTime.setText(dateStr + " • " + timeStr + " WIB");

            // Seats
            List<String> seats = booking.getSelectedSeats();
            String seatsText = booking.getPassengerCount() + " Kursi";
            if (seats != null && !seats.isEmpty()) {
                seatsText += " (" + String.join(", ", seats) + ")";
            }
            tvSeats.setText(seatsText);

            // Status
            String status = booking.getBookingStatus();
            String statusText = "Aktif";
            int statusColor = R.color.green_primary;

            switch (status) {
                case "active":
                    statusText = "Aktif";
                    statusColor = R.color.green_primary;
                    break;
                case "completed":
                    statusText = "Selesai";
                    statusColor = R.color.blue_primary;
                    break;
                case "cancelled":
                    statusText = "Dibatalkan";
                    statusColor = R.color.red_primary;
                    break;
            }

            tvStatus.setText(statusText);
            tvStatus.setTextColor(itemView.getContext().getColor(statusColor));

            // Booking ID
            String bookingId = booking.getBookingId();
            if (bookingId != null && !bookingId.isEmpty()) {
                tvBookingId.setText("ID: " + bookingId);
            } else {
                tvBookingId.setText("ID: " + booking.getId().substring(0, 6).toUpperCase());
            }
        }
    }
}

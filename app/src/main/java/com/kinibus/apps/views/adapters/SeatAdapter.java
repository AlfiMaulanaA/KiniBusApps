package com.kinibus.apps.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kinibus.apps.R;
import com.kinibus.apps.models.Seat;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter untuk seat grid RecyclerView
 * Handles seat selection logic dan UI updates
 */
public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    
    private Context context;
    private List<Seat> seatList;
    private OnSeatClickListener listener;

    /**
     * Interface untuk seat click callback
     */
    public interface OnSeatClickListener {
        void onSeatClicked(Seat seat, int position);
    }

    /**
     * Constructor
     */
    public SeatAdapter(Context context, List<Seat> seatList, OnSeatClickListener listener) {
        this.context = context;
        this.seatList = seatList != null ? seatList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.bind(seat, position);
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    /**
     * Update seat list dan refresh UI
     */
    public void updateSeats(List<Seat> newSeats) {
        this.seatList = newSeats;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder untuk seat item
     */
    class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeatNumber;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeatNumber = itemView.findViewById(R.id.tv_seat_number);
        }

        public void bind(Seat seat, int position) {
            // Handle driver seat separately
            if (seat.isDriverSeat()) {
                tvSeatNumber.setText("SOPIR");
                tvSeatNumber.setTextSize(10);
                tvSeatNumber.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.seat_disabled)
                ));
                tvSeatNumber.setTextColor(ContextCompat.getColor(context, R.color.text_light));
                tvSeatNumber.setEnabled(false);
                tvSeatNumber.setClickable(false);
                return;
            }

            // Set seat number
            tvSeatNumber.setText(seat.getSeatNumber());
            tvSeatNumber.setTextSize(14);

            // Set background dan text color based on status
            switch (seat.getStatus()) {
                case AVAILABLE:
                    tvSeatNumber.setBackgroundResource(R.drawable.bg_seat_available);
                    tvSeatNumber.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
                    tvSeatNumber.setEnabled(true);
                    tvSeatNumber.setSelected(false);
                    break;

                case SELECTED:
                    tvSeatNumber.setBackgroundResource(R.drawable.bg_seat_available);
                    tvSeatNumber.setTextColor(ContextCompat.getColor(context, R.color.white));
                    tvSeatNumber.setEnabled(true);
                    tvSeatNumber.setSelected(true);
                    // Force background tint untuk selected state
                    tvSeatNumber.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.seat_selected)
                    ));
                    break;

                case BOOKED:
                    tvSeatNumber.setBackgroundResource(R.drawable.bg_seat_booked);
                    tvSeatNumber.setTextColor(ContextCompat.getColor(context, R.color.text_light));
                    tvSeatNumber.setEnabled(false);
                    tvSeatNumber.setSelected(false);
                    break;
            }

            // Set click listener
            tvSeatNumber.setOnClickListener(v -> {
                if (seat.isAvailable() || seat.isSelected()) {
                    if (listener != null) {
                        listener.onSeatClicked(seat, position);
                    }
                }
            });
        }
    }
}

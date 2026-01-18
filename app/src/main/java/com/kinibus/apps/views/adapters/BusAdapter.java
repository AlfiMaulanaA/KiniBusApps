package com.kinibus.apps.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.kinibus.apps.R;
import com.kinibus.apps.models.Bus;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    public interface OnBusItemClickListener {
        void onBusItemClicked(Bus bus);
    }

    private final List<Bus> busList;
    private final OnBusItemClickListener listener;

    public BusAdapter(List<Bus> busList, OnBusItemClickListener listener) {
        this.busList = busList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus, listener);
    }

    @Override
    public int getItemCount() {
        return (busList != null) ? busList.size() : 0;
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusName, tvClass, tvPrice, tvDepartureTime, tvDepartureLoc, tvArrivalTime, tvArrivalLoc, tvDuration, tvSeatsLeft;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusName = itemView.findViewById(R.id.tv_bus_name);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDepartureTime = itemView.findViewById(R.id.tv_departure_time);
            tvDepartureLoc = itemView.findViewById(R.id.tv_departure_loc);
            tvArrivalTime = itemView.findViewById(R.id.tv_arrival_time);
            tvArrivalLoc = itemView.findViewById(R.id.tv_arrival_loc);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvSeatsLeft = itemView.findViewById(R.id.tv_seats_left);
        }

        public void bind(final Bus bus, final OnBusItemClickListener listener) {
            tvBusName.setText(bus.getNama() != null ? bus.getNama() : "");
            tvClass.setText(bus.getJenis() != null ? bus.getJenis().toUpperCase() : "");
            tvPrice.setText(itemView.getContext().getString(R.string.price_format, bus.getHarga()));

            String departureTime = itemView.getContext().getString(R.string.default_time);
            String arrivalTime = itemView.getContext().getString(R.string.default_time);
            String duration = itemView.getContext().getString(R.string.default_dash);

            // Create SimpleDateFormat with current locale for proper localization
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            if (bus.getWaktuKeberangkatan() != null) {
                departureTime = timeFormat.format(bus.getWaktuKeberangkatan());
            }
            if (bus.getWaktuTiba() != null) {
                arrivalTime = timeFormat.format(bus.getWaktuTiba());
            }

            if (bus.getWaktuKeberangkatan() != null && bus.getWaktuTiba() != null) {
                long diffInMillis = bus.getWaktuTiba().getTime() - bus.getWaktuKeberangkatan().getTime();
                long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                duration = itemView.getContext().getString(R.string.duration_format, hours, minutes);
            }

            tvDepartureTime.setText(departureTime);
            tvArrivalTime.setText(arrivalTime);
            tvDuration.setText(duration);
            tvDepartureLoc.setText(bus.getKeberangkatan() != null ? bus.getKeberangkatan() : itemView.getContext().getString(R.string.default_dash));
            tvArrivalLoc.setText(bus.getTujuan() != null ? bus.getTujuan() : itemView.getContext().getString(R.string.default_dash));

            if (bus.isTersedia() && bus.getKursiTersedia() > 0) {
                tvSeatsLeft.setText(itemView.getContext().getString(R.string.seats_left_format, bus.getKursiTersedia()));
                tvSeatsLeft.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_orange_dark));
                itemView.setAlpha(1.0f);
                itemView.setOnClickListener(v -> listener.onBusItemClicked(bus));
            } else {
                if (!bus.isTersedia()) {
                    tvSeatsLeft.setText(itemView.getContext().getString(R.string.not_available));
                } else {
                    tvSeatsLeft.setText(itemView.getContext().getString(R.string.sold_out));
                }
                tvSeatsLeft.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark));
                itemView.setAlpha(0.5f);
                itemView.setClickable(false);
            }
        }
    }
}

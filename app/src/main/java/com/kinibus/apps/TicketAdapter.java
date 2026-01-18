package com.kinibus.apps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> ticketList;

    public TicketAdapter(Context context, List<Ticket> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // PERBAIKAN: Menggunakan item_ticket.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        // Set Data Text
        holder.tvBusName.setText(ticket.getBus_nama());
        holder.tvDetail.setText(ticket.getTanggal_sewa() + " • " + ticket.getJenis());
        holder.tvRouteStart.setText(ticket.getRute_awal());
        holder.tvRouteEnd.setText(ticket.getRute_akhir());
        holder.tvTimeStart.setText(ticket.getJam_berangkat());
        holder.tvTimeEnd.setText(ticket.getJam_sampai());
        holder.tvPrice.setText("Rp " + String.format("%,.0f", ticket.getHarga()));

        // LOGIKA STATUS WARNA
        if ("MENUNGGU_BAYAR".equals(ticket.getStatus())) {
            // Tampilan Menunggu (Orange)
            holder.tvStatus.setText("MENUNGGU PEMBAYARAN");
            holder.bgStatus.setBackgroundColor(Color.parseColor("#FFF3E0")); // Orange muda background
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // Orange text
            holder.icStatus.setColorFilter(Color.parseColor("#EF6C00")); // Icon orange
            holder.icStatus.setImageResource(R.drawable.ic_clock); // Ganti icon jadi jam

            holder.btnAction.setText("Bayar Sekarang");
            holder.btnAction.setBackgroundColor(Color.parseColor("#000000")); // Hitam
            holder.btnAction.setTextColor(Color.WHITE);

        } else {
            // Tampilan Selesai (Hijau)
            holder.tvStatus.setText("E-TIKET TERBIT");
            holder.bgStatus.setBackgroundColor(Color.parseColor("#E8F5E9")); // Hijau muda background
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // Hijau text
            holder.icStatus.setColorFilter(Color.parseColor("#2E7D32")); // Icon hijau
            holder.icStatus.setImageResource(R.drawable.ic_check); // Icon centang

            holder.btnAction.setText("Lihat Detail");
            holder.btnAction.setBackgroundColor(Color.parseColor("#EEEEEE")); // Abu-abu
            holder.btnAction.setTextColor(Color.BLACK);
        }

        // Klik Item ke Detail
        View.OnClickListener goToDetail = v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("TICKET_DATA", ticket);
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(goToDetail);
        holder.btnAction.setOnClickListener(goToDetail);
    }

    @Override
    public int getItemCount() {
        return ticketList != null ? ticketList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusName, tvDetail, tvRouteStart, tvRouteEnd, tvTimeStart, tvTimeEnd, tvPrice, tvStatus;
        View bgStatus;
        ImageView icStatus;
        MaterialButton btnAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Binding ID dari item_ticket.xml
            tvBusName = itemView.findViewById(R.id.busName);
            tvDetail = itemView.findViewById(R.id.busDetail);
            tvRouteStart = itemView.findViewById(R.id.routeStart);
            tvRouteEnd = itemView.findViewById(R.id.routeEnd);
            tvTimeStart = itemView.findViewById(R.id.timeStart);
            tvTimeEnd = itemView.findViewById(R.id.timeEnd);
            tvPrice = itemView.findViewById(R.id.price);

            // Header Status Components
            tvStatus = itemView.findViewById(R.id.tvStatusHeader);
            bgStatus = itemView.findViewById(R.id.bgStatus);
            icStatus = itemView.findViewById(R.id.icStatus);

            // Button
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}
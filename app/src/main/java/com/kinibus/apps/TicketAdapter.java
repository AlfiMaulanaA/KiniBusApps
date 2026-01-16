package com.kinibus.apps;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
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
        // Asumsikan Anda membuat layout item row terpisah bernama item_ticket.xml
        // atau menggunakan layout yang sudah ada
        View view = LayoutInflater.from(context).inflate(R.layout.riwayat_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);

        holder.tvBusName.setText(ticket.getBus_nama());
        holder.tvDate.setText(ticket.getTanggal_sewa());
        holder.tvRouteStart.setText(ticket.getRute_awal());
        holder.tvRouteEnd.setText(ticket.getRute_akhir());
        holder.tvTimeStart.setText(ticket.getJam_berangkat());
        holder.tvTimeEnd.setText(ticket.getJam_sampai());
        holder.tvPrice.setText("Rp " + String.format("%,.0f", ticket.getHarga()));

        if ("MENUNGGU_BAYAR".equals(ticket.getStatus())) {
            holder.tvStatus.setText("MENUNGGU PEMBAYARAN");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        } else {
            holder.tvStatus.setText("E-TIKET TERBIT");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("TICKET_DATA", ticket);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusName, tvDate, tvRouteStart, tvRouteEnd, tvTimeStart, tvTimeEnd, tvPrice, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sesuaikan ID dengan layout item_ticket.xml Anda
            tvBusName = itemView.findViewById(R.id.busName);
            tvDate = itemView.findViewById(R.id.busDetail); // Menggunakan textview detail untuk tanggal
            tvRouteStart = itemView.findViewById(R.id.routeStart);
            tvRouteEnd = itemView.findViewById(R.id.routeEnd);
            tvTimeStart = itemView.findViewById(R.id.timeStart);
            tvTimeEnd = itemView.findViewById(R.id.timeEnd);
            tvPrice = itemView.findViewById(R.id.price);
            tvStatus = itemView.findViewById(R.id.tvStatusHeader); // Anda perlu menambahkan ID ini di XML item
        }
    }
}

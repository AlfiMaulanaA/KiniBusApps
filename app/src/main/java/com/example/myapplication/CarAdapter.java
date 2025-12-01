package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private Context context;
    private int selectedPosition = -1; // To track selected car

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvCarName.setText(car.getName());
        holder.tvCarPrice.setText(car.getPrice());
        holder.tvCarDesc.setText(""); // Can set description from strings

        // Set image based on car name
        int imgRes = android.R.drawable.ic_menu_camera; // default
        String name = car.getName().toLowerCase();
        if (name.contains("avanza")) imgRes = R.drawable.avanza;
        else if (name.contains("xpander")) imgRes = R.drawable.xpander;
        else if (name.contains("brio")) imgRes = R.drawable.brio;
        else if (name.contains("civic")) imgRes = R.drawable.civic;
        else if (name.contains("etios")) imgRes = R.drawable.etios;
        else if (name.contains("mobilio")) imgRes = R.drawable.mobilio;
        else if (name.contains("ertiga")) imgRes = R.drawable.ertiga;
        // For Mitsubishi Expander, perhaps use ertiga or default
        else if (name.contains("expander")) imgRes = R.drawable.ertiga;
        holder.ivCarImage.setImageResource(imgRes);

        // Highlight selected card
        holder.itemView.setBackgroundResource(selectedPosition == position ? android.R.color.darker_gray : android.R.color.white);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            for (Car c : carList) c.setSelected(false);
            car.setSelected(true);
            notifyDataSetChanged(); // Refresh to show selection
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public Car getSelectedCar() {
        return selectedPosition >= 0 ? carList.get(selectedPosition) : null;
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCarImage;
        TextView tvCarName, tvCarPrice, tvCarDesc;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCarImage = itemView.findViewById(R.id.iv_car_image);
            tvCarName = itemView.findViewById(R.id.tv_car_name);
            tvCarPrice = itemView.findViewById(R.id.tv_car_price);
            tvCarDesc = itemView.findViewById(R.id.tv_car_desc);
        }
    }
}

package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment {

    // UI Components
    private TextView tvTitle;
    private Button btnSubmit, btnPickDate, btnPickTime;
    private EditText etName, etPhone;
    private CheckBox checkBox;
    private SwitchCompat switchLocation;
    private RecyclerView rvCars;
    private CarAdapter carAdapter;
    private Button btnPickReturnDate;

    // Data storage
    private String selectedCar = "";
    private String selectedDate = "";
    private String selectedTime = "";
    private String selectedReturnDate = "";
    private boolean checkAccepted = false;
    private boolean switchServices = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvTitle = view.findViewById(R.id.tv_dashboard_title);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnPickDate = view.findViewById(R.id.btn_pick_date);
        btnPickTime = view.findViewById(R.id.btn_pick_time);
        btnPickReturnDate = view.findViewById(R.id.btn_pick_return_date);
        etName = view.findViewById(R.id.et_name);
        etPhone = view.findViewById(R.id.et_phone);
        checkBox = view.findViewById(R.id.check_box);
        switchLocation = view.findViewById(R.id.switch_location);
        rvCars = view.findViewById(R.id.rv_cars);

        // Setup RecyclerView for cars
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("Avanza 2020", getString(R.string.price_avanza), "android.R.drawable.ic_menu_camera"));
        carList.add(new Car("Xpander", getString(R.string.price_xpander), "android.R.drawable.ic_menu_compass"));
        carList.add(new Car("Honda Brio", getString(R.string.price_brio), "android.R.drawable.ic_menu_gallery"));
        carList.add(new Car("Honda Civic", getString(R.string.price_civic), "android.R.drawable.ic_menu_send"));
        carList.add(new Car("Toyota Etios", getString(R.string.price_toyota_etios), "android.R.drawable.ic_menu_directions"));
        carList.add(new Car("Mitsubishi Expander", getString(R.string.price_mitsubishi_expander), "android.R.drawable.ic_menu_help"));
        carList.add(new Car("Honda Mobilio", getString(R.string.price_honda_mobilio), "android.R.drawable.ic_menu_info_details"));
        carList.add(new Car("Suzuki Ertiga", getString(R.string.price_suzuki_ertiga), "android.R.drawable.ic_menu_preferences"));

        carAdapter = new CarAdapter(getContext(), carList);
        rvCars.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvCars.setAdapter(carAdapter);

        // Event listeners

        // Date picker button
        btnPickDate.setOnClickListener(v -> showDatePicker());

        // Time picker button
        btnPickTime.setOnClickListener(v -> showTimePicker());

        // Return date picker button
        btnPickReturnDate.setOnClickListener(v -> showReturnDatePicker());

        // Check box listener
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkAccepted = isChecked;
        });

        // Switch listener
        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchServices = isChecked;
        });

        // Submit button - go to payment page
        btnSubmit.setOnClickListener(v -> {
            String inputData = "Detail Pembayaran:\n\n" + readAllInputs();
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            intent.putExtra("DETAILS", inputData);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
            (view, year1, month1, day1) -> {
                selectedDate = day1 + "/" + (month1 + 1) + "/" + year1;
                btnPickDate.setText("Tanggal: " + selectedDate);
            }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
            (view, hourOfDay, minute1) -> {
                selectedTime = hourOfDay + ":" + minute1;
                btnPickTime.setText("Waktu: " + selectedTime);
            }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showReturnDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
            (view, year1, month1, day1) -> {
                selectedReturnDate = day1 + "/" + (month1 + 1) + "/" + year1;
                btnPickReturnDate.setText("Tanggal Kembali: " + selectedReturnDate);
            }, year, month, day);
        datePickerDialog.show();
    }

    private String calculateTotalPrice() {
        Car car = carAdapter.getSelectedCar();
        if (car != null && !selectedDate.isEmpty() && !selectedReturnDate.isEmpty()) {
            try {
                // Parse prices like "Rp 350.000/hari - ..."
                String pricePart = car.getPrice().split(" - ")[0]; // Get "Rp 350.000/hari"
                String priceStr = pricePart.replace("Rp ", "").replace("/hari", "").replace(".", "");
                long pricePerDay = Long.parseLong(priceStr);

                // Parse dates dd/MM/yyyy to Calendar for days diff
                String[] startSplit = selectedDate.split("/");
                String[] returnSplit = selectedReturnDate.split("/");

                Calendar startCal = Calendar.getInstance();
                startCal.set(Integer.parseInt(startSplit[2]), Integer.parseInt(startSplit[1]) - 1, Integer.parseInt(startSplit[0]));

                Calendar returnCal = Calendar.getInstance();
                returnCal.set(Integer.parseInt(returnSplit[2]), Integer.parseInt(returnSplit[1]) - 1, Integer.parseInt(returnSplit[0]));

                long diffInMillis = returnCal.getTimeInMillis() - startCal.getTimeInMillis();
                long days = diffInMillis / (24 * 60 * 60 * 1000); // approximate
                if (days < 1) days = 1; // minimum 1 day

                long total = pricePerDay * days;
                return "Rp " + String.format("%,d", total).replace(",", ".");
            } catch (Exception e) {
                return "Rp 0";
            }
        }
        return "Rp 0";
    }



    private String readAllInputs() {
        StringBuilder result = new StringBuilder();

        // EditText inputs
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        result.append(String.format("%-25s %s\n", "Nama:", name));
        result.append(String.format("%-25s %s\n", "Telepon:", phone));

        Car selected = carAdapter.getSelectedCar();
        if (selected != null) {
            result.append(String.format("%-25s %s\n", "Mobil:", selected.getName() + " - " + selected.getPrice()));
        } else {
            result.append(String.format("%-25s %s\n", "Mobil:", "Tidak ada yang dipilih"));
        }

        result.append(String.format("%-25s %s\n", "Tanggal Sewa:", selectedDate));
        result.append(String.format("%-25s %s\n", "Tanggal Kembali:", selectedReturnDate));
        result.append(String.format("%-25s %s\n", "Terms Accepted:", checkAccepted ? "Ya" : "Tidak"));
        result.append(String.format("%-25s %s\n", "Layanan Lokasi:", switchServices ? "Aktif" : "Tidak"));

        // Calculate total price
        String totalPrice = calculateTotalPrice();
        result.append("\n").append(String.format("%-35s %s\n", "Total Harga:", totalPrice));

        return result.toString();
    }
}

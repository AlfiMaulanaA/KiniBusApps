package com.kinibus.apps.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.kinibus.apps.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditTripBottomSheetFragment extends BottomSheetDialogFragment {

    public interface OnTripDetailsChangedListener {
        void onTripDetailsChanged(String origin, String destination, Date date, String passengers);
    }

    private OnTripDetailsChangedListener mListener;

    private TextInputEditText etDate;
    private AutoCompleteTextView actvOrigin, actvDestination, actvPassenger;
    private ImageView ivSwap;
    private Button btnSearchTicket;
    private Date selectedDate;

    public static EditTripBottomSheetFragment newInstance(String origin, String destination, long date, String passengers) {
        EditTripBottomSheetFragment fragment = new EditTripBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("ARG_ORIGIN", origin);
        args.putString("ARG_DESTINATION", destination);
        args.putLong("ARG_DATE", date);
        args.putString("ARG_PASSENGERS", passengers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTripDetailsChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTripDetailsChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_trip, container, false);
        etDate = view.findViewById(R.id.et_date);
        actvOrigin = view.findViewById(R.id.actv_origin);
        actvDestination = view.findViewById(R.id.actv_destination);
        actvPassenger = view.findViewById(R.id.actv_passenger);
        ivSwap = view.findViewById(R.id.iv_swap);
        btnSearchTicket = view.findViewById(R.id.btn_search_ticket);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] origins = {"Gambir", "Kp. Rambutan", "Pulo Gebang", "Plaza Senayan", "Lebak Bulus"};
        String[] destinations = {"Pasteur", "Lw. Panjang", "Cicaheum", "Cihampelas", "Gedebage"};
        String[] passengerCounts = {"1 Orang", "2 Orang", "3 Orang", "4 Orang"};

        Context listContext = new ContextThemeWrapper(getContext(), com.google.android.material.R.style.Theme_MaterialComponents_Light);
        
        ArrayAdapter<String> originAdapter = new ArrayAdapter<>(listContext, android.R.layout.simple_list_item_1, origins);
        actvOrigin.setAdapter(originAdapter);

        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(listContext, android.R.layout.simple_list_item_1, destinations);
        actvDestination.setAdapter(destinationAdapter);

        ArrayAdapter<String> passengerAdapter = new ArrayAdapter<>(listContext, android.R.layout.simple_list_item_1, passengerCounts);
        actvPassenger.setAdapter(passengerAdapter);

        if (getArguments() != null) {
            actvOrigin.setText(getArguments().getString("ARG_ORIGIN"), false);
            actvDestination.setText(getArguments().getString("ARG_DESTINATION"), false);
            actvPassenger.setText(getArguments().getString("ARG_PASSENGERS"), false);
            updateDate(new Date(getArguments().getLong("ARG_DATE")));
        } else {
            updateDate(new Date());
        }

        etDate.setOnClickListener(v -> showDatePicker());
        ivSwap.setOnClickListener(v -> swapLocations());
        actvOrigin.setOnClickListener(v -> actvOrigin.showDropDown());
        actvDestination.setOnClickListener(v -> actvDestination.showDropDown());
        actvPassenger.setOnClickListener(v -> actvPassenger.showDropDown());

        btnSearchTicket.setOnClickListener(v -> {
            String origin = actvOrigin.getText().toString();
            String destination = actvDestination.getText().toString();
            String passengers = actvPassenger.getText().toString();

            mListener.onTripDetailsChanged(origin, destination, selectedDate, passengers);
            dismiss();
        });
    }

    private void showDatePicker() {
        // Allow all dates for testing (remove date validation)
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        long selection = MaterialDatePicker.todayInUtcMilliseconds();
        if (selectedDate != null) {
            selection = selectedDate.getTime();
        }

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(selection)
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(sel -> {
            TimeZone timeZoneUTC = TimeZone.getDefault();
            long offset = timeZoneUTC.getOffset(new Date().getTime()) * -1;
            updateDate(new Date(sel + offset));
        });

        datePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    private void swapLocations() {
        String origin = actvOrigin.getText().toString();
        String destination = actvDestination.getText().toString();
        actvOrigin.setText(destination, false);
        actvDestination.setText(origin, false);
    }

    private void updateDate(Date date) {
        selectedDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM", Locale.getDefault());
        etDate.setText(sdf.format(date));
    }
}

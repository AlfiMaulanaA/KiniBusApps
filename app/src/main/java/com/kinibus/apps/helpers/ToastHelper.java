package com.kinibus.apps.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kinibus.apps.R;

/**
 * Helper class for showing custom styled toast messages
 */
public class ToastHelper {

    /**
     * Show success toast with custom styling
     */
    public static void showSuccessToast(Context context, String message) {
        showCustomToast(context, message, R.drawable.toast_background, R.drawable.ic_check_circle);
    }

    /**
     * Show error toast with custom styling
     */
    public static void showErrorToast(Context context, String message) {
        showCustomToast(context, message, R.drawable.toast_background_error, R.drawable.ic_error);
    }

    /**
     * Show info toast with custom styling
     */
    public static void showInfoToast(Context context, String message) {
        showCustomToast(context, message, R.drawable.toast_background_info, R.drawable.ic_info);
    }

    /**
     * Show custom toast with specified background and icon
     */
    private static void showCustomToast(Context context, String message, int backgroundResId, int iconResId) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.custom_toast_layout, null);

            // Set background
            layout.findViewById(R.id.custom_toast_container).setBackgroundResource(backgroundResId);

            // Set icon
            ImageView icon = layout.findViewById(R.id.toast_icon);
            icon.setImageResource(iconResId);

            // Set message
            TextView text = layout.findViewById(R.id.toast_message);
            text.setText(message);

            // Create and show toast
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            // Fallback to regular toast if custom toast fails
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
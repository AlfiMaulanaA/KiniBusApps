package com.example.myapplication;

public class Car {
    private String name;
    private String price;
    private String imageResource; // Placeholder, e.g., android:drawable
    private boolean selected;

    public Car(String name, String price, String imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.selected = false;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageResource() { return imageResource; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}

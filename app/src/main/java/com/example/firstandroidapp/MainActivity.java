package com.example.firstandroidapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {}
    public void onNothingSelected(AdapterView<?> parent) {}
    public Map<String, Double> conversionMap = new HashMap<>();
    public int arraySelect = R.array.all_types;

    public String convert(String initialUnit, String conversionUnit, double value, String category) {
        double result = 0;
        double baseValue;
        if (!Objects.equals(initialUnit, conversionUnit)) {
            if (!Objects.equals(category, "Temperature")) {
                baseValue = value / conversionMap.get(initialUnit);
                result = baseValue * conversionMap.get(conversionUnit);
            }
            else if (Objects.equals(initialUnit, "Fahrenheit")) {
                result = (value - 32) / 1.8;
                if (Objects.equals(conversionUnit, "Kelvin")) {
                    result = result + 273.15;
                }
            }
            else if (Objects.equals(initialUnit, "Celsius")) {
                if (Objects.equals(conversionUnit, "Fahrenheit")) {
                    result = (value * 1.8) + 32;
                }
                else {
                    result = value - 273.15;
                }
            }
            else {
                result = value + 273.15;
                if (Objects.equals(conversionUnit, "Fahrenheit")) {
                    result = (result * 1.8) + 32;
                }
            }
        }
        else {
            result = value;
        }
        return String.valueOf(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Resources res = getResources();
        String[] conv_types = res.getStringArray(R.array.all_types);

        TextView label3 = findViewById(R.id.categoryText);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        TextView label1 = findViewById(R.id.fromText);
        Spinner fromSpinner = findViewById(R.id.fromSpinner);
        EditText input = findViewById(R.id.input);
        TextView label2 = findViewById(R.id.toText);
        Spinner toSpinner = findViewById(R.id.toSpinner);
        Button convertBtn = findViewById(R.id.convertBtn);
        TextView output = findViewById(R.id.output);

        conversionMap.put("USD", 1.0);
        conversionMap.put("AUD", 1.55);
        conversionMap.put("EUR", 0.92);
        conversionMap.put("JPY", 148.5);
        conversionMap.put("GBP", 0.78);
        conversionMap.put("Miles per Gallon", 1.0);
        conversionMap.put("Kilometres per Litre", 0.425);
        conversionMap.put("Gallons (US)", 1.0);
        conversionMap.put("Litres", 3.785);
        conversionMap.put("Nautical Miles", 1.0);
        conversionMap.put("Kilometres", 1.852);

        ArrayAdapter<CharSequence> categoryAd = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                R.layout.spinner_item
        );
        categoryAd.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                this,
                R.array.all_types,
                R.layout.spinner_item
        );
        ad.setDropDownViewResource(R.layout.spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAd);
        fromSpinner.setAdapter(ad);
        toSpinner.setAdapter(ad);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (categorySpinner.getSelectedItem().toString()) {
                    case "Currency":
                        arraySelect = R.array.currencies;
                        break;
                    case "Fuel efficiency":
                        arraySelect = R.array.fuel_eco;
                        break;
                    case "Volume":
                        arraySelect = R.array.volume;
                        break;
                    case "Distance":
                        arraySelect = R.array.distance;
                        break;
                    case "Temperature":
                        arraySelect = R.array.temperature;
                        break;
                    default:
                        arraySelect = R.array.all_types;
                        break;
                }
                ArrayAdapter<CharSequence> newAd = ArrayAdapter.createFromResource(
                        MainActivity.this,
                        arraySelect,
                        R.layout.spinner_item
                );
                newAd.setDropDownViewResource(R.layout.spinner_dropdown_item);
                fromSpinner.setAdapter(newAd);
                toSpinner.setAdapter(newAd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String initialUnit = fromSpinner.getSelectedItem().toString();
                String conversionUnit = toSpinner.getSelectedItem().toString();
                String result = convert(initialUnit, conversionUnit,
                        Double.parseDouble(input.getText().toString()),
                        categorySpinner.getSelectedItem().toString());
                output.setText(result);
                output.setVisibility(View.VISIBLE);

            }
        });
    }
}
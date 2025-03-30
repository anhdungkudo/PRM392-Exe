package vn.edu.fpt.bmiapp;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rgUnits;
    private RadioButton rbMetric, rbEnglish;
    private EditText etWeight, etHeight;
    private Button btnCalculate;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        rgUnits = findViewById(R.id.rgUnits);
        rbMetric = findViewById(R.id.rbMetric);
        rbEnglish = findViewById(R.id.rbEnglish);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        // Set onClick listener for the Calculate button
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = etWeight.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(MainActivity.this, "Please enter both weight and height", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Get the entered values
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            float weightInKg;
            float heightInMeters;

            // Check which unit is selected
            if (rbEnglish.isChecked()) {
                // English units: weight in pounds, height in inches
                // Convert weight (lbs) to kilograms: 1 lb = 0.453592 kg
                // Convert height (inches) to meters: 1 inch = 0.0254 m
                weightInKg = weight * 0.453592f;
                heightInMeters = height * 0.0254f;
            } else {
                // Metric units: weight in kg, height in centimeters
                // Convert height from cm to meters
                weightInKg = weight;
                heightInMeters = height / 100f;
            }

            // Calculate BMI: weight (kg) divided by height squared (m^2)
            float bmi = weightInKg / (heightInMeters * heightInMeters);

            // Determine BMI category
            String category;
            if (bmi < 18.5f) {
                category = "Underweight";
            } else if (bmi < 24.9f) {
                category = "Normal";
            } else if (bmi < 29.9f) {
                category = "Overweight";
            } else {
                category = "Obese";
            }

            // Format and display the result
            String result = String.format("Your BMI is: %.2f\nCategory: %s", bmi, category);
            tvResult.setText(result);

        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}

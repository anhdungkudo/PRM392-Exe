package vn.edu.fpt.mpgcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText etMilesDriven, etGallonsUsed;
    private TextView tvMpgResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMilesDriven = findViewById(R.id.etMilesDriven);
        etGallonsUsed = findViewById(R.id.etGallonsUsed);
        tvMpgResult = findViewById(R.id.tvMpgResult);

        // Cập nhật MPG khi nhập dữ liệu
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateMPG();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etMilesDriven.addTextChangedListener(textWatcher);
        etGallonsUsed.addTextChangedListener(textWatcher);
    }

    private void calculateMPG() {
        String milesDrivenStr = etMilesDriven.getText().toString();
        String gallonsUsedStr = etGallonsUsed.getText().toString();

        if (milesDrivenStr.isEmpty() || gallonsUsedStr.isEmpty()) {
            tvMpgResult.setText("MPG: -");
            return;
        }

        double milesDriven = Double.parseDouble(milesDrivenStr);
        double gallonsUsed = Double.parseDouble(gallonsUsedStr);

        if (gallonsUsed == 0) {
            tvMpgResult.setText("MPG: Invalid (Gallons cannot be 0)");
            return;
        }

        // Tính MPG
        double mpg = milesDriven / gallonsUsed;

        // Định dạng số
        DecimalFormat df = new DecimalFormat("#,###.##");
        tvMpgResult.setText("MPG: " + df.format(mpg));
    }
}

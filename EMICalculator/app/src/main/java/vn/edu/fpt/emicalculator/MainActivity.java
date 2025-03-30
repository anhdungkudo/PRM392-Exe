package vn.edu.fpt.emicalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText etLoanAmount, etInterestRate;
    private SeekBar sbLoanDuration;
    private TextView tvLoanDuration, tvEMIAmount;
    private int loanDuration = 1; // Default: 1 year

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLoanAmount = findViewById(R.id.etLoanAmount);
        etInterestRate = findViewById(R.id.etInterestRate);
        sbLoanDuration = findViewById(R.id.sbLoanDuration);
        tvLoanDuration = findViewById(R.id.tvLoanDuration);
        tvEMIAmount = findViewById(R.id.tvEMIAmount);

        // Set default loan duration text
        tvLoanDuration.setText("Loan Duration: " + loanDuration + " years");

        // Update EMI when SeekBar changes
        sbLoanDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                loanDuration = progress + 1; // Min 1 year
                tvLoanDuration.setText("Loan Duration: " + loanDuration + " years");
                calculateEMI();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Update EMI when input fields change
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateEMI();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etLoanAmount.addTextChangedListener(textWatcher);
        etInterestRate.addTextChangedListener(textWatcher);
    }

    private void calculateEMI() {
        String loanAmountStr = etLoanAmount.getText().toString();
        String interestRateStr = etInterestRate.getText().toString();

        if (loanAmountStr.isEmpty() || interestRateStr.isEmpty()) {
            tvEMIAmount.setText("Enter valid values");
            return;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double annualInterestRate = Double.parseDouble(interestRateStr);
        double monthlyInterestRate = annualInterestRate / (12 * 100);
        int totalMonths = loanDuration * 12;

        // Công thức tính EMI
        double emi = (loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths)) /
                (Math.pow(1 + monthlyInterestRate, totalMonths) - 1);

        // Định dạng hiển thị tiền tệ bằng dấu $
        DecimalFormat df = new DecimalFormat("$#,###.##");
        tvEMIAmount.setText("Monthly EMI: " + df.format(emi));
    }

}

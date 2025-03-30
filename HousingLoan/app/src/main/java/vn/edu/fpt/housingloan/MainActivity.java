package vn.edu.fpt.housingloan;

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
    private TextView tvLoanDuration, tvEMIAmount, tvAnnualInterest;
    private int[] loanDurations = {7, 14, 21}; // Chỉ các lựa chọn 7, 14, 21 năm
    private int selectedDuration = 7; // Mặc định là 7 năm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLoanAmount = findViewById(R.id.etLoanAmount);
        etInterestRate = findViewById(R.id.etInterestRate);
        sbLoanDuration = findViewById(R.id.sbLoanDuration);
        tvLoanDuration = findViewById(R.id.tvLoanDuration);
        tvEMIAmount = findViewById(R.id.tvEMIAmount);
        tvAnnualInterest = findViewById(R.id.tvAnnualInterest);

        // Hiển thị thời gian vay ban đầu
        tvLoanDuration.setText("Loan Duration: " + selectedDuration + " years");

        // SeekBar thay đổi loan duration
        sbLoanDuration.setMax(2); // Có 3 mức: 0, 1, 2 tương ứng với 7, 14, 21 năm
        sbLoanDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDuration = loanDurations[progress]; // Lấy giá trị tương ứng
                tvLoanDuration.setText("Loan Duration: " + selectedDuration + " years");
                calculateLoanDetails();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Cập nhật khi nhập dữ liệu vào EditText
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateLoanDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etLoanAmount.addTextChangedListener(textWatcher);
        etInterestRate.addTextChangedListener(textWatcher);
    }

    private void calculateLoanDetails() {
        String loanAmountStr = etLoanAmount.getText().toString();
        String interestRateStr = etInterestRate.getText().toString();

        if (loanAmountStr.isEmpty() || interestRateStr.isEmpty()) {
            tvEMIAmount.setText("Enter valid values");
            tvAnnualInterest.setText("");
            return;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double annualInterestRate = Double.parseDouble(interestRateStr);
        double monthlyInterestRate = annualInterestRate / (12 * 100);
        int totalMonths = selectedDuration * 12;

        // Công thức tính EMI
        double emi = (loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths)) /
                (Math.pow(1 + monthlyInterestRate, totalMonths) - 1);

        // Lãi suất hàng năm
        double annualInterest = loanAmount * (annualInterestRate / 100);

        // Định dạng số tiền
        DecimalFormat df = new DecimalFormat("$#,###.##");
        tvEMIAmount.setText("Monthly EMI: " + df.format(emi));
        tvAnnualInterest.setText("Annual Interest: " + df.format(annualInterest));
    }
}

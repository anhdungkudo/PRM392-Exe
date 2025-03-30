package vn.edu.fpt.stopwatchapp;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import android.text.TextUtils;

import android.widget.EditText;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Biến đồng hồ bấm giờ
    private TextView tvStopwatch;
    private Button btnStartStopwatch, btnStopStopwatch, btnResetStopwatch;
    private Handler stopwatchHandler = new Handler();
    private long stopwatchStartTime = 0;
    private boolean isStopwatchRunning = false;

    // Biến bộ đếm ngược
    private TextView tvCountdown;
    private EditText etCountdownInput;
    private Button btnStartCountdown, btnCancelCountdown;
    private CountDownTimer countDownTimer;
    // Thời gian đếm ngược mặc định (nếu không nhập) là 10 giây
    private long defaultCountdownTimeInMillis = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các view của đồng hồ bấm giờ
        tvStopwatch = findViewById(R.id.tvStopwatch);
        btnStartStopwatch = findViewById(R.id.btnStartStopwatch);
        btnStopStopwatch = findViewById(R.id.btnStopStopwatch);
        btnResetStopwatch = findViewById(R.id.btnResetStopwatch);

        // Ánh xạ các view của bộ đếm ngược
        tvCountdown = findViewById(R.id.tvCountdown);
        etCountdownInput = findViewById(R.id.etCountdownInput);
        btnStartCountdown = findViewById(R.id.btnStartCountdown);
        btnCancelCountdown = findViewById(R.id.btnCancelCountdown);

        // Sự kiện cho đồng hồ bấm giờ
        btnStartStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStopwatchRunning) {
                    stopwatchStartTime = System.currentTimeMillis();
                    stopwatchHandler.post(stopwatchRunnable);
                    isStopwatchRunning = true;
                }
            }
        });

        btnStopStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopwatchHandler.removeCallbacks(stopwatchRunnable);
                isStopwatchRunning = false;
            }
        });

        btnResetStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopwatchHandler.removeCallbacks(stopwatchRunnable);
                isStopwatchRunning = false;
                tvStopwatch.setText("00:00");
            }
        });

        // Sự kiện cho bộ đếm ngược
        btnStartCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ EditText
                String input = etCountdownInput.getText().toString();
                long countdownTime;
                if (TextUtils.isEmpty(input)) {
                    // Nếu không nhập, sử dụng giá trị mặc định
                    countdownTime = defaultCountdownTimeInMillis;
                    Toast.makeText(MainActivity.this, "Không nhập thời gian, sử dụng mặc định 10 giây", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int seconds = Integer.parseInt(input);
                        countdownTime = seconds * 1000; // chuyển đổi giây sang mili giây
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Giá trị không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                startCountdown(countdownTime);
            }
        });

        btnCancelCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    tvCountdown.setText("00:00");
                }
            }
        });
    }

    // Runnable cập nhật thời gian đồng hồ bấm giờ mỗi 500ms
    private Runnable stopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedMillis = System.currentTimeMillis() - stopwatchStartTime;
            int seconds = (int) (elapsedMillis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            String timeFormatted = String.format("%02d:%02d", minutes, seconds);
            tvStopwatch.setText(timeFormatted);
            stopwatchHandler.postDelayed(this, 500);
        }
    };

    // Phương thức khởi tạo bộ đếm ngược với thời gian đầu vào
    private void startCountdown(long countdownTimeInMillis) {
        // Hủy bỏ bộ đếm cũ nếu có
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(countdownTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                int minutes = secondsRemaining / 60;
                int seconds = secondsRemaining % 60;
                tvCountdown.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00");
            }
        }.start();
    }
}
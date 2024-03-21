package hieunnph32561.fpoly.asm_restapi_hieunnph32561.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import hieunnph32561.fpoly.asm_restapi_hieunnph32561.Acivity.MainActivity;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.R;

public class Hello extends AppCompatActivity {
    private boolean buttonClicked = false;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt biến để chỉ ra rằng nút đã được nhấn
                buttonClicked = true;
                Intent intent = new Intent(Hello.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra nếu nút chưa được nhấn thì chạy hàm
                if (!buttonClicked) {
                    Intent intent = new Intent(Hello.this, Login.class);
                    startActivity(intent);
                }
            }
        }, 2000);
    }

    }

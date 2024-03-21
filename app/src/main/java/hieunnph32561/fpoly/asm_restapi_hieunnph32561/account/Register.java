package hieunnph32561.fpoly.asm_restapi_hieunnph32561.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import hieunnph32561.fpoly.asm_restapi_hieunnph32561.Acivity.MainActivity;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.R;

public class Register extends AppCompatActivity {
    private TextView txtdk;
    private ImageView imgback;
    private EditText edmail, edpassword, repassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        edmail = findViewById(R.id.editTextEmailR);
        edpassword = findViewById(R.id.editTextPasswordR);
        repassword = findViewById(R.id.editreTextPasswordR);
        btnLogin = findViewById(R.id.buttonRegister);

        txtdk = findViewById(R.id.txtdangky);
        imgback = findViewById(R.id.imageViewBack);
        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String email = edmail.getText().toString().trim();
        String password = edpassword.getText().toString().trim();
        String repasswordd = repassword.getText().toString().trim();

        if (!password.equals(repasswordd)) { // Kiểm tra xác nhận mật khẩu
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem email và password có rỗng không
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký tài khoản bằng email và password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Register.this, Login.class));
                            // Đăng ký thành công, đăng nhập người dùng vào ứng dụng
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            // Thực hiện các hành động tiếp theo sau khi đăng ký thành công, ví dụ: chuyển hướng đến màn hình chính
                        } else {
                            // Đăng ký thất bại, hiển thị thông báo lỗi
                            Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package hieunnph32561.fpoly.asm_restapi_hieunnph32561.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import hieunnph32561.fpoly.asm_restapi_hieunnph32561.Acivity.MainActivity;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.R;

public class Register_Phone extends AppCompatActivity {
    private EditText editTextPhoneNumber, editTextOTP;
    private Button buttonSendOTP, buttonVerifyOTPP;
    private FirebaseAuth mAuth;
    private String verificationId;
    ImageView imgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        mAuth = FirebaseAuth.getInstance();

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextOTP = findViewById(R.id.editTextOTP);
        buttonSendOTP = findViewById(R.id.buttonSendOTP);
        buttonVerifyOTPP = findViewById(R.id.buttonVerifyOTP);
        imgback = findViewById(R.id.imageViewBack);

        buttonSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });

        buttonVerifyOTPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void sendOTP() {
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP() {
        String otp = editTextOTP.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(Register_Phone.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Register_Phone.this.verificationId = verificationId;
                    Toast.makeText(Register_Phone.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                }
            };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register_Phone.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            // Chuyển đến màn hình tiếp theo khi xác thực thành công
                            Intent intent = new Intent(Register_Phone.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Đóng màn hình hiện tại để ngăn người dùng quay lại bằng nút back
                        } else {
                            Toast.makeText(Register_Phone.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }

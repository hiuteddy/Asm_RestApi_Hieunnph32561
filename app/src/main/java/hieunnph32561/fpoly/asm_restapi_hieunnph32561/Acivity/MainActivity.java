package hieunnph32561.fpoly.asm_restapi_hieunnph32561.Acivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Import thư viện Log
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hieunnph32561.fpoly.asm_restapi_hieunnph32561.R;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.adapter.Student_Adapter;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.models.Student;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Đặt tag cho log

    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private Student_Adapter adapter;
    private List<Student> studentList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvview);
        button = findViewById(R.id.floatBtnAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        adapter = new Student_Adapter(MainActivity.this, studentList);
        recyclerView.setAdapter(adapter);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.230:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo ApiService
        apiService = retrofit.create(ApiService.class);
        getStudentList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogadd();
            }
        });
    }

    private void getStudentList() {
        // Gọi API để lấy danh sách sinh viên
        Call<List<Student>> call = apiService.getStudents();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    // Nếu nhận được dữ liệu thành công, cập nhật RecyclerView
                    studentList.clear();
                    studentList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    // Xử lý lỗi
                    Log.e(TAG, "Failed to get student list"); // Log lỗi ở đây
                    Toast.makeText(MainActivity.this, "Failed to get student list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e(TAG, "Network error: " + t.getMessage()); // Log lỗi ở đây
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Thêm phương thức để thêm sinh viên mới
    private void dialogadd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_add_student, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextNamea);
        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        EditText editTextMssv = dialogView.findViewById(R.id.editTextMssv);
        CheckBox checkBoxStatus = dialogView.findViewById(R.id.checkBoxStatus);


        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (TextUtils.isEmpty(editTextName.getText())
                        || TextUtils.isEmpty(editTextAge.getText())
                        || TextUtils.isEmpty(editTextMssv.getText())) {
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Kết thúc phương thức khi có trường dữ liệu bị bỏ trống
                }
                // Lấy dữ liệu từ EditText và CheckBox
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                String mssv = editTextMssv.getText().toString();
                boolean status = checkBoxStatus.isChecked();


                // Chuyển đổi tuổi từ chuỗi sang kiểu int


                // Gọi hàm thêm dữ liệu API
                addStudent(name, age, mssv, status);


            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Thêm sinh viên mới
    private void addStudent(String name, int age, String mssv, boolean status) {
        // Tạo một đối tượng Student mới từ dữ liệu đầu vào
        Student newStudent = new Student(name, age, mssv, status);


        // Gọi API để thêm sinh viên mới
        Call<Void> call = apiService.addStudent(newStudent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    getStudentList();
                    Toast.makeText(MainActivity.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi khi thêm sinh viên không thành công
                    Log.e(TAG, "Failed to add student: " + response.message());
                    Toast.makeText(MainActivity.this, "Thêm sinh viên không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

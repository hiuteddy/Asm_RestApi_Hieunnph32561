package hieunnph32561.fpoly.asm_restapi_hieunnph32561.adapter;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import hieunnph32561.fpoly.asm_restapi_hieunnph32561.R;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.models.Student;
import hieunnph32561.fpoly.asm_restapi_hieunnph32561.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Student_Adapter extends RecyclerView.Adapter<Student_Adapter.StudentViewHolder> {

    private Context context;
    private ApiService apiService;
    private List<Student> students;

    public Student_Adapter(Context context,List<Student> students) {
        this.students = students;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_student, parent, false);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.230:3000/") // Thay đổi URL base cho phù hợp với API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Khởi tạo ApiService từ Retrofit
        apiService = retrofit.create(ApiService.class);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.textViewName.setText(student.getName());
        holder.textViewTuoi.setText("" + String.valueOf(student.getTuoi()));
        holder.textViewmsv.setText("" + String.valueOf(student.getMssv()));
        holder.textViewtt.setText("" + (student.isTrangthaihoc() ? "Đã ra trường" :"Chưa ra trường"));



        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận xóa");
                    builder.setMessage("Bạn có chắc chắn muốn xóa sinh viên này?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = holder.getAdapterPosition();
                            Student student = students.get(position);
                            deleteStudent(student.get_id(), position);
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Đóng dialog
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

        });


        holder.imgupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Student student = students.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.item_update_student, null);
                builder.setView(dialogView);

                EditText editTextName = dialogView.findViewById(R.id.editTextName);
                EditText editTextTuoi = dialogView.findViewById(R.id.editTextTuoi);
                EditText editTextMasv = dialogView.findViewById(R.id.editTextMasv);
                CheckBox cbktt = dialogView.findViewById(R.id.cbktrangthai);
//                TextView txtadd= dialogView.findViewById(R.id.btn_update);

                editTextName.setText(student.getName());
                editTextTuoi.setText(String.valueOf(student.getTuoi()));
                editTextMasv.setText(student.getMssv());
                cbktt.setChecked(student.isTrangthaihoc());



                builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = editTextName.getText().toString();
                        String newTuoi = editTextTuoi.getText().toString();
                        String newMasv = editTextMasv.getText().toString();
                        boolean newTrangThai = cbktt.isChecked();

                        if (newName.isEmpty() || newName.isEmpty() || newMasv.isEmpty()) {
                            // Hiển thị thông báo lỗi nếu có trường dữ liệu trống
                            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return; // Không thêm sinh viên nếu có trường dữ liệu trống
                        }


                        // Pass the current student's ID to the update method
                        updateStudent(student.get_id(), newName, Integer.parseInt(newTuoi), newMasv, newTrangThai, position);
//                        Toast.makeText(context, "Cập nhật sinh viên", Toast.LENGTH_SHORT).show();
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
        });



    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewTuoi;
        public TextView textViewmsv;
        public TextView textViewtt;

        public ImageView imgdelete, imgupdate;
        // Các View khác và sự kiện nút sửa, xóa nếu cần

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTuoi = itemView.findViewById(R.id.textViewTuoi);
            textViewmsv = itemView.findViewById(R.id.textViewmsv);
            textViewtt = itemView.findViewById(R.id.textViewtt);

            imgdelete = itemView.findViewById(R.id.imgdelete);
            imgupdate = itemView.findViewById(R.id.imgupdate);

        }
    }

    private void deleteStudent(String studentId, int position) {
        Call<Void> call = apiService.deleteStudent(studentId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    students.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Log.e(TAG, "Failed to delete student");
                    Toast.makeText(context, "Failed to delete student", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error deleting student: " + t.getMessage());
                Toast.makeText(context, "Error deleting student", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateStudent(String studentId, String newName, int newTuoi, String newMssv, boolean newDaraTruong, int position) {
        Call<Void> call = apiService.updateStudent(studentId, new Student(newName, newTuoi, newMssv, newDaraTruong));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Update student data in the list
                    Student updatedStudent = students.get(position);
                    updatedStudent.setName(newName);
                    updatedStudent.setTuoi(newTuoi);
                    updatedStudent.setMssv(newMssv);
                    updatedStudent.setTrangthaihoc(newDaraTruong);

                    // Notify RecyclerView to update
                    notifyItemChanged(position);

                    Toast.makeText(context, "Failed to update student", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e(TAG, "Failed to update student");
                    Toast.makeText(context, "Failed to update student", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error updating student: " + t.getMessage());
                Toast.makeText(context, "Error updating student", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


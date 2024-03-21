package hieunnph32561.fpoly.asm_restapi_hieunnph32561.service;

import java.util.List;

import hieunnph32561.fpoly.asm_restapi_hieunnph32561.models.Student;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/")
        // Địa chỉ API để lấy danh sách sinh viên, thay đổi nếu cần
    Call<List<Student>> getStudents();
    @DELETE("/api/delete-student/{id}")
    Call<Void> deleteStudent(@Path("id") String id);

    @PUT("/api/update-student/{id}") // Địa chỉ API để cập nhật sinh viên
    Call<Void> updateStudent(@Path("id") String id, @Body Student student);

    @POST("/api/add-student") // Địa chỉ API để thêm sinh viên
    Call<Void> addStudent(@Body Student student);
}



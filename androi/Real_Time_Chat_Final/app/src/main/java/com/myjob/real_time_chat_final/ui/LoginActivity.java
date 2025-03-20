package com.myjob.real_time_chat_final.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.myjob.real_time_chat_final.api.RetrofitClient;
import com.myjob.real_time_chat_final.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static int userid = 0;
    private static final String TAG = "LoginActivity"; // Biến TAG để log
    private EditText usernameEditText, passwordEditText;
    private ImageButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        // Gọi API login
        RetrofitClient.getApiUserService().login(requestBody).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = (String) response.body().get("status");
                    String message = (String) response.body().get("message");

                    if ("success".equals(status)) {
                        int userId = ((Number) response.body().get("userId")).intValue();
                        userid = userId; // Lưu userId vào biến global
                        Intent intent = new Intent(LoginActivity.this, MessageListActivity.class);
                        intent.putExtra("USER_ID", userId); // Gửi userId sang MessListActivity
                        startActivity(intent);
                        finish(); // Kết thúc LoginActivity để không quay lại được
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công! ID: " + userId, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Lỗi đăng nhập: " + message);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Response thất bại: " + response.code() + " - " + response.message());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối API: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

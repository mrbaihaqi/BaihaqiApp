package com.example.baihaqiapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baihaqiapp.R;
import com.example.baihaqiapp.api.ApiClient;
import com.example.baihaqiapp.api.ApiService;
import com.example.baihaqiapp.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFormActivity extends AppCompatActivity {

    private EditText nameEditText, jobEditText;
    private Button saveButton;
    private boolean isEditMode = false;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        nameEditText = findViewById(R.id.nameEditText);
        jobEditText = findViewById(R.id.jobEditText);
        saveButton = findViewById(R.id.saveButton);

        if (getIntent().hasExtra("userId")) {
            isEditMode = true;
            userId = getIntent().getIntExtra("userId", -1);
            nameEditText.setText(getIntent().getStringExtra("userName"));
            jobEditText.setText(getIntent().getStringExtra("userJob"));
        }

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String job = jobEditText.getText().toString();

            if (name.isEmpty() || job.isEmpty()) {
                Toast.makeText(UserFormActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            if (isEditMode) {
                User user = new User();
                user.setFirstName(name.split(" ")[0]);
                user.setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
                user.setEmail(job);
                apiService.updateUser(userId, user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UserFormActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Set result to RESULT_OK
                            finish();
                        } else {
                            Toast.makeText(UserFormActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(UserFormActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                User user = new User();
                user.setFirstName(name.split(" ")[0]);
                user.setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
                user.setEmail(job);
                apiService.createUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UserFormActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Set result to RESULT_OK
                            finish();
                        } else {
                            Toast.makeText(UserFormActivity.this, "Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(UserFormActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

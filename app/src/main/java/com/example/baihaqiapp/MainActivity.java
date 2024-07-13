package com.example.baihaqiapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baihaqiapp.adapter.OnUserClickListener;
import com.example.baihaqiapp.adapter.UserAdapter;
import com.example.baihaqiapp.api.ApiClient;
import com.example.baihaqiapp.api.ApiService;
import com.example.baihaqiapp.api.ApiService.ResponseData;
import com.example.baihaqiapp.model.User;
import com.example.baihaqiapp.ui.UserFormActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private FloatingActionButton addButton;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserFormActivity.class);
            startActivityForResult(intent, 1);
        });

        loadData();
    }

    private void loadData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseData> call = apiService.getUsers();
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> userList = response.body().data;
                    Log.d(TAG, "Users loaded: " + userList);
                    userAdapter = new UserAdapter(userList, new OnUserClickListener() {
                        @Override
                        public void onEdit(User user) {
                            Intent intent = new Intent(MainActivity.this, UserFormActivity.class);
                            intent.putExtra("userId", user.getId());
                            intent.putExtra("userName", user.getFirstName() + " " + user.getLastName());
                            intent.putExtra("userJob", user.getEmail());
                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public void onDelete(int userId) {
                            apiService.deleteUser(userId).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
                                        loadData(); // Refresh data
                                    } else {
                                        Toast.makeText(MainActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    recyclerView.setAdapter(userAdapter);
                } else {
                    Log.e(TAG, "Failed to load users: " + response.errorBody());
                    Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e(TAG, "Error loading users", t);
                Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadData(); // Refresh data after adding or editing user
        }
    }
}

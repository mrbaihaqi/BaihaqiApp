# Membuat Aplikasi CRUD (Create, Read, Update, Delete) di Android Studio dengan penggunaan restful API
```
NAMA        : BAIHAQI ASA'ARI LUBIS
NIM         : 312210720
MATA KULIAH : PEMROGRAMAN MOBILE 2
```
# Pendahuluan
Tutorial ini akan membimbing Anda melalui proses pembuatan aplikasi Android dengan fungsi CRUD (Create, Read, Update, Delete) menggunakan Retrofit untuk berinteraksi dengan RESTful API. Kita akan menggunakan API dari reqres.in untuk contoh ini.

#Daftar Isi
```
	1. Struktur Proyek
	2. Membuat ApiService
	3. Membuat UserFormActivity
	4. Memperbarui MainActivity
	5. Memperbarui UserAdapter
	6. Menyusun Directory Project
```
#1. Struktur Proyek
Berikut adalah struktur folder dan file dalam proyek Android kita:
```
app/
├── java/
│   └── com/
│       └── example/
│           └── baihaqiapp/
│               ├── adapter/
│               │   └── UserAdapter.java
│               ├── api/
│               │   ├── ApiClient.java
│               │   └── ApiService.java
│               ├── model/
│               │   └── User.java
│               ├── ui/
│               │   └── UserFormActivity.java
│               ├── MainActivity.java
│               └── AndroidManifest.xml
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   └── activity_user_form.xml
│   ├── drawable/
│   │   ├── ic_edit.xml
│   │   └── ic_delete.xml
│   └── values/
│       └── strings.xml
└── build.gradle
```
#2. Membuat ApiService
Pertama, kita akan membuat `ApiService` yang akan mendefinisikan metode untuk berinteraksi dengan API RESTful. Pastikan Anda menambahkan dependensi Retrofit di file `build.gradle` Anda.
ApiService.java

    package com.example.baihaqiapp.api;

    import com.example.baihaqiapp.model.User;
    import java.util.List;
    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.DELETE;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.PUT;
    import retrofit2.http.Path;

    public interface ApiService {
      @GET("users")
      Call<ResponseData> getUsers();

      @PUT("users/{id}")
      Call<User> updateUser(@Path("id") int id, @Body User user);

      @POST("users")
      Call<User> createUser(@Body User user);

      @DELETE("users/{id}")
      Call<Void> deleteUser(@Path("id") int id);

      class ResponseData {
        public List<User> data;
      }
    }
    user.setEmail(job);
     apiService.updateUser(userId, user).enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
           Toast.makeText(UserFormActivity.this, "User updated", Toast.LENGTH_SHORT).show();
           setResult(RESULT_OK);
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
        setResult(RESULT_OK);
        finish();
      } else {
         Toast.makeText(UserFormActivity.this, "Create failed", Toast.LENGTH_SHORT).show();
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
#3. Membuat UserFormActivity
Selanjutnya, kita akan membuat `UserFormActivity` yang akan digunakan untuk menambah dan mengedit pengguna.
UserFormActivity.java
   
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
                            setResult(RESULT_OK);
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
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(UserFormActivity.this, "Create failed", Toast.LENGTH_SHORT).show();
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

4. Memperbarui MainActivity
Selanjutnya, kita akan memperbarui `MainActivity` untuk menampilkan daftar pengguna dan menangani aksi CRUD.
MainActivity.java
```
package com.example.baihaqiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baihaqiapp.adapter.UserAdapter;
import com.example.baihaqiapp.api.ApiClient;
import com.example.baihaqiapp.api.ApiService;
import com.example.baihaqiapp.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {
private RecyclerView recyclerView;
private UserAdapter userAdapter;
private Button addButton;

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

    fetchUsers();
}

private void fetchUsers() {
  ApiService apiService = ApiClient.getClient().create(ApiService.class);
  Call<ApiService.ResponseData> call = apiService.getUsers();
  call.enqueue(new Callback<ApiService.ResponseData>() {
    @Override
    public void onResponse(Call<ApiService.ResponseData> call, Response<ApiService.ResponseData> response) {
    if (response.isSuccessful()) {
    List<User> userList = response.body().data;
    userAdapter = new UserAdapter(userList, MainActivity.this);
    recyclerView.setAdapter(userAdapter);
    } else {
      Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
    }
}

@Override
public void onFailure(Call<ApiService.ResponseData> call, Throwable t) {
  Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
  }
  });
}

@Override
public void onUserClick(User user) {
  Intent intent = new Intent(MainActivity.this, UserFormActivity.class);
  intent.putExtra("userId", user.getId());
  intent.putExtra("userName", user.getFirstName() + " " + user.getLastName());
  intent.putExtra("userJob", user.getEmail());
  startActivityForResult(intent, 1);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (requestCode == 1 && resultCode == RESULT_OK) {
    fetchUsers();
    }
  }
}
```
#4. Memperbarui MainActivity
Selanjutnya, kita akan memperbarui `MainActivity` untuk menampilkan daftar pengguna dan menangani aksi CRUD.
MainActivity.java
```
package com.example.baihaqiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baihaqiapp.adapter.UserAdapter;
import com.example.baihaqiapp.api.ApiClient;
import com.example.baihaqiapp.api.ApiService;
import com.example.baihaqiapp.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button addButton;

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

        fetchUsers();
    }

    private void fetchUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ApiService.ResponseData> call = apiService.getUsers();
        call.enqueue(new Callback<ApiService.ResponseData>() {
            @Override
            public void onResponse(Call<ApiService.ResponseData> call, Response<ApiService.ResponseData> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body().data;
                    userAdapter = new UserAdapter(userList, MainActivity.this);
                    recyclerView.setAdapter(userAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ResponseData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(MainActivity.this, UserFormActivity.class);
        intent.putExtra("userId", user.getId());
        intent.putExtra("userName", user.getFirstName() + " " + user.getLastName());
        intent.putExtra("userJob", user.getEmail());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fetchUsers();
        }
    }
}
```
#Penutup
Dengan menyelesaikan tutorial ini, Anda telah berhasil membuat aplikasi Android dengan fungsi CRUD lengkap menggunakan Retrofit untuk berinteraksi dengan RESTful API. Anda dapat memperluas aplikasi ini dengan fitur tambahan seperti autentikasi pengguna, validasi input, dan masih banyak lagi.



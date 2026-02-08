package com.example.greeanstatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserWindowScreen extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private ImageView profileImage;
    private Button btnUpload, saveButton;
    private EditText nameEditText, notesEditText;
    private String deviceId;
    private DatabaseReference userRef;
    private Uri imageUri;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_window_screen);

        // Инициализация Firebase
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Получаем ID устройства
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = UUID.randomUUID().toString();
        }

        // Инициализация UI
        profileImage = findViewById(R.id.profile_image);
        btnUpload = findViewById(R.id.btn_upload);
        nameEditText = findViewById(R.id.nameEditText);
        notesEditText = findViewById(R.id.notesEditText);
        saveButton = findViewById(R.id.saveButton);

        // Загрузка данных пользователя
        loadUserData();

        // Обработчики кнопок
        btnUpload.setOnClickListener(v -> openImageChooser());
        saveButton.setOnClickListener(v -> saveUserData());
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .circleCrop()
                    .into(profileImage);
        }
    }

    private void saveUserData() {
        String name = nameEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Введите имя");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Сохранение данных...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Если есть новое изображение, сначала загружаем его
        if (imageUri != null) {
            uploadImage().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    saveUserToDatabase(name, notes, task.getResult(), progressDialog);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
                    Log.e("UploadImage", "Error", task.getException());
                }
            });
        } else {
            // Если нет нового изображения, сохраняем данные без него
            saveUserToDatabase(name, notes, null, progressDialog);
        }
    }

    private Task<String> uploadImage() {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("user_images/" + deviceId + ".jpg");

        return imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                })
                .continueWith(task -> task.getResult().toString());
    }

    private void saveUserToDatabase(String name, String notes, String imageUrl, ProgressDialog progressDialog) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("notes", notes);
        userData.put("lastUpdated", ServerValue.TIMESTAMP);

        // Если есть URL изображения, добавляем его в данные
        if (imageUrl != null) {
            userData.put("avatarUrl", imageUrl);
        }

        userRef.child(deviceId).setValue(userData)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(UserWindowScreen.this,
                                "Данные успешно сохранены!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserWindowScreen.this,
                                "Ошибка сохранения: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("SaveUser", "Error", task.getException());
                    }
                });
    }

    private void loadUserData() {
        userRef.child(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Загружаем имя и заметки
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String notes = dataSnapshot.child("notes").getValue(String.class);

                    if (name != null) nameEditText.setText(name);
                    if (notes != null) notesEditText.setText(notes);

                    // Загружаем аватар, если есть
                    String avatarUrl = dataSnapshot.child("avatarUrl").getValue(String.class);
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Glide.with(UserWindowScreen.this)
                                .load(avatarUrl)
                                .circleCrop()
                                .into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LoadUser", "Failed to load user data", databaseError.toException());
            }
        });
    }

    // Методы навигации
    public void startActivityCalculator(View v) {
        startActivity(new Intent(this, CalculatorScreen.class));
    }

    public void startActivityFood(View v) {
        startActivity(new Intent(this, FoodScreen.class));
    }

    public void startActivityMap(View v) {
        startActivity(new Intent(this, MapScreen.class));
    }

    public void startActivityHome(View v) {
        startActivity(new Intent(this, HomeScreen.class));
    }
}
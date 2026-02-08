package com.example.greeanstatus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Stack;

public class HomeScreen extends AppCompatActivity {

    private ImageView[] imgViews = new ImageView[8];
    private Stack<ImageView> colorImgElement = new Stack<>();
    private ImageView minusButton;
    private ImageView plusButton;

    private TextView moveText;

    private int WaterCount = 0;

    private  TextView numberMove;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Установка splash screen
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // Настройка отображения splash screen в течение 2 секунд
        boolean[] isAppReady = {false};
        new Handler(Looper.getMainLooper()).postDelayed(() -> isAppReady[0] = true, 2000);
        splashScreen.setKeepOnScreenCondition(() -> !isAppReady[0]);

        // Настройка оконных инсетов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Ищу айдишник изминения
            moveText = findViewById(R.id.number_information);

            numberMove = findViewById(R.id.move_number);

            return insets;
        });


        // Инициализация ImageView
        imgViews[0] = findViewById(R.id.cell_1);
        imgViews[1] = findViewById(R.id.cell_2);
        imgViews[2] = findViewById(R.id.cell_3);
        imgViews[3] = findViewById(R.id.cell_4);
        imgViews[4] = findViewById(R.id.cell_5);
        imgViews[5] = findViewById(R.id.cell_6);
        imgViews[6] = findViewById(R.id.cell_7);
        imgViews[7] = findViewById(R.id.cell_8);

        minusButton = findViewById(R.id.minus);
        plusButton = findViewById(R.id.plus);
    }

    // Методы для запуска активностей
    public void startActivityCalculator(View v) {
        startActivity(new Intent(this, CalculatorScreen.class));
    }

    public void startActivityFood(View v) {
        startActivity(new Intent(this, FoodScreen.class));
    }

    public void startActivityMap(View v) {
        startActivity(new Intent(this, MapScreen.class));
    }

    public void startActivityUser(View v) {
        startActivity(new Intent(this, UserWindowScreen.class));
    }

    // Логика добавления цветного изображения
    public void plusImg(View v) {
        if (colorImgElement.size() < 8) {

            WaterCount++;



            if(WaterCount < 4) {
                moveText.setTextColor(Color.parseColor("#EF4444"));
                numberMove.setTextColor(Color.parseColor("#EF4444"));
            } else if (WaterCount >= 4 && WaterCount < 7) {
                moveText.setTextColor(Color.parseColor("#EAB40C"));
                numberMove.setTextColor(Color.parseColor("#EAB40C"));
            } else {
                moveText.setTextColor(Color.parseColor("#4CAF50"));
                numberMove.setTextColor(Color.parseColor("#4CAF50"));
            }

            for (ImageView imgView : imgViews) {
                if (!colorImgElement.contains(imgView)) {
                    colorImgElement.push(imgView);
                    imgView.setImageResource(R.drawable.color_img);
                    break;
                }
            }

            numberMove.setText(String.valueOf(colorImgElement.size()));
        }
    }
    // Метод для удаления цветного изображения
    public void minusImg(View v) {
        if (!colorImgElement.isEmpty()) {

            WaterCount--;

            if(WaterCount <= 8 && WaterCount >= 7) {
                moveText.setTextColor(Color.parseColor("#4CAF50"));
                numberMove.setTextColor(Color.parseColor("#4CAF50"));
            } else if (WaterCount >= 4 && WaterCount < 7) {
                moveText.setTextColor(Color.parseColor("#EAB40C"));
                numberMove.setTextColor(Color.parseColor("#EAB40C"));
            } else {
                moveText.setTextColor(Color.parseColor("#EF4444"));
                numberMove.setTextColor(Color.parseColor("#EF4444"));
            }

            ImageView lastImg = colorImgElement.pop();
            lastImg.setImageResource(R.drawable.rectangle);

            numberMove.setText(String.valueOf(colorImgElement.size()));
        }
    }
}
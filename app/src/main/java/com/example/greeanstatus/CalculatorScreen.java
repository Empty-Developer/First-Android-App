package com.example.greeanstatus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorScreen extends AppCompatActivity {

    String[] genderItems = {"Мужчина", "Женщина"};
    String[] lifestyleItems = {"Сидячий", "Умеренно активный", "Активный", "Очень активный", "Профессиональный спорт"};

    AutoCompleteTextView genderAutoComplete, lifestyleAutoComplete;
    ArrayAdapter<String> genderAdapter, lifestyleAdapter;
    EditText weightInput, heightInput, ageInput;
    Button calculateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_screen);

        // Инициализация полей ввода
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        ageInput = findViewById(R.id.ageInput);
        calculateButton = findViewById(R.id.calculateButton);

        // Инициализация для выбора пола
        genderAutoComplete = findViewById(R.id.genderInput);
        genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                genderItems);
        genderAutoComplete.setAdapter(genderAdapter);
        genderAutoComplete.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedItem = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(CalculatorScreen.this,
                    "Пол: " + selectedItem,
                    Toast.LENGTH_SHORT).show();
        });

        // Инициализация для выбора образа жизни
        lifestyleAutoComplete = findViewById(R.id.lifestyleInput);
        lifestyleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                lifestyleItems);
        lifestyleAutoComplete.setAdapter(lifestyleAdapter);
        lifestyleAutoComplete.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedItem = adapterView.getItemAtPosition(position).toString();
            Toast.makeText(CalculatorScreen.this,
                    "Образ жизни: " + selectedItem,
                    Toast.LENGTH_SHORT).show();
        });

        // Обработчик кнопки расчета
        calculateButton.setOnClickListener(v -> calculateCalories());
    }

    private void calculateCalories() {
        // Проверка заполнения всех полей
        if (weightInput.getText().toString().isEmpty() ||
                heightInput.getText().toString().isEmpty() ||
                ageInput.getText().toString().isEmpty() ||
                genderAutoComplete.getText().toString().isEmpty() ||
                lifestyleAutoComplete.getText().toString().isEmpty()) {

            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Получение данных из полей ввода
            double weight = Double.parseDouble(weightInput.getText().toString());
            int height = Integer.parseInt(heightInput.getText().toString());
            int age = Integer.parseInt(ageInput.getText().toString());
            String gender = genderAutoComplete.getText().toString();
            String lifestyle = lifestyleAutoComplete.getText().toString();

            // Расчет базового метаболизма (BMR)
            double bmr;
            if (gender.equals("Мужчина")) {
                bmr = 88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age);
            } else {
                bmr = 447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age);
            }

            // Учет уровня активности
            double activityMultiplier = getActivityMultiplier(lifestyle);
            double dailyCalories = bmr * activityMultiplier;

            // Округление до целых
            int roundedCalories = (int) Math.round(dailyCalories);

            // Показ результата
            showResultDialog(roundedCalories);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
        }
    }

    private double getActivityMultiplier(String lifestyle) {
        switch (lifestyle) {
            case "Сидячий":
                return 1.2;
            case "Умеренно активный":
                return 1.375;
            case "Активный":
                return 1.55;
            case "Очень активный":
                return 1.725;
            case "Профессиональный спорт":
                return 1.9;
            default:
                return 1.2;
        }
    }

    private void showResultDialog(int calories) { 
        new AlertDialog.Builder(this)
                .setTitle("Результат расчета")
                .setMessage("Ваша рекомендуемая дневная норма калорий: " + calories + " ккал")
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    // Методы для запуска активностей
    public void startActivityHome(View v) {
        startActivity(new Intent(this, HomeScreen.class));
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
}
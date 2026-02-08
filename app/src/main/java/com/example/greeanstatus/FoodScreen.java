package com.example.greeanstatus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class FoodScreen extends AppCompatActivity {

    private LinearLayout cardsContainer;
    private SearchView searchView;
    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_screen);

        // Инициализация элементов интерфейса
        cardsContainer = findViewById(R.id.cards_container);
        searchView = findViewById(R.id.searchView);

        // Заполняем список продуктов
        initializeFoodItems();

        // Отображаем все карточки
        displayFoodItems(foodItems);

        // Настройка поиска
        setupSearchView();
    }

    private void initializeFoodItems() {
        foodItems.add(new FoodItem("Куринная грудка", "165 ккал", "P: 31г | C: 0г | F: 3.6г"));
        foodItems.add(new FoodItem("Гречневая каша", "343 ккал", "P: 13г | C: 72г | F: 3.4г"));
        foodItems.add(new FoodItem("Яблоко", "52 ккал", "P: 0.3г | C: 14г | F: 0.2г"));
        foodItems.add(new FoodItem("Овсяная каша", "389 ккал", "P: 17г | C: 66г | F: 7г"));
        foodItems.add(new FoodItem("Яйцо куриное", "155 ккал", "P: 13г | C: 1.1г | F: 11г"));
        foodItems.add(new FoodItem("Творог 5%", "121 ккал", "P: 17г | C: 1.8г | F: 5г"));
    }

    private void displayFoodItems(List<FoodItem> itemsToDisplay) {
        // Очищаем контейнер перед добавлением новых карточек
        cardsContainer.removeAllViews();

        for (FoodItem item : itemsToDisplay) {
            // Создаем карточку продукта
            View cardView = LayoutInflater.from(this).inflate(R.layout.food_card_layout, cardsContainer, false);

            // Находим элементы в карточке
            TextView nameTextView = cardView.findViewById(R.id.food_name);
            TextView caloriesTextView = cardView.findViewById(R.id.food_calories);
            TextView detailsTextView = cardView.findViewById(R.id.food_details);

            // Устанавливаем данные
            nameTextView.setText(item.getName());
            caloriesTextView.setText(item.getCalories());
            detailsTextView.setText(item.getDetails());

            // Добавляем карточку в контейнер
            cardsContainer.addView(cardView);
        }
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFoodItems(newText);
                return true;
            }
        });
    }

    private void filterFoodItems(String searchText) {
        List<FoodItem> filteredList = new ArrayList<>();

        if (searchText.isEmpty()) {
            // Если строка поиска пуста, показываем все элементы
            filteredList.addAll(foodItems);
        } else {
            // Ищем совпадения
            FoodItem matchedItem = null;

            for (FoodItem item : foodItems) {
                if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    // Если нашли точное совпадение, запоминаем его
                    if (item.getName().equalsIgnoreCase(searchText)) {
                        matchedItem = item;
                    } else {
                        // Добавляем частичные совпадения в список
                        filteredList.add(item);
                    }
                }
            }

            // Если нашли точное совпадение, ставим его первым в списке
            if (matchedItem != null) {
                filteredList.add(0, matchedItem);
            }
        }

        // Обновляем отображение
        displayFoodItems(filteredList);
    }

    // Методы для навигации (оставлю как есть)
    public void startActivityCalculator(View view) {
        Intent intent = new Intent(this, CalculatorScreen.class);
        startActivity(intent);
    }

    public void startActivityHome(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }

    public void startActivityMap(View view) {
        Intent intent = new Intent(this, MapScreen.class);
        startActivity(intent);
    }

    public void startActivityUser(View view) {
        Intent intent = new Intent(this, UserWindowScreen.class);
        startActivity(intent);
    }

    // Внутренний класс для хранения данных о продукте
    private static class FoodItem {
        private String name;
        private String calories;
        private String details;

        public FoodItem(String name, String calories, String details) {
            this.name = name;
            this.calories = calories;
            this.details = details;
        }

        public String getName() {
            return name;
        }

        public String getCalories() {
            return calories;
        }

        public String getDetails() {
            return details;
        }
    }
}
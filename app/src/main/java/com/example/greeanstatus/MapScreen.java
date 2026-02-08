package com.example.greeanstatus;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.greeanstatus.api.models.WeatherResponse;
import com.example.greeanstatus.api.services.WeatherApiService;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapScreen extends AppCompatActivity {

    private MapView mapView;
    private final String MAPKIT_API_KEY = "f60d7958-499d-4ba5-91a2-e91c5a457b9e";
    private static final String WEATHER_API_KEY = "1d684aadf9a164e0fabc51a956b9418d";
    private static final String CITY = "Krasnoyarsk";
    private static final String UNITS = "metric";
    private static final String LANGUAGE = "ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация MapKit перед setContentView
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.map_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapView = findViewById(R.id.map);
        setupMap();
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService service = retrofit.create(WeatherApiService.class);
        Call<WeatherResponse> call = service.getCurrentWeather(CITY, UNITS, WEATHER_API_KEY, LANGUAGE);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    showWeatherAlert(weather);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Ошибка запроса - можно показать уведомление или проигнорировать
            }
        });
    }

    private void showWeatherAlert(WeatherResponse weather) {
        double temp = weather.getMain().getTemp();
        String description = weather.getWeather()[0].getDescription();
        String city = weather.getName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Погода в " + city)
                .setMessage(String.format("%.1f°C, %s", temp, description))
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .create()
                .show();
    }

    private void setupMap() {
        Point point = new Point(56.01317250575379, 92.88871176680699);
        mapView.getMap().move(
                new CameraPosition(point, 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null
        );
        mapView.getMap().getMapObjects().addPlacemark(point);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    public void startActivityCalculator(View v) {
        startActivity(new Intent(this, CalculatorScreen.class));
    }

    public void startActivityFood(View v) {
        startActivity(new Intent(this, FoodScreen.class));
    }

    public void startActivityHome(View v) {
        startActivity(new Intent(this, HomeScreen.class));
    }

    public void startActivityUser(View v) {
        startActivity(new Intent(this, UserWindowScreen.class));
    }
}
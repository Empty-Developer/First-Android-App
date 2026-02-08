package com.example.greeanstatus.api.models;

public class WeatherResponse {
    private Main main;
    private Weather[] weather;
    private String name;

    public static class Main {
        private double temp;
        public double getTemp() { return temp; }
    }

    public static class Weather {
        private String description;
        public String getDescription() { return description; }
    }

    public Main getMain() { return main; }
    public Weather[] getWeather() { return weather; }
    public String getName() { return name; }
}
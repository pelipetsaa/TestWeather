package com.example.testapplication.model;

import java.util.List;

/**
 * Created by apelipets on 9/30/15.
 */
public class WeatherResponse {
    private Coord coord;
    private List<Weather> weather;
    private String base; //"cmc stations",
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private long dt; // 1443603600,
    private Sys sys;
    private int id; // 696050,
    private String name; //"Pushcha-Voditsa",
    private int cod; // 200

    public static class Coord{
        public double lon; // 30.5
        public double lat; // 50.45

        @Override
        public String toString() {
            return "{" +
                    "lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }
    }

    public static class Weather{
            public int id; // 803,
            public String main; // "Clouds",
            public String description; // "broken clouds",
            public String icon; //"04d"

        @Override
        public String toString() {
            return "{" +
                    "main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class Main{
            public double temp; //285.15
            public int pressure; //1030
            public int humidity; //66
            public double temp_min; //285.15
            public double temp_max; //285.15

        @Override
        public String toString() {
            return "{" +
                    "temp=" + temp +
                    ", pressure=" + pressure +
                    ", humidity=" + humidity +
                    ", temp_min=" + temp_min +
                    ", temp_max=" + temp_max +
                    '}';
        }
    }

    public static class Wind{
            public int speed; //7
            public int deg; //10

        @Override
        public String toString() {
            return "{" +
                    "speed=" + speed +
                    ", deg=" + deg +
                    '}';
        }
    }

    public static class Clouds{
            public int all; //75

        @Override
        public String toString() {
            return "{" +
                    "all=" + all +
                    '}';
        }
    }

    public static class Sys {
        public int type; // 1,
        public int id; // 7358,
        public double message; // 0.0078,
        public String country; // "UA",
        public long sunrise; // 1443585388,
        public long sunset; // 1443627516
    }

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    @Override
    public String toString() {
        return
                "coord=" + coord +
                "\nweather=" + weather +
                "\nbase=" + base +
                "\nmain=" + main +
                "\nwind=" + wind +
                "\nclouds=" + clouds +
                "\nname=" + name;
    }
}

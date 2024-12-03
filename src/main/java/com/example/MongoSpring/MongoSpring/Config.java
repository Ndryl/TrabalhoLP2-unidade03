package com.example.MongoSpring.MongoSpring;

public class Config {
    private static final String URI = "mongodb+srv://hendrylmarques120:1kmLFbSSx9pFL3Zy@cluster0.zqm0a.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    public static String getUri() {
        return URI;
    }
}

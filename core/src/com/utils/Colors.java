package com.utils;

public class Colors {
    public static int green = 0;
    public static int purple = 1;
    public static int yellow = 2;
    public static int orange = 3;

    public static int currentColor = 0;

    private static String[] colorList = {"green","purple","yellow","orange"};
    public static String getColor(int color) {
        if (color >= 4 || color < 0) return "none";
        else return colorList[color];
    }
}

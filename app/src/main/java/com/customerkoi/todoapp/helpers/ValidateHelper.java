package com.customerkoi.todoapp.helpers;

public class ValidateHelper {

    public static int validateStringToIntegerData(String data) {

        try {
            if (data != null && !data.equals("") && !data.equalsIgnoreCase("null")) {
                return Integer.valueOf(data);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }

    }

    public static String validateStringData(String data) {
        try {
            if (data != null && !data.equals("") && !data.equalsIgnoreCase("null")) {
                return data;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static int validateIntegerData(int data) {
        try {

        } catch (Exception e) {

        }
        return data;
    }

    public static double validateStringDoubleData(String data) {
        try {
            return Double.valueOf(data);
        } catch (Exception e) {
            return 0.0;
        }

    }

//    public static float validateStringToFloatData(String data) {
//
//        try {
//            if (data != null && !data.equals("") && !data.equalsIgnoreCase("null")) {
//                return Float.valueOf(data);
//            } else {
//                return 0.00;
//            }
//        } catch (Exception e) {
//            return 0;
//        }
//
//    }

}

package com.example.daniil.test_app;
//Вспомогательные функции для вычислений
public class SecondaryFunc {

    private SecondaryFunc() {
    }
    //Сумма элементов массива
    public static float sum(float[] array) {
        float value = 0.0f;
        for (int i = 0; i < array.length; i++) {
            value += array[i];
        }
        return value;
    }

    //ЕВклидова норма
    public static float norm(float[] array) {
        float value = 0;
        for (int i = 0; i < array.length; i++) {
            value += array[i] * array[i];
        }
        return (float) Math.sqrt(value);
    }

    // Скалярное произведение двух векторов
    public static float scalar_mult(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }


}
package com.example.daniil.test_app;

public class StepDetector {

    private static final int ACCEL_SIZE = 50;
    private static final int TIME_SERIES_SIZE = 10;
    private static final float STEP_DOWN = 8f;
    public static final float STEP_UP=14f;
    private int accelCounter = 0;
    private float[] accelX = new float[ACCEL_SIZE];
    private float[] accelY = new float[ACCEL_SIZE];
    private float[] accelZ = new float[ACCEL_SIZE];
    private int timeSeriesCounter = 0;
    private float[] timeSeries = new float[TIME_SERIES_SIZE];
    private float oldTimeSeries = 0;
    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    public void updateAccel(float currentAccel[], float linAccel[]) {
        int hold=3;
        // Первый шаг обновить предположение о том где находится глобальный вектор z
        accelCounter++;
        accelX[accelCounter % ACCEL_SIZE] = currentAccel[0];
        accelY[accelCounter % ACCEL_SIZE] = currentAccel[1];
        accelZ[accelCounter % ACCEL_SIZE] = currentAccel[2];

        float[] globalVectorZ = new float[3];
        globalVectorZ[0] = SecondaryFunc.sum(accelX) / Math.min(accelCounter, ACCEL_SIZE);
        globalVectorZ[1] = SecondaryFunc.sum(accelY) / Math.min(accelCounter, ACCEL_SIZE);
        globalVectorZ[2] = SecondaryFunc.sum(accelZ) / Math.min(accelCounter, ACCEL_SIZE);

        float norma = SecondaryFunc.norm(globalVectorZ);

        globalVectorZ[0] = globalVectorZ[0] / norma;
        globalVectorZ[1] = globalVectorZ[1] / norma;
        globalVectorZ[2] = globalVectorZ[2] / norma;

        //далее вычислим текущее ускорение в направлении гравитации и вычтем ее
        //ускорение пользователя относительно силы тяжести
        float currentZ = SecondaryFunc.scalar_mult(globalVectorZ, currentAccel) - norma;
        timeSeriesCounter++;
        timeSeries[timeSeriesCounter % TIME_SERIES_SIZE] = currentZ;

        float timeSeriesAssess = SecondaryFunc.sum(timeSeries);//сумма ускорений пользователя отностельно силы тяжести

        if ((timeSeriesAssess<STEP_UP) && (timeSeriesAssess > STEP_DOWN) && (oldTimeSeries <= STEP_DOWN)
                //дополнительное условие, что ускорение больше чем 3м/с2 по любой из осей.
                     && (Math.abs(linAccel[0])>=hold || Math.abs(linAccel[1])>=hold || Math.abs(linAccel[2])>=hold)) {
            listener.step();
        }
        oldTimeSeries = timeSeriesAssess;
    }
}
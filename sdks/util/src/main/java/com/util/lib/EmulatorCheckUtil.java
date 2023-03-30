package com.util.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by jiaokang on 2019-11-06.
 * <p>
 * source at
 * <a href="https://github.com/lamster2018/EasyProtector/blob/master/library/src/main/java/com/lahm/library/EmulatorCheckUtil.java">EmulatorCheckUtil</a>
 * <p>
 */
public class EmulatorCheckUtil {

    /**
     * 检测是否是模拟器
     *
     * @param context  上下文
     * @param callback 回调
     * @return
     */
    public static boolean checkIsRunningInEmulator(Context context,
                                                   EmulatorCheckCallback callback) {
        return readSysProperty(context, callback);
    }


    public static boolean readSysProperty(Context context, EmulatorCheckCallback callback) {
        if (strongCheckIsEmulator()) {
            return true;
        }

        int suspectCount = 0;

        String baseBandVersion = getProperty("gsm.version.baseband");
        if (baseBandVersion != null && baseBandVersion.contains("1.0.0.0"))
            ++suspectCount;

        String buildFlavor = getProperty("ro.build.flavor");
        if (buildFlavor != null && (buildFlavor.contains("vbox") || buildFlavor.contains(
                "sdk_gphone")))
            ++suspectCount;

        String productBoard = getProperty("ro.product.board");
        if (productBoard != null && (productBoard.contains("android") || productBoard.contains(
                "goldfish")))
            ++suspectCount;

        String boardPlatform = getProperty("ro.board.platform");
        if (boardPlatform != null && boardPlatform.contains("android"))
            ++suspectCount;

        String hardWare = getProperty("ro.hardware");
        if (null == hardWare) ++suspectCount;
        else if (hardWare.toLowerCase().contains("ttvm")) suspectCount += 10;
        else if (hardWare.toLowerCase().contains("nox")) suspectCount += 10;

        String sensorNum = "sensorNum";
        if (context != null) {
            SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            int sensorSize = sm.getSensorList(Sensor.TYPE_ALL).size();
            if (sensorSize < 7) ++suspectCount;
            sensorNum = sensorNum + sensorSize;
        }

        if (callback != null) {
            StringBuilder stringBuffer = new StringBuilder("ceshi start|")
                    .append(baseBandVersion).append("|")
                    .append(buildFlavor).append("|")
                    .append(productBoard).append("|")
                    .append(boardPlatform).append("|")
                    .append(hardWare).append("|")
                    .append(sensorNum).append("|")
                    .append("end");
            callback.findEmulator(stringBuffer.toString());
        }

        return suspectCount > 3;
    }

    private static int getUserAppNum(String userApps) {
        if (TextUtils.isEmpty(userApps)) return 0;
        String[] result = userApps.split("package:");
        return result.length;
    }

    private static String getProperty(String propName) {
        String value = null;
        Object roSecureObj;
        try {
            roSecureObj = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class)
                    .invoke(null, propName);
            if (roSecureObj != null) value = (String) roSecureObj;
        } catch (Exception e) {
            value = null;
        }
        return TextUtils.isEmpty(value) ? null : value;
    }

    private static boolean strongCheckIsEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || "sdk".equals(Build.PRODUCT);
    }

    public interface EmulatorCheckCallback {
        void findEmulator(String emulatorInfo);
    }
}

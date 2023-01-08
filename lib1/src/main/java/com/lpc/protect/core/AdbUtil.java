package com.lpc.protect.core;

import java.io.IOException;

public class AdbUtil {
    public static void checkDeviceIsConn(){
        try {
            Runtime.getRuntime().exec("adb device");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

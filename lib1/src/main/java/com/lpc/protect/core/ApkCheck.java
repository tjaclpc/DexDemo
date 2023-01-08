package com.lpc.protect.core;



public class ApkCheck {
    private static ApkCheck instance = new ApkCheck();

    public static ApkCheck getInstance() {
        return instance;
    }

    private ApkCheck() {
    }

    public boolean getUninstallApkInfo( String filePath) {
        AdbUtil.checkDeviceIsConn();
        return false;
    }

    /**
     * 创建虚拟机，工程太浩大，直接启用真机
     * @param context
     * @param filePath
     * @return
     */
//    public boolean getUninstallApkInfo(Context context, String filePath) {
//        boolean result = false;
//        try {
//            PackageManager pm = context.getPackageManager();
//            Log.e("archiveFilePath", filePath);
//            PackageInfo info = pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
//            if (info != null) {
//                result = true;//完整
//            }
//        } catch (Exception e) {
//            result = false;//不完整
//        }
//        return result;
//    }
}

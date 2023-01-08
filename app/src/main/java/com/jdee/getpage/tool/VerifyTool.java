package com.jdee.getpage.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.util.Log;

import com.jdee.getpage.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class VerifyTool {
    private static String TAG = "VerifyTool";
    /*校验Dex CRC值*/
    private void verifyDex(Context context,int crc_value){
        // 获取String.xml中的value, 实践中应该联网获取用于比对的CRC值
        Long dexCrc = Long.parseLong(context.getString(crc_value));
        String apkPath = context.getPackageCodePath();
        Log.d(TAG, "verifyDex: PackageCodePath: " + apkPath);
        try {
            ZipFile zipFile = new ZipFile(apkPath);
            ZipEntry dexEntry = zipFile.getEntry("classes.dex");
            // 计算classes.dex的crc
            long dexEntryCrc = dexEntry.getCrc();
            Log.d(TAG, "verifyDex: dexEntryCrc: "+ dexEntryCrc);
            if(dexCrc == dexEntryCrc){
                Log.d(TAG, "dex has not been modified'");
            }else{
                Log.d(TAG, "dex has been modified");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*校验APK MD5值
     *与dex校验不同， apk检验必须把计算好的Hash值放到网络服务器, 因为对APK的任何改动都会影响到最后的Hash值
     * */
    private void verifyApk(Context context) {
        // /data/app/com.example.myapplication-1/base.apk
        String apkPath = context.getPackageCodePath();
        MessageDigest msgDigest;
        try {
            // 获取apk并计算MD5值
            msgDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[4096];
            int count;
            FileInputStream fis = new FileInputStream(new File(apkPath));
            while ((count = fis.read(bytes)) > 0) {
                msgDigest.update(bytes, 0, count);
            }
            // 计算出MD5值
            BigInteger bInt = new BigInteger(1, msgDigest.digest());
            String md5 = bInt.toString(16);
            fis.close();
            Log.d(TAG, "verifyApk: md5: " + md5);
            // 与服务端的MD5值进行对比
            // code ....
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*获取签名的Md5*/
    public static void verifySignature(Context context){
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        String md5 = "";
        try{
            pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] s = pi.signatures;
            // 计算出MD5值
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.reset();
            msgDigest.update(s[0].toByteArray());
            BigInteger bInt = new BigInteger(1, msgDigest.digest());
            md5 = bInt.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "verifySignature: md5:" + md5);
        // 与服务端的MD5值进行对比
        // code ....
    }

    // 拷贝assets/文件 到 app/files/ 下
    private String copyDex(Context context,String dexName) {
        // 获取assets目录管理器
        AssetManager as = context.getAssets();
        // 目的地址
        String path = context.getFilesDir() + File.separator + dexName;
        // /data/user/0/com.example.myapplication/files/mytestclass.dex
        Log.d(TAG, "copyDex: path: " + path);
        // 将文件拷贝到目的地址
        try {
            // 创建文件流
            FileOutputStream out = new FileOutputStream(path);
            // 打开文件 assets/dexName
            InputStream is = as.open(dexName);
            // 循环读取并写入文件
            byte[] buffer = new byte[1024];
            int len =0;
            while((len = is.read(buffer))!= -1){
                out.write(buffer, 0, len);
            }
            // 关闭文件
            out.close();
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
        return path;
    }
}

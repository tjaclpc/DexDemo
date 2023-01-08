package com.jdee.getpage.classinfo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;


import com.jdee.getpage.MainActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ClsManager {
    private static ClsManager instance = new ClsManager();
    public static ClsManager getInstance() {
        return instance;
    }



    public  void test_ClassLoader(Activity activity){
        ClassLoader mainActivityClassloader = MainActivity.class.getClassLoader();
        Log.v("wanbai", "MainActivity的类加载加载器:" + "hashcode:" + mainActivityClassloader.hashCode() + " classLoader:" + mainActivityClassloader);
        ClassLoader contextClassloader = Context.class.getClassLoader();
        Log.v("wanbai", "Context的类加载加载器:" + " hashcode:" + contextClassloader.hashCode() + " classLoader:" + contextClassloader);
        ClassLoader listViewClassLoader = ListView.class.getClassLoader();
        Log.v("wanbai", "ListView的类加载器:" + " hashcode:" + listViewClassLoader.hashCode() + " classLoader:" + listViewClassLoader);
        ClassLoader activityContextImplClassloader = activity.getClassLoader();
        Log.v("wanbai", "应用程序默认加载器:" + " hashcode:" + activityContextImplClassloader.hashCode() + " classLoader:" + activityContextImplClassloader);
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Log.v("wanbai", "系统类加载器:" + " hashcode:" + systemClassLoader.hashCode() + " classLoader:" + systemClassLoader);

        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        Log.v("wanbai", "当前线程的classloader:" + " hashcode:" + threadClassLoader.hashCode() + " classLoader:" + threadClassLoader);

        Log.v("wanbai", "打印应用程序默认加载器的委派机制:");
        ClassLoader classLoader = activityContextImplClassloader;
        while (classLoader != null) {
            Log.v("wanbai", "-------------- hashcode:" + classLoader.hashCode() + " classLoader:" + classLoader);
            classLoader = classLoader.getParent();
        }

        Log.v("wanbai", "打印系统加载器的委派机制:");
        classLoader = systemClassLoader;
        while (classLoader != null) {
            Log.v("wanbai", "-------------- hashcode:" + classLoader.hashCode() + " classLoader:" + classLoader);
            classLoader = classLoader.getParent();
        }

    }

    public void testDexClassLoader(Context context, String dexfilepath) {
        //构建文件路径：/data/data/com.jdee.getpage/app_opt_dex，存放优化后的dex,lib库
        File optfile = context.getDir("opt_dex",0);
        System.out.println(optfile.getAbsoluteFile()+"--"+optfile.canRead()+
                "--"+optfile.canWrite()+"--"+optfile.listFiles().length);
        File libfile = context.getDir("lib_dex",0);
        System.out.println(libfile.getAbsolutePath()+"--"+libfile.canRead()+
                "--"+libfile.canWrite()+"--"+libfile.listFiles().length);
        ClassLoader parentclassloader = Thread.currentThread().getContextClassLoader();
        ClassLoader tmpclassloader = context.getClassLoader();
        //可以为DexClassLoader指定父类加载器
        DexClassLoader dexClassLoader = null;
        try {
            dexClassLoader = new DexClassLoader(dexfilepath,optfile.getAbsolutePath(),libfile.getAbsolutePath(),parentclassloader);
//            dexClassLoader = new DexClassLoader(dexfilepath,null,null,parentclassloader);
            getClassListInClassLoader(dexClassLoader);
        }catch (Exception e){
            e.printStackTrace();
        }
        Class clazz = null;
        try {
            clazz = dexClassLoader.loadClass("com.jdee.getpage.classinfo.Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(clazz!=null){
            try {
                Method testFuncMethod = clazz.getDeclaredMethod("test02");
                Object obj = clazz.newInstance();
                testFuncMethod.invoke(obj);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }


    public static void getClassListInClassLoader(ClassLoader classLoader){
        //先拿到BaseDexClassLoader
        try {
            //拿到pathList
            Class BaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = BaseDexClassLoader.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object pathListObj = pathListField.get(classLoader);

            //拿到dexElements
            Class DexElementClass = Class.forName("dalvik.system.DexPathList");
            Field DexElementFiled = DexElementClass.getDeclaredField("dexElements");
            DexElementFiled.setAccessible(true);
            Object[]  dexElementObj = (Object[]) DexElementFiled.get(pathListObj);
            //拿到dexFile
            Class Element = Class.forName("dalvik.system.DexPathList$Element");
            Field dexFileField = Element.getDeclaredField("dexFile");
            dexFileField.setAccessible(true);
            Class DexFile =Class.forName("dalvik.system.DexFile");
            Field mCookieField = DexFile.getDeclaredField("mCookie");
            mCookieField.setAccessible(true);
            Field mFiledNameField = DexFile.getDeclaredField("mFileName");
            mFiledNameField.setAccessible(true);
            //拿到getClassNameList
            Method getClassNameListMethod = DexFile.getDeclaredMethod("getClassNameList",Object.class);
            getClassNameListMethod.setAccessible(true);

            for(Object dexElement:dexElementObj){
                Object dexfileObj = dexFileField.get(dexElement);
                Object mCookiedobj = mCookieField.get(dexfileObj);
                String mFileNameobj = (String) mFiledNameField.get(dexfileObj);
                String[] classlist = (String[]) getClassNameListMethod.invoke(null,mCookiedobj);
                for(String classname:classlist){
                    Log.e("classlist",classLoader.toString()+"-----"+mFileNameobj+"-----"+classname);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

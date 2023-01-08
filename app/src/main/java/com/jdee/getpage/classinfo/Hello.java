package com.jdee.getpage.classinfo;

public class Hello {
    //通过对smali代码进行乱序来干扰代码逆向
    public static void main(String[] argc){
        // 字符串的声明和定义
        String a="1";
        String b="2";
        // 字符串拼接
        String c = a+b;
        // 打印字符串
        System.out.println(c);
    }
}

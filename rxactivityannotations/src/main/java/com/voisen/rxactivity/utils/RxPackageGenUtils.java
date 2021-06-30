package com.voisen.rxactivity.utils;

public class RxPackageGenUtils {

    /**
     * 获取类名称
     * @param path path
     * @return class name
     */
    public static String getClassName(String path){
        StringBuilder builder = new StringBuilder();
        builder.append("_Rx");
        String[] strings = path.split("/");
        for (String s : strings) {
            if (s != null && s.length() > 0){
                String s1 = s.substring(0, 1);
                builder.append(s1.toUpperCase());
                builder.append(s.substring(1));
            }
        }
        return builder.toString();
    }

}

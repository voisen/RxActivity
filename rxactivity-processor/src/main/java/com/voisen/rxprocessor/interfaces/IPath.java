package com.voisen.rxprocessor.interfaces;
import java.lang.reflect.Field;


public class IPath {

    private static IPath iPath;

    public static String IMPL_CLASS = "IPathImpl";



    static {
        try {
            @SuppressWarnings("unchecked")
            Class<IPath> aClass = (Class<IPath>) Class.forName(IPath.class.getPackage() + "." + IMPL_CLASS);
            iPath = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getActivityClass(String path){
        if (iPath == null){
            return null;
        }
        return iPath.getClassByPath(path);
    }

    private Class<?> getClassByPath(String path){
        String s = "_" + path.hashCode();
        try {
            Field field = this.getClass().getDeclaredField(s);
            field.setAccessible(true);
            return (Class<?>) field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

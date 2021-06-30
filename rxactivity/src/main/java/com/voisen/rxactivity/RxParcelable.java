package com.voisen.rxactivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.voisen.rxactivity.utils.RxBundleUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface RxParcelable extends Parcelable {
    String TAG = "ParcelableHelper";
    //数据存储
    Creator<?> CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            try{
                String s = source.readString();
                Class<?> aClass = Class.forName(s);
                Object instance = aClass.newInstance();
                ArrayList<String> parcelableFields = new ArrayList<>();
                source.readStringList(parcelableFields);
                for (String parcelableField : parcelableFields) {
                    try {
                        Field field = aClass.getDeclaredField(parcelableField);
                        Class<?> type = field.getType();
                        Object o = source.readValue(type.getClassLoader());
                        field.set(instance, o);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return instance;
            }catch (ClassNotFoundException | IllegalAccessException | InstantiationException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    @Override
    default int describeContents() {
        return 0;
    }

    @Override
    default void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getClass().getName());
        Field[] fields = this.getClass().getDeclaredFields();
        List<String> filedNames = new ArrayList<>();
        List<Object> valuesList = new ArrayList<>();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                boolean type = RxBundleUtils.isSupportType(value);
                if (type){
                    filedNames.add(field.getName());
                    valuesList.add(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "writeToParcel: 写入: "+ filedNames);
        dest.writeStringList(filedNames);
        for (Object value : valuesList) {
            dest.writeValue(value);
        }
    }
}

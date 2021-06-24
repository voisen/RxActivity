package com.voisen.rxactivity.utils;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class BundleUtils {

    public static void putObject(Bundle bundle , @Nullable String key, @Nullable Object value) {
        if (bundle == null) return;
        if (value == null) {
            bundle.putString(key, null);
        }else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Size) {
            bundle.putSize(key, (Size) value);
        } else if (value instanceof SizeF) {
            bundle.putSizeF(key, (SizeF) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof ArrayList) {
            bundle.putParcelableArrayList(key, (ArrayList) value);
        } else if (value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Binder) {
            bundle.putBinder(key, (Binder) value);
        } else if (value instanceof IBinder) {
            bundle.putBinder(key, (IBinder) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (Double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else {
            throw new IllegalArgumentException("[" + key + "]Unsupported type " + value.getClass());
        }
    }

    public static boolean isSupportType(Object value){
        if (value == null) {
            return true;
        }else if (value instanceof Byte) {
            return true;
        } else if (value instanceof Character) {
            return true;
        } else if (value instanceof Short) {
            return true;
        } else if (value instanceof Float) {
            return true;
        } else if (value instanceof CharSequence) {
            return true;
        } else if (value instanceof Parcelable) {
            return true;
        } else if (value instanceof Size) {
            return true;
        } else if (value instanceof SizeF) {
            return true;
        } else if (value instanceof Parcelable[]) {
            return true;
        } else if (value instanceof ArrayList) {
            return true;
        } else if (value instanceof SparseArray) {
            return true;
        } else if (value instanceof Serializable) {
            return true;
        } else if (value instanceof byte[]) {
            return true;
        } else if (value instanceof short[]) {
            return true;
        } else if (value instanceof char[]) {
            return true;
        } else if (value instanceof float[]) {
            return true;
        } else if (value instanceof CharSequence[]) {
            return true;
        } else if (value instanceof Bundle) {
            return true;
        } else if (value instanceof Binder) {
            return true;
        } else if (value instanceof IBinder) {
            return true;
        } else if (value instanceof Boolean) {
            return true;
        } else if (value instanceof Integer) {
            return true;
        } else if (value instanceof Long) {
            return true;
        } else if (value instanceof Double) {
            return true;
        } else if (value instanceof String) {
            return true;
        } else if (value instanceof boolean[]) {
            return true;
        } else if (value instanceof int[]) {
            return true;
        } else if (value instanceof long[]) {
            return true;
        } else if (value instanceof double[]) {
            return true;
        } else if (value instanceof String[]) {
            return true;
        }
        return false;
    }

}

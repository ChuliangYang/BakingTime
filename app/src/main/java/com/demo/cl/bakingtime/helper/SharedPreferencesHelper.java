package com.demo.cl.bakingtime.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by CL on 9/24/17.
 */

public class SharedPreferencesHelper {

    public static void saveObject(Context context, String preferenceName, String key, List<?> object) throws Exception {
        if(object instanceof Serializable) {
            Timber.w("object can save,saving...");
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
//                ObjectOutputStream oos = new ObjectOutputStream(baos);
//                oos.writeObject(object);//把对象写到流里
//                String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
                editor.putString(key, listToString(object));
                editor.commit();
                Timber.w("object is saved");
            } catch (IOException e) {
                e.printStackTrace();
                Timber.w("iO error");
            }
        }else {
            throw new Exception("Object must implements Serializable");
        }
    }

    public static List<?> getObject(Context context, String preferenceName,String key) {
        Timber.w("start getting object");
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        String temp = sharedPreferences.getString(key, "");
        try {
            return StringToList(temp);
        } catch (IOException e) {
            e.printStackTrace();
            Timber.w("error");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Timber.w("error");
        }
//        ByteArrayInputStream bais =  new ByteArrayInputStream(temp.getBytes());
//        Object object = null;
//        try {
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            object = (Object) ois.readObject();
//            Timber.w("object done read");
//        } catch (IOException e) {
//            Timber.w("iO error");
//        }catch(ClassNotFoundException e1) {
//            Timber.w("ClassNotFoundException");
//        }
//        return object;

        return null;

    }


    public static String listToString(List<?> list)throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(list);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String listString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return listString;
    }

    @SuppressWarnings("unchecked")
    public static List<?> StringToList(String listString) throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(listString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<?> WeatherList = (List<?>) objectInputStream
                .readObject();
        objectInputStream.close();
        return WeatherList;
    }

    public static void saveKeyValue(Context context, String preferenceName, String key, Object object){
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }


    public static Object getValueByKey(Context context, String preferenceName,String key,Object object){
        SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceName,context.MODE_PRIVATE);
        if (object instanceof String) {
            return sharedPreferences.getString(key, (String) object);
        } else if (object instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) object);
        } else if (object instanceof Long) {
            return sharedPreferences.getLong(key, (Long) object);
        } else {
            return sharedPreferences.getString(key, object.toString());
        }
    }



}

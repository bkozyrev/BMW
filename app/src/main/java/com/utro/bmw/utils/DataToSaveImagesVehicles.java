package com.utro.bmw.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataToSaveImagesVehicles implements Parcelable{
    LinkedHashMap<String, Bitmap> map;

    public DataToSaveImagesVehicles(LinkedHashMap<String, Bitmap> map){
        this.map = map;
    }

    public LinkedHashMap<String, Bitmap> getMap(){
        return map;
    }

    public DataToSaveImagesVehicles(Parcel in) {
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            Bitmap value = in.readParcelable(Bitmap.class.getClassLoader());
            map.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(map.size());
        for(Map.Entry<String, Bitmap> entry : map.entrySet()){
            parcel.writeString(entry.getKey());
            parcel.writeValue(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public DataToSaveImagesVehicles createFromParcel(Parcel in) {
            return new DataToSaveImagesVehicles(in);
        }

        @Override
        public DataToSaveImagesVehicles[] newArray(int size) {
            return new DataToSaveImagesVehicles[size];
        }
    };
}

package co.yoprice.tyrunt.utils;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;

/**
 * Created by Charl on 3/27/2016.
 */
public class PhoneUtils {
    public static class DeviceInfo {
        public String device, model, version;

        private DeviceInfo() {
        }

        public static DeviceInfo Builder() {
            return new DeviceInfo();
        }

        public String getDevice() {
            return device;
        }

        public DeviceInfo setDevice(String device) {
            this.device = device;
            return this;
        }

        public String getModel() {
            return model;
        }

        public DeviceInfo setModel(String model) {
            this.model = model;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public DeviceInfo setVersion(String version) {
            this.version = version;
            return this;
        }

        @Override
        public String toString() {
            return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this, getClass());
        }
    }

    public static DeviceInfo getDevice() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String version = getVersion().getVersion() + " " + Build.VERSION.RELEASE;

        if (model.startsWith(manufacturer)) {
            return DeviceInfo.Builder().setModel(model).setDevice(capitalize(manufacturer)).setVersion(version);
        }
        return DeviceInfo.Builder().setModel(model).setDevice(capitalize(manufacturer)).setVersion(version);
    }

    enum ANDROID_VERSIONS {
        NA("UNDEFINED",0),
        BASE("BASE",1),
        BASE_1_1("BASE",2),
        CUPCAKE("CUPCAKE",3),
        DONUT("DONUT",4),
        ECLAIR("ECLIAR",5),
        ECLAIR_0_1("ECLAIR",6),
        ECLAIR_MR1("ECLAIR",7),
        FROYO("FROYO",8),
        GINGERBREAD("GINGERBREAD",9),
        GINGERBREAD_MR1("GINGERBREAD",10),
        HONEYCOMB("HONEYCOMB",11),
        HONEYCOMB_MR1("HONEYCOMB",12),
        HONEYCOMB_MR2("HONEYCOMB",13),
        ICE_CREAM_SANDWICH("ICE CREAM SANDWICH",14),
        ICE_CREAM_SANDWICH_MR1("ICE CREAM SANDWICH",15),
        JELLY_BEAN("JELLY BEAN",16),
        JELLY_BEAN_MR1("JELLY BEAN",17),
        JELLY_BEAN_MR2("JELLY BEAN",18),
        KITKAT("KITKAT",19),
        KITKAT_WATCH("KITKAT WATCH",20),
        LOLLIPOP("LOLLIPOP",21),
        LOLLIPOP_MR1("LOLLIPOP",22),
        M("MARSHMALLOW",23);

        public static ANDROID_VERSIONS findVersion(int code){
            for(ANDROID_VERSIONS v : ANDROID_VERSIONS.values()){
                if(v.getCode() == code) return v;
            }
            return ANDROID_VERSIONS.NA;
        }

        public int getCode() {
            return code;
        }

        public String getVersion() {
            return version;
        }

        public static ANDROID_VERSIONS findVersion(float code){
            int i = Float.valueOf(code).intValue();
            for(ANDROID_VERSIONS v : ANDROID_VERSIONS.values()){
                if(v.getCode() == i) return v;
            }
            return ANDROID_VERSIONS.NA;
        }

        private final String version;
        private final int code;
        ANDROID_VERSIONS(String version, int code){
            this.version = version;
            this.code = code;
        }
    };

    public static ANDROID_VERSIONS getVersion() {
        return ANDROID_VERSIONS.findVersion(Build.VERSION.SDK_INT);
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
}

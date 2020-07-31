package com.example.groupproject;

import android.os.Parcel;
import android.os.Parcelable;

public class GeoCity implements Parcelable {
    protected String country, region, city, latitude, longitude, currency;
    protected int index;

    public GeoCity (String country, String region, String city, String latitude, String longitude, String currency) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currency = currency;
    }

    public GeoCity (String city) {
        this.country = null;
        this.region = null;
        this.city = city;
        this.latitude = null;
        this.longitude = null;
        this.currency = null;
    }

    protected GeoCity(Parcel in) {
        country = in.readString();
        region = in.readString();
        city = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        currency = in.readString();
    }

    public static final Creator<GeoCity> CREATOR = new Creator<GeoCity>() {
        @Override
        public GeoCity createFromParcel(Parcel in) {
            return new GeoCity(in);
        }

        @Override
        public GeoCity[] newArray(int size) {
            return new GeoCity[size];
        }
    };

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCurrency() {
        return currency;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(region);
        dest.writeString(city);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(currency);
    }
}


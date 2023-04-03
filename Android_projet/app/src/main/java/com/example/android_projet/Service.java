package com.example.android_projet;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {

    private String title;
    private String description;
    private String localisation;
    private String user;

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {return new Service(in);}

        @Override
        public Service[] newArray(int size) {return new Service[size];}
    };

    public Service(){}

    public Service(String title, String desc, String loc, String userMail) {
        this.title = title;
        this.description = desc;
        this.localisation = loc;
        this.user = userMail;
    }

    protected Service(Parcel in) {
        title = in.readString();
        description = in.readString();
        localisation = in.readString();
        user = in.readString();
    }

    public String getDescription() {
        return description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(localisation);
        parcel.writeString(user);
    }
}
package com.example.android_projet;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String mail;
    private String nom;
    private String prenom;
    private String telephone;

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {return new User(in);}

        @Override
        public User[] newArray(int size) {return new User[size];}
    };

    public User(){}

    public User(String mail, String nom, String prenom, String telephone) {
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public String getMail() { return mail; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }

    protected User(Parcel in) {
        mail = in.readString();
        nom = in.readString();
        prenom = in.readString();
        telephone = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mail);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(telephone);
    }
}
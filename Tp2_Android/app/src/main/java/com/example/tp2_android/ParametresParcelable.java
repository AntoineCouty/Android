package com.example.tp2_android;

import android.os.Parcel;
import android.os.Parcelable;

public class ParametresParcelable extends Parametres implements Parcelable {

    public ParametresParcelable(String clair, String chiffre, int cle) {
        super(clair, chiffre, cle);
    }

    protected ParametresParcelable(Parcel in) {
        super(in.readString(), in.readString(), in.readInt());
    }

    public static final Creator<ParametresParcelable> CREATOR = new Creator<ParametresParcelable>() {
        @Override
        public ParametresParcelable createFromParcel(Parcel in) {
            return new ParametresParcelable(in);
        }

        @Override
        public ParametresParcelable[] newArray(int size) {
            return new ParametresParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clair);
        dest.writeString(this.chiffre);
        dest.writeInt(this.cle);
    }
}

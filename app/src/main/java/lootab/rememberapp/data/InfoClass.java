package lootab.rememberapp.data;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ichung-gi on 2016. 1. 19..
 */
public class InfoClass implements Parcelable {
    public int _id;
    public String title;
    public String content;
    public String day;
    public String number;

    public InfoClass(){}

    public InfoClass(int _id , String title , String content , String day, String number){
        this._id = _id;
        this.title = title;
        this.content = content;
        this.day = day;
        this.number = number;
    }

    public InfoClass(String title , String content , String day, String number){
        this.title = title;
        this.content = content;
        this.day = day;
        this.number = number;
    }

    public InfoClass(InfoClass copy) {
        this._id = copy._id;
        this.title = copy.title;
        this.content = copy.content;
        this.day = copy.day;
        this.number = copy.number;
    }

    public InfoClass(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.day);
        dest.writeString(this.number);
    }

    private void readFromParcel(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.day = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<InfoClass> CREATOR = new Creator<InfoClass>() {
        public InfoClass createFromParcel(Parcel in) {
            return new InfoClass(in);
        }

        public InfoClass[] newArray (int size) {
            return new InfoClass[size];
        }
    };

}


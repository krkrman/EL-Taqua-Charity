package com.example.el_taquacharity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    String eventName, eventDescription;
    int numberOfFamilies;
    int remainingFamilies;

    public Event() {
    }

    public Event(String eventName, String eventDescription) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        numberOfFamilies = 0;
        remainingFamilies = 0;
    }

    protected Event(Parcel in) {
        // then do like that
        eventName = in.readString();
        eventDescription = in.readString();
        numberOfFamilies = in.readInt();
        remainingFamilies = in.readInt();
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getNumberOfFamilies() {
        return numberOfFamilies;
    }

    public void setNumberOfFamilies(int numberOfFamilies) {
        this.numberOfFamilies = numberOfFamilies;
    }

    public int getRemainingFamilies() {
        return remainingFamilies;
    }

    public void setRemainingFamilies(int remainingFamilies) {
        this.remainingFamilies = remainingFamilies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventDescription);
        dest.writeInt(numberOfFamilies);
        dest.writeInt(remainingFamilies);
    }
}

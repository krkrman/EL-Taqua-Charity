package com.example.el_taquacharity.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Family implements Parcelable {
    public static final Creator<Family> CREATOR = new Creator<Family>() {
        @Override
        public Family createFromParcel(Parcel in) {
            return new Family(in);
        }

        @Override
        public Family[] newArray(int size) {
            return new Family[size];
        }
    };

    private String ID;
    private String husbandName;
    private String wifeName;
    private ArrayList<String> childrenNames;
    private ArrayList<Integer> childrenAges;
    private String socialStatus;
    private String sonsNumber;
    private String work;
    private String phoneNumber;
    private String address;
    private String status;
    private String others;
    private int income;
    private int dept;
    private boolean isUnableToEarn, isOrphans, isOld, hasEndemic;

    public Family() {
    }
    public Family(String others, String husbandName, String wifeName, String socialStatus
            , String sonsNumber, String work, String phoneNumber, String address, String status
            , int income, int dept, ArrayList<String> childrenNames,ArrayList<Integer> childrenAges, boolean isUnableToEarn
            , boolean isOrphans, boolean isOld, boolean hasEndemic) {
        this.husbandName = husbandName;
        this.wifeName = wifeName;
        this.socialStatus = socialStatus;
        this.sonsNumber = sonsNumber;
        this.work = work;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.income = income;
        this.dept = dept;
        this.others = others;
        this.childrenNames = childrenNames;
        this.isUnableToEarn = isUnableToEarn;
        this.isOld = isOld;
        this.isOrphans = isOrphans;
        this.hasEndemic = hasEndemic;
        this.childrenAges = childrenAges;
    }
    protected Family(Parcel in) {
        // then do like that
        ID = in.readString();
        husbandName = in.readString();
        wifeName = in.readString();
        socialStatus = in.readString();
        sonsNumber = in.readString();
        work = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        status = in.readString();
        income = in.readInt();
        dept = in.readInt();
        others = in.readString();
        isOrphans = in.readByte() != 0;
        isOld = in.readByte() != 0;
        isUnableToEarn = in.readByte() != 0;
        hasEndemic = in.readByte() != 0;
        if (in.readByte() == 0x01) {
            childrenNames = new ArrayList<>();
            childrenAges = new ArrayList<>();
            in.readList(childrenNames, String.class.getClassLoader());
            in.readList(childrenAges,String.class.getClassLoader());
        } else {
            childrenNames = null;
            childrenAges = null;
        }
    }

    public ArrayList<Integer> getChildrenAges() {
        return childrenAges;
    }

    public void setChildrenAges(ArrayList<Integer> childrenAges) {
        this.childrenAges = childrenAges;
    }

    public boolean isUnableToEarn() {
        return isUnableToEarn;
    }

    public void setUnableToEarn(boolean unableToEarn) {
        isUnableToEarn = unableToEarn;
    }

    public boolean isOrphans() {
        return isOrphans;
    }

    public void setOrphans(boolean orphans) {
        isOrphans = orphans;
    }

    public boolean isOld() {
        return isOld;
    }

    public void setOld(boolean old) {
        isOld = old;
    }

    public boolean isHasEndemic() {
        return hasEndemic;
    }

    public void setHasEndemic(boolean hasEndemic) {
        this.hasEndemic = hasEndemic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(husbandName);
        dest.writeString(wifeName);
        dest.writeString(socialStatus);
        dest.writeString(sonsNumber);
        dest.writeString(work);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeInt(income);
        dest.writeInt(dept);
        dest.writeString(others);
        dest.writeByte((byte) (isOld ? 1 : 0));
        dest.writeByte((byte) (isOrphans ? 1 : 0));
        dest.writeByte((byte) (isUnableToEarn ? 1 : 0));
        dest.writeByte((byte) (hasEndemic ? 1 : 0));
        if (childrenNames == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childrenNames);
            dest.writeList(childrenAges);
        }
    }

    @Exclude
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public String getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
    }

    public String getSonsNumber() {
        return sonsNumber;
    }

    public void setSonsNumber(String sonsNumber) {
        this.sonsNumber = sonsNumber;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public ArrayList<String> getChildrenNames() {
        return childrenNames;
    }

    public void setChildrenNames(ArrayList<String> childrenNames) {
        this.childrenNames = childrenNames;
    }
}

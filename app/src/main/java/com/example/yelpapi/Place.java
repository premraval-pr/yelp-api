package com.example.yelpapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class Place implements Parcelable {
    private String name,id,phone,image_url,web_url,price;
    private double latitude,longitude;
    private List<String> cat;
    private List<Place.Hours> placeHours;


    protected Place(Parcel in) {
        name = in.readString();
        id = in.readString();
        phone = in.readString();
        image_url = in.readString();
        web_url = in.readString();
        price = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        cat = in.createStringArrayList();
        placeHours = in.createTypedArrayList(Hours.CREATOR);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Place(){}

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getCat() {
        return cat;
    }

    public void setCat(List<String> cat) {
        this.cat = cat;
    }

    public List<Place.Hours> getPlaceHours() {
        return placeHours;
    }

    public void setPlaceHours(List<Place.Hours> placeHours) {
        this.placeHours = placeHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(phone);
        dest.writeString(image_url);
        dest.writeString(web_url);
        dest.writeString(price);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeStringList(cat);
        dest.writeTypedList(placeHours);
    }


    public String objectPrint() {
        return "Place{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", image_url='" + image_url + '\'' +
                ", web_url='" + web_url + '\'' +
                ", price='" + price + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", cat=" + cat +
                ", placeHours=" + placeHours +
                '}';
    }

    public static class Hours implements Parcelable{
        private String day;
        private Date start_time,end_time;

        protected Hours(Parcel in) {
            day = in.readString();
            start_time = (Date) in.readSerializable();
            end_time = (Date) in.readSerializable();
        }

        public static final Creator<Hours> CREATOR = new Creator<Hours>() {
            @Override
            public Hours createFromParcel(Parcel in) {
                return new Hours(in);
            }

            @Override
            public Hours[] newArray(int size) {
                return new Hours[size];
            }
        };

        public String getDay() {
            return day;
        }

        public Date getStart_time() {
            return start_time;
        }

        public Date getEnd_time() {
            return end_time;
        }

        public Hours(String day, Date start_time, Date end_time) {
            this.day = day;
            this.start_time = start_time;
            this.end_time = end_time;
        }

        @Override
        public String toString() {
            return "Hours{" +
                    "day='" + day + '\'' +
                    ", start_time=" + start_time +
                    ", end_time=" + end_time +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(day);
            dest.writeSerializable(start_time);
            dest.writeSerializable(end_time);
        }
    }
}

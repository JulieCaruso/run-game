package adneom.moutons_electriques.game.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class User implements Parcelable, Comparable<User> {

    @Expose
    private Integer id;
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    @Expose
    private String photo1;
    @Expose
    private String photo2;
    @Expose
    private Integer score;

    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Integer id, String firstname, String lastname, String photo1, String photo2, int score) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.score = score;
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.photo1 = in.readString();
        this.photo2 = in.readString();
        this.score = in.readInt();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(photo1);
        dest.writeString(photo2);
        dest.writeInt(score);
    }

    @Override
    public int compareTo(@NonNull User o) {
        return o.score.compareTo(this.score);
    }
}


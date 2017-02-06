package ir.upcoders.sheydaee.model;

import android.graphics.Bitmap;

/**
 * Created by mahdi on 10/22/16.
 */
public class Martyr {


    private int id;
    private String first_name;
    private String last_name;
    private String birth_place;
    private String martyrdom_place;
    private String martyrdom_date;
    private String birth_date;
    private Bitmap avatar;
    private String mission;
    private Integer martyr_bio_id;
    private Integer martyr_memo_id;
    private Integer martyr_will_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getMartyrdom_place() {
        return martyrdom_place;
    }

    public void setMartyrdom_place(String martyrdom_place) {
        this.martyrdom_place = martyrdom_place;
    }

    public String getMartyrdom_date() {
        return martyrdom_date;
    }

    public void setMartyrdom_date(String martyrdom_date) {
        this.martyrdom_date = martyrdom_date;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public Integer getMartyr_bio_id() {
        return martyr_bio_id;
    }

    public void setMartyr_bio_id(Integer martyr_bio_id) {
        this.martyr_bio_id = martyr_bio_id;
    }

    public Integer getMartyr_memo_id() {
        return martyr_memo_id;
    }

    public void setMartyr_memo_id(Integer martyr_memo_id) {
        this.martyr_memo_id = martyr_memo_id;
    }

    public Integer getMartyr_will_id() {
        return martyr_will_id;
    }

    public void setMartyr_will_id(Integer martyr_will_id) {
        this.martyr_will_id = martyr_will_id;
    }
}

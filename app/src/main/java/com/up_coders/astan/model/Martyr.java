package com.up_coders.astan.model;

import android.graphics.Bitmap;

/**
 * Created by mahdi on 10/22/16.
 */
public class Martyr {


    private int id;
    private String name;
    private String file_path;
    private String file_name;
    private String file_size;
    private String title;
    private String description;
    private String mime_type;
    private String tag;
    private String creation_dtime;
    private String modif_dtime;
    private String tenant;
    private String submitter;
    private String downloads;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreation_dtime() {
        return creation_dtime;
    }

    public void setCreation_dtime(String creation_dtime) {
        this.creation_dtime = creation_dtime;
    }

    public String getModif_dtime() {
        return modif_dtime;
    }

    public void setModif_dtime(String modif_dtime) {
        this.modif_dtime = modif_dtime;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public void setDownloads(String downloads) {
        this.submitter = submitter;
    }
    public String getDownloads() { return downloads;  }

}

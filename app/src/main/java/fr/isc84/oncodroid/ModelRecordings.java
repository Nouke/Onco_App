package fr.isc84.oncodroid;

import android.net.Uri;

public class ModelRecordings {
    //Declaration des attributs
    String title;
    String duration;
    String date;
    Uri uri;

    //Declaration des getteurs et setteurs
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


}
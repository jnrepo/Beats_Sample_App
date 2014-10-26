package challenge.beats.joon.models;

import org.json.JSONObject;

/**
 * Album model class
 * Created by Joon on 10/24/2014.
 */
public class Album {
    private String id;
    public String result_type, detail, display, type;
    public JSONObject related;
    public String url;

    public Album(String id, String result_type, String detail, String display, String type, JSONObject related) {
        this.id = id;
        this.result_type = result_type;
        this.detail = detail;
        this.display = display;
        this.type = type;
        this.related = related;
    }

    public void setAlbumArtUrl(String url) {
      this.url = url;
    }
    public String getAlbumArtUrl() {return url;}
    public String getId() {return id;}
    public String getTitle() {return display;}
    public String getArtist() {return detail;}
}

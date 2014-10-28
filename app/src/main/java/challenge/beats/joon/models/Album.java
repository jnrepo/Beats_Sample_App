package challenge.beats.joon.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Model of Album
 *
 * Stores information about an Album we retrieve from the BeatsAPI server response
 * - album id
 * - result type
 * - detail
 * - display
 * - type
 * - related
 * - album art url
 */
public class Album {
    private String url;

    @SerializedName("id")
    private String id;

    @SerializedName("result_type")
    private String result_type;

    @SerializedName("detail")
    private String detail;

    @SerializedName("display")
    private String display;

    @SerializedName("type")
    private String type;

    @SerializedName("related")
    private JsonObject related;

    public Album() {}

    public void setAlbumArtUrl(String url) {
        this.url = url;
    }
    public String getAlbumArtUrl() {return url;}
    public String getId() {return id;}
    public String getTitle() {return display;}
    public String getArtist() {return detail;}
    public String getResultType(){return result_type;}
    public String getType(){return type;}
}

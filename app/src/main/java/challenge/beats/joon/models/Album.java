package challenge.beats.joon.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Models an album class and holds all relevant data
 *
 * Created by Joon on 10/24/2014.
 */
public class Album {
    private static final String TAG = "ALBUM";
    private String id, result_type, detail, display, type, url;
    public JSONObject related;

    public Album(JSONObject j) {

        try {
            this.id = j.getString("id");
            this.result_type = j.getString("result_type");
            this.detail = j.getString("detail");
            this.display = j.getString("display");
            this.type = j.getString("type");
            this.related = j.getJSONObject("related");
        } catch (JSONException e) {
            Log.e(TAG, "[Error] while reading JSON object to create album");
            e.printStackTrace();
        }
    }

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
    public String getResultType(){return result_type;}
    public String getType(){return type;}
}

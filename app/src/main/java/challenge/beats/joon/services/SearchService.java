package challenge.beats.joon.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import challenge.beats.joon.models.Album;
import challenge.beats.joon.views.activities.Main;

/**
 * Created by Joon on 10/24/2014.
 */
public class SearchService extends Service {
    // logging
    private final String TAG = "SearchService";

    // TODO create url factory builder, later
    private final String SEARCH_BASE_URL = "https://partner.api.beatsmusic.com/v1/api/search?";
    private final String ALBUM_ART_BASE = "https://partner.api.beatsmusic.com/v1/api/albums/";
    private final String ALBUM_IMAGE = "/images/default";

    // json request object
    private JsonObjectRequest mReq;

    // singletons
    private RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
    private Main main = Main.getInstance();

    public SearchService(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    /**
     * Makes a call to the Beats SearchAPI, then stores results and tells the main activity it is done
     *
     * @param album (String): search query containing an album title
     */
    public void searchByAlbum(final String album) {
        String query = album.trim().replace(' ','+');
        final ArrayList<Album> result = new ArrayList<Album>();
        StringBuilder url = new StringBuilder(SEARCH_BASE_URL)
                .append("q=")
                .append(query)
                .append("&type=album");
        appendKey(url);

        Log.d(TAG, "[ALBUM] query url: " + url.toString());

        // make the call
        mReq =  new JsonObjectRequest(Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

            @Override
            public void
            onResponse(JSONObject response) {
            Log.i(TAG, "Parsing ALBUM search response...");

            try {
                // get data node
                if (response.has("data")) {
                    JSONArray albums_json = response.getJSONArray("data");
                    Log.i(TAG, "albums: " + albums_json);
                    for(int i = 0; i < albums_json.length(); i++) {
                        JSONObject a = albums_json.getJSONObject(i);
                        result.add(new Album(a.getString("id"), a.getString("result_type"), a.getString("detail"), a.getString("display"), a.getString("type"), a.getJSONObject("related")));

                    }

                    for(int x = 0; x < result.size(); x+=5) {
                        getAlbumArt(result, x);
                        getAlbumArt(result, x + 1);
                        getAlbumArt(result, x + 2);
                        getAlbumArt(result, x + 3);
                        getAlbumArt(result, x + 4);
                    }

                    main.setAlbums(result);
                }

            } catch (JSONException e) {
                Log.e(TAG, "[error] Something was wrong with the JSONObject...");
                e.printStackTrace();
                // fire a toast telling there was an error
            }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "There was an error searching for an album");
            }
        });

        queue.add(mReq);
    }

    /**
     * Makes a call to the SearchAPI with the album id, obtain album art url
     *
     * - Call Format: https://partner.api.beatsmusic.com/v1/api/albums/al74961607/images/default?client_id=[CLIENT_ID]
     *                https://partner.api.beatsmusic.com/v1/api/albums/al1001393/images/default?client_id=qtv3jd27hk45ymsmhhfsbc9q
     *
     * @param a: list of albums we're looking through
     * @param index (int): index of the album we want to access in the album list
     */
    public void getAlbumArt(final ArrayList<Album> a, final int index) {
        // check to see we don't call an element that doesn't exist in the array
        if (index < a.size()) {
            final Album content = a.get(index);
            StringBuilder url = new StringBuilder(ALBUM_ART_BASE)
                                    .append(content.getId())
                                    .append(ALBUM_IMAGE).append("?client_id=").append("qtv3jd27hk45ymsmhhfsbc9q");

            Log.i(TAG, "Request URL for album art: " + url);
            content.setAlbumArtUrl(url.toString());

        }
    }

    /**
     * Appends the client key to the passed in StringBuilder object.
     *
     * **Future, move to url builder class
     *
     * @param s (StringBuilder): string builder we are appending the client id to
     * @return (StringBuilder): the stringbuilder with the client id appended to it
     */
    public void appendKey(StringBuilder s) {
        s.append("&client_id=").append("qtv3jd27hk45ymsmhhfsbc9q");
    }

    /**
     * Custom error listener
     *
     * should return
     *  - what the input was
     *  - what is wrong
     *
     * @param methodName (String): type of search we failed on
     * @return
     */
    private Response.ErrorListener ErrorListener(final String methodName) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "[Error] call failed, while searching for" + methodName);

            }
        };
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public SearchService getService() {
            return SearchService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}

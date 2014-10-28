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
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import challenge.beats.joon.models.Album;
import challenge.beats.joon.views.activities.MainActivity;

/**
 * This service communicates with the BeatsAPI, parses the information and relays the parsed response to MainActivity.
 */
public class SearchService extends Service {
    private final String TAG = "SearchService";

    private final static String SEARCH_BASE_URL = "https://partner.api.beatsmusic.com/v1/api/search?";
    private final static String ALBUM_ART_BASE = "https://partner.api.beatsmusic.com/v1/api/albums/";
    private final static String ALBUM_IMAGE = "/images/default";

    private StringRequest msReq;

    // singletons
    private RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
    private MainActivity main = MainActivity.getInstance();

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();
    private JsonParser parser = new JsonParser();

    public SearchService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    /**
     * Makes a call to the Beats SearchAPI, then parses the response and stores albums into an ArrayList<Album>.
     *
     * @param album (String): search query containing an album title
     */
    public void searchByAlbum(final String album) {
        // replace spaces with '+'
        String query = album.trim().replace(' ', '+');
        final ArrayList<Album> result = new ArrayList<Album>();
        StringBuilder url = new StringBuilder(SEARCH_BASE_URL)
                .append("q=")
                .append(query)
                .append("&type=album")
                .append("&client_id=")
                .append("qtv3jd27hk45ymsmhhfsbc9q");

        // make the call
        msReq = new StringRequest(Method.GET, url.toString(), new Response.Listener<String>() {

            @Override
            public void
            onResponse(String response) {
            Log.i(TAG, "Parsing ALBUM search response...");

            // get data node
            JsonObject albums_json = parser.parse(response).getAsJsonObject();
            Type listType = new TypeToken<ArrayList<Album>>() {}.getType();
            ArrayList<Album> result = new Gson().fromJson(albums_json.get("data"), listType);

            // get the album art urls
            for (int x = 0; x < result.size(); x++) {
                StringBuilder url = new StringBuilder(ALBUM_ART_BASE)
                        .append(result.get(x).getId())
                        .append(ALBUM_IMAGE)
                        .append("?client_id=")
                        .append("qtv3jd27hk45ymsmhhfsbc9q");
                result.get(x).setAlbumArtUrl(url.toString());
            }

            main.setAlbums(result);

            }
        }, ErrorListener("album"));

        queue.add(msReq);
    }

    /**
     * Appends the client key to the passed in StringBuilder object.
     * <p/>
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
     * @param methodName (String): what were calling when we failed
     * @return : an error response
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

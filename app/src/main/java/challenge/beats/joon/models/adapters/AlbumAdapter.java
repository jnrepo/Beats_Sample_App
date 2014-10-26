package challenge.beats.joon.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import challenge.beats.joon.models.Album;
import challenge.beats.joon.services.R;
import challenge.beats.joon.services.VolleySingleton;

public class AlbumAdapter extends BaseAdapter {
    // constant that dictates the size of titles
    private final int MAX_TITLE_LENGTH_LANDSCAPE = 75;
    private final int MAX_TITLE_LENGTH_PORTRAIT = 45;

    private Activity activity;
    private ArrayList<Album> albums;
    private static LayoutInflater inflater = null;

    // Singleton for image loading
    private ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();
    
    public AlbumAdapter(Activity targetActivity, ArrayList<Album> albums) {
        this.activity = targetActivity;
        this.albums = albums;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Gets the size of ArrayList<Album>
     *
     * @return the size of list of data
     */
    public int getCount() {
        return albums.size();
    }

    public Object getItem(int position) {
        return albums.get(position);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView == null)
            vi = inflater.inflate(R.layout.item, null);

        Album album = albums.get(position);

        // get the views
        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView artist = (TextView) vi.findViewById(R.id.artist);
        NetworkImageView image = (NetworkImageView) vi.findViewById(R.id.image);

        // set the views
        title.setText(truncateTitle(album.getTitle()));
        artist.setText(album.getArtist());
        image.setImageUrl(album.getAlbumArtUrl(), imageLoader);

        return vi;
    }

    /**
     * If the title's longer than 'MAX_TITLE_LENGTH_LANDSCAPE' characters (up to dev's discretion), we truncate the title for aesthetics
     *
     * TODO, truncate differently for portrait orientation?, maybe by the difference in ratio of width vs height
     *
     * @param title (String): the title of the album
     * @return (String): either the original title, or truncated title
     */
    public String truncateTitle(String title) {
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        // landscape
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            if (title.toCharArray().length > MAX_TITLE_LENGTH_LANDSCAPE) {
                StringBuilder nTitle = new StringBuilder(title.substring(0, MAX_TITLE_LENGTH_LANDSCAPE)).append("...");
                return nTitle.toString();
            }
        } else {
            if (title.toCharArray().length > MAX_TITLE_LENGTH_PORTRAIT) {
                StringBuilder nTitle = new StringBuilder(title.substring(0, MAX_TITLE_LENGTH_PORTRAIT)).append("...");
                return nTitle.toString();
            }
        }
        return title;
    }

}
package challenge.beats.joon.models.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import challenge.beats.joon.services.R;
import challenge.beats.joon.services.VolleySingleton;
import challenge.beats.joon.models.Album;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private  ArrayList<Album> albums;
    private static LayoutInflater inflater=null;
    private ImageLoader imageLoader = VolleySingleton.getInstance().getImageLoader();

    private int maxTitleLength = 75;
    
    public LazyAdapter(Activity a, ArrayList<Album> d) {
        activity = a;
        albums =d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return albums.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        // get the views
        TextView title=(TextView)vi.findViewById(R.id.title);
        TextView artist=(TextView)vi.findViewById(R.id.artist);
        NetworkImageView image=(NetworkImageView)vi.findViewById(R.id.image);

        // set the views
        title.setText(truncateTitle(albums.get(position).getTitle()));
        artist.setText(albums.get(position).getArtist());
        image.setImageUrl(albums.get(position).getAlbumArtUrl(), imageLoader);

        return vi;
    }

    /**
     * If the title's longer than 150 characters (up to dev's discretion), we truncate the title for aesthetics
     *
     * @param title (String): the title of the album
     * @return (String): either the original title, or truncated title
     */
    public String truncateTitle(String title) {
        if (title.toCharArray().length > maxTitleLength) {
            StringBuilder nTitle = new StringBuilder(title.substring(0, maxTitleLength)).append("...");
            return nTitle.toString();
        }
        return title;
    }

}
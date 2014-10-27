package challenge.beats.joon.views.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import challenge.beats.joon.models.Album;
import challenge.beats.joon.models.adapters.AlbumAdapter;
import challenge.beats.joon.services.R;
import challenge.beats.joon.views.activities.MainActivity;

/**
 * View that displays the list of albums.
 */
public class SearchResultsFragment extends Fragment implements AbsListView.OnItemClickListener {

    private ArrayList<Album> albums;
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private AlbumAdapter mAdapter;

    public SearchResultsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Set the adapter
        if (albums.size() == 0) {
            Toast.makeText(MainActivity.getInstance(), "Sorry, we couldn't find anything...", Toast.LENGTH_LONG).show();
            return view;
        }

        mAdapter = new AlbumAdapter(getActivity(), albums);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>)mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item_album has been selected.
            mListener.onListItemClick(String.valueOf(position));
        }
    }

    /**
     * Sets the text if we don't have anything
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        public void onListItemClick(String id);
    }

    public void setAlbums(ArrayList<Album> a) {
        this.albums = a;
    }

}

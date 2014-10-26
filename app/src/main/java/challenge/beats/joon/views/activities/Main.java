package challenge.beats.joon.views.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;

import challenge.beats.joon.models.Album;
import challenge.beats.joon.services.R;
import challenge.beats.joon.services.SearchService;
import challenge.beats.joon.views.fragments.MainFragment;
import challenge.beats.joon.views.fragments.SearchResultsFragment;

public class Main extends Activity implements SearchResultsFragment.OnFragmentInteractionListener {
    // Logging
    private final String TAG = "Main";

    // Services
    private SearchService mBoundService;
    private boolean mIsBound = false;

    // Singleton-related
    private static Main mInstance;
    private static Context mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // interviews
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        } else {
            Log.i(TAG, "Saved instance wasn't null...");
            SearchResultsFragment saved = (SearchResultsFragment) getFragmentManager().findFragmentByTag("search");
            if (saved != null) {

            }
        }

        doBindService();
        handleIntent(getIntent());

        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static Main getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    // we should start the search fragment
    public void setAlbums(ArrayList<Album> result) {

        // set a new fragment
        SearchResultsFragment frag_search = new SearchResultsFragment();
        frag_search.setAlbums(result);  // pass in the result to the adapter
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, frag_search, "search");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListItemClick(String id) {

    }

    /* intent section */

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * method that reacts to different types of events
     * - if search, we call a search using the SearchService
     *
     * @param intent
     */
    private void handleIntent(Intent intent) {

        // if search is called, call a search with service
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String raw_query = intent.getStringExtra(SearchManager.QUERY);

            // replace ' ' with '+' to do search
            String nQuery = raw_query.trim().replace(' ','+');

            mBoundService.searchByAlbum(nQuery);

        }
    }

    /* related to binding and unbinding with services */

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((SearchService.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(this,
                SearchService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}

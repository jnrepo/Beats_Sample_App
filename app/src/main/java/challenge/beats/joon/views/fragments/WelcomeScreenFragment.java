package challenge.beats.joon.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import challenge.beats.joon.services.R;

/**
 * Created by Joon on 10/24/2014.
 */
public class WelcomeScreenFragment extends Fragment {
    public WelcomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        return rootView;
    }

}

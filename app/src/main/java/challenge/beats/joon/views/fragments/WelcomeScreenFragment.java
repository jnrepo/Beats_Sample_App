package challenge.beats.joon.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import challenge.beats.joon.services.R;

/**
 * View for the welcome screen
 */
public class WelcomeScreenFragment extends Fragment {
    public WelcomeScreenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

}

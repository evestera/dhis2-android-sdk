package org.hisp.dhis.android.sdk.ui.fragments.coordinatepicker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import org.hisp.dhis.android.sdk.R;

public class CoordinatePickerFragment extends Fragment {

    public static final String TAG = CoordinatePickerFragment.class.getSimpleName();
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The system calls this when creating the fragment. Within your implementation,
        //you should initialize essential components of the fragment that you want
        //to retain when the fragment is paused or stopped, then resumed.
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //The system calls this when it's time for the fragment to draw its user
        //interface for the first time. To draw a UI for your fragment, you
        //must return a View from this method that is the root of your
        //fragment's layout. You can return null if the fragment does not
        //provide a UI.
        View view = inflater.inflate(R.layout.fragment_coordinate_picker, container, false);
        return view;
    }
}

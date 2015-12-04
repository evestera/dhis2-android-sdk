package org.hisp.dhis.android.sdk.ui.fragments.coordinatepicker;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.controllers.GpsController;

public class CoordinatePickerFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap; // The Google Map object
    private Marker marker; // The marker shown in the map view
    private Callback callback; // The object that handles the callback from the map to the listview
                                // Carrying the selected coordinates through saveLocation
    private double initialLatitude; // The previous latitude
    private double initialLongitude; // The previous longitude
    public static final String TAG = CoordinatePickerFragment.class.getSimpleName();

    /**
     * Factory method for constructing a new instance of the CoordinatePickerFragment.
     * Initial/previous latitude and longitude needs to be specified.
     */
    public static CoordinatePickerFragment newInstance(double latitude, double longitude, Callback callback) {
        CoordinatePickerFragment fragment =  new CoordinatePickerFragment();
        fragment.setCallback(callback);
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void saveLocation(double latitude, double longitude);
    }

    /**
     * When a new instance of CoordinatePickerFragment is created using the newInstance method
     * onCreate is called, reading the latitude and longitude that were bundled in the object.
     * These values are then assign to the corresponding class/instance variables of that instance.
     */
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            initialLatitude = savedInstanceState.getDouble("latitude");
            initialLongitude = savedInstanceState.getDouble("longitude");
        } else {
            initialLatitude = getArguments().getDouble("latitude");
            initialLongitude = getArguments().getDouble("longitude");
        }
    }

    /**
     * The next to methods makes sure the Actionbar is hidden during the duration of the map
     * presentation.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.show();
    }

    private ActionBar getActionBar() {
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        } else {
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("latitude", marker != null ? marker.getPosition().latitude : 0);
        outState.putDouble("longitude", marker != null ? marker.getPosition().longitude : 0);
    }

    /**
     * This method handles setup procedures of the view by defining the OK- and cancel buttons as
     * well as their listeners. These serves as an overlay to the Map-view itself and dictates
     * the logic that is performed when exiting the map view.
     */
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coordinate_picker, container, false);
        Button okButton = (Button) view.findViewById(R.id.ok_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker == null) {
                    return;
                }
                LatLng location = marker.getPosition();
                callback.saveLocation(location.latitude, location.longitude);

                getFragmentManager().popBackStack(); // Go to previous fragment i.e the listview.
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    /**
     * This method implements the Google Map functionality i.e mostly defining properties and
     * listeners for the various actions.
     * @param googleMap: The Google Map Object specific to the CoordinatePickerFragment instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (initialLatitude != 0.0 || initialLongitude != 0.0) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(initialLatitude, initialLongitude)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            /**
             * This listener is invoked when pressing the location button at the top right of the
             * screen in the Google Maps display. This method removes the previous marker and
             * created at new at the users locations by using GpsController.getLocation(). After the
             * marker variable has been updated the user receives a toast informing the user that the
             * location will be saved when saved is pressed (even if the map is not shown).
             */
            @Override
            public boolean onMyLocationButtonClick() {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(GpsController.getLocation().getLatitude(),
                                GpsController.getLocation().getLongitude())));
                Toast.makeText(
                        getActivity(),
                        "Your coordinates selected. Click Ok to save.",
                        Toast.LENGTH_LONG).show();

                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                return true;
            }
        });
    }
}

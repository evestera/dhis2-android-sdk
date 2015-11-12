package org.hisp.dhis.android.sdk.ui.fragments.coordinatepicker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.hisp.dhis.android.sdk.R;

public class CoordinatePickerFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;
    private Callback callback;
    private double initalLatitude;
    private double initialLongitude;
    public static final String TAG = CoordinatePickerFragment.class.getSimpleName();

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

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            initalLatitude = savedInstanceState.getDouble("latitude");
            initialLongitude = savedInstanceState.getDouble("longitude");
        } else {
            initalLatitude = getArguments().getDouble("latitude");
            initialLongitude = getArguments().getDouble("longitude");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("latitude", marker != null ? marker.getPosition().latitude : 0);
        outState.putDouble("longitude", marker != null ? marker.getPosition().longitude : 0);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coordinate_picker, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        if (initalLatitude != 0.0 || initialLongitude != 0.0) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(initalLatitude, initialLongitude)));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(latLng));
                callback.saveLocation(latLng.latitude, latLng.longitude);
            }
        });
    }
}

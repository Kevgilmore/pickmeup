package je.digital.kevin_pickmeup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import je.digital.kevin_pickmeup.models.Pickup;

public class PassengerFragment extends Fragment {

    final FirebaseDatabase fb = FirebaseDatabase.getInstance();
    DatabaseReference mRef = fb.getReference();

    GoogleMap map;

    LatLng centre;

    LatLng myLocation = new LatLng(49.182399, -2.102213);

    ImageView iv;

    Pickup pickup;

    boolean isPickupRequested = false;

    // this runs WHILE the view is loading (mainly for the map)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // creates the passenger fragment object
        View rootView = inflater.inflate(R.layout.fragment_passenger, container, false);

        // defines where the load the map
        SupportMapFragment mapFragment =  (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);

        // creates the map using the mapFragment object
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap googleMap) {

                map = googleMap; //saves the map to be used in other functions

                //start here
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.getUiSettings().setCompassEnabled(true);
                CameraPosition overMe = CameraPosition.builder()
                        .target(myLocation)
                        .zoom(14)
                        .bearing(0)
                        .tilt(10)
                        .build();
                map.animateCamera(CameraUpdateFactory.
                newCameraPosition(overMe),100,null);

                // updates the centre location when dragging the map
                map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        centre = map.getCameraPosition().target;
                        System.out.println(centre);
                   }
                });

            }
        });

        return rootView;
    }


    // this runs AFTER the view is loaded (sets up buttons etc)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // MAIN BUTTON
        final Button mainBtn = view.findViewById(R.id.requestBtn);
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start here
                if ( !isPickupRequested ){
                    mainBtn.setText("CANCEL REQUEST");
                    requestPickup(v);
                } else if ( isPickupRequested ) {
                    mainBtn.setText("REQUEST PICKUP");
                    cancelPickup(v);
                }
            }
        });

        // FLOATING BUTTON
        final FloatingActionButton actionBtn = view.findViewById(R.id.myLocationButton);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sets cam position over digital jersey (gps mock)

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18));

            }
        });

        // LOCATION MARKER
        iv= getView().findViewById(R.id.locationMarker);
    }

    public void requestPickup(View view) {
        // clears any old markers
        map.clear();

        // add marker to map with custom icon
        MarkerOptions markerOptions = new MarkerOptions().position(centre);
        map.addMarker(markerOptions);

        //hides the default marker
        iv.setVisibility(View.GONE);

        //sets the marker variable to true
        isPickupRequested = true;

        //save to firebase
        saveToFirebase(new Pickup(myLocation.toString()));
    }

    public void cancelPickup(View view) {
        map.clear();
        iv.setVisibility(View.VISIBLE);
        isPickupRequested = false;
        removeFromFirebase(pickup);
    }

    public void saveToFirebase(Pickup pickup) {
        // read the index key
        pickup.setId(mRef.push().getKey());

        // create a child with index value
        mRef.child("Pickups").child(pickup.getId()).setValue(pickup);
    }

    public void removeFromFirebase(Pickup pickup) {
        //mRef.child("Pickups").child(pickup.getId()).removeValue();
    }
}

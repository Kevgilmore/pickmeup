package je.digital.kevin_pickmeup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import je.digital.kevin_pickmeup.constants.Status;
import je.digital.kevin_pickmeup.models.Pickup;

public class DriverFragment extends Fragment {

    final FirebaseDatabase fb = FirebaseDatabase.getInstance();
    DatabaseReference mRef = fb.getReference();

    RecyclerView pickupsRecycleView;

    // this runs WHILE the view is loading (sets up the view)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_driver, container, false);

        pickupsRecycleView = rootView.findViewById(R.id.RecycleView);

        return rootView;
    }

    // this runs WHILE the page is loading
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this code runs whenever data changes
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // creates an empty list
                List<Pickup> pickups = new ArrayList<>();

                // iterates over each pickup returned on firebase
                for (DataSnapshot ds : dataSnapshot.child("Pickups").getChildren()) {
                    Pickup pickup = ds.getValue(Pickup.class); //creates an empty pickup object
                    pickup.getStatus();
                    if (pickup.getStatus()==Status.ACTIVE) { //checks if status is active
                        pickups.add(pickup); //only adds active pickups to the list
                    }
                }
                updateUI(pickups); //calls updateUI function and passes it the list of active pickups
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("cancelled");
            }
        });

    }

    public void updateUI(List<Pickup> pickups) {
        RecyclerAdapter adapter = new RecyclerAdapter(pickups, getContext());
        pickupsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        pickupsRecycleView.setAdapter(adapter);
    }
}

package wencongchio.com.ucsibustracker;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LinearLayout noServiceLayout;
    private TextView textTBSDepartureTime;
    private TextView textUniversityDepartureTime;
    private TextView textCollegeDepartureTime;
    private FloatingActionButton btnShowTraffic;

    LatLng universityLatLng;
    LatLng collegeLatLng;
    LatLng tbsLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        noServiceLayout = (LinearLayout)findViewById(R.id.location_noServiceLayout);
        textTBSDepartureTime = (TextView)findViewById(R.id.textView_location_tbsDepartureTime);
        textCollegeDepartureTime = (TextView)findViewById(R.id.textView_location_collegeDepartureTime);
        textUniversityDepartureTime = (TextView)findViewById(R.id.textView_location_universityDepartureTime);
        btnShowTraffic = (FloatingActionButton)findViewById(R.id.btn_location_traffic);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkBusAvailable();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        btnShowTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.isTrafficEnabled()){
                    mMap.setTrafficEnabled(false);
                }
                else{
                    mMap.setTrafficEnabled(true);
                }
            }
        });

        universityLatLng = new LatLng(3.078449, 101.733248);
        collegeLatLng = new LatLng(3.085257, 101.736870);
        tbsLatLng = new LatLng(3.076147, 101.711917);

        mMap.addMarker(new MarkerOptions().position(universityLatLng).title("Bus Stop - University").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_stop)));
        mMap.addMarker(new MarkerOptions().position(collegeLatLng).title("Bus Stop - College").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_stop)));
        mMap.addMarker(new MarkerOptions().position(tbsLatLng).title("Bus Stop - TBS").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_stop)));


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(universityLatLng, 16);
        mMap.animateCamera(cameraUpdate);

        getBusLocation("a");
        getBusLocation("b");
        getBusLocation("c");
        getBusDepartureTime("tbs", textTBSDepartureTime);
        getBusDepartureTime("college", textCollegeDepartureTime);
        getBusDepartureTime("university", textUniversityDepartureTime);

    }

    private void getBusLocation(final String busID){
        final Marker[] busMarker = new Marker[1];

        DatabaseReference busLocationRef = FirebaseDatabase.getInstance().getReference().child("BusAvailable").child(busID).child("l");
        busLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    List<Object> map = (List<Object>)dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;

                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    LatLng busLatLng = new LatLng(locationLat, locationLng);

                    if(busMarker[0] != null){
                        busMarker[0].remove();
                    }
                    busMarker[0] = mMap.addMarker(new MarkerOptions().position(busLatLng).title("Bus " + busID.toUpperCase()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus_location)));

                }
                else{
                    if(busMarker[0] != null){
                        busMarker[0].remove();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getBusDepartureTime(final String destinationID, final TextView timeText){

        final DatabaseReference busDepartureTimeRef = FirebaseDatabase.getInstance().getReference().child("BusDepartureTime");
        Query departureTimeQuery = busDepartureTimeRef.orderByChild(destinationID).startAt("00:00:00").limitToFirst(1);
        departureTimeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

                        HashMap map = (HashMap) snapshot.getValue();

                        if(map.containsKey(destinationID)){
                            Date time = null;
                            try {
                                time = inputFormat.parse(map.get(destinationID).toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String formattedTime = outputFormat.format(time);
                            timeText.setText(formattedTime);
                        }
                        else{
                            timeText.setText("-");
                        }

                    }
                }
                else{
                    timeText.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkBusAvailable(){
        DatabaseReference busLocationRef = FirebaseDatabase.getInstance().getReference().child("BusAvailable");
        busLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    noServiceLayout.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                else{
                    noServiceLayout.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

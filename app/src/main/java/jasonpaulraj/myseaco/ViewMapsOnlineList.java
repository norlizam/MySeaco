package jasonpaulraj.myseaco;

/**
 * Created by GPH on 10/18/2016.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonGeometry;
import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class ViewMapsOnlineList extends FragmentActivity implements OnMapReadyCallback {

    public final String TAG = "ViewMapsOnlineList";
    public GoogleMap mMap;
    private GeoJsonGeometry mGeometry;
    protected LocationManager locationManager;
    private MySQLiteHelper db;
    private GoogleApiClient client;

    String lat, lng, address1, address2, barcode;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");
        address1 = getIntent().getExtras().getString("address1");
        address2 = getIntent().getExtras().getString("address2");
        barcode = getIntent().getExtras().getString("barcode");

        Log.d(TAG,"LATITUDE ["+lat+" AND LONGITUDE ["+lng+"]");

        if (mMap == null){
            Log.d("MapsActivity","if not null");
        }
        //        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        // mMap.setMyLocationEnabled(true);
        // mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        //STEP GET CURRENT LOCATION 5: start permission get current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //end permission current location


        //start focus on the selected location
        double latDouble = Double.parseDouble(lat);
        double lngDouble = Double.parseDouble(lng);
        Log.d(TAG,"LATITUDE double ["+latDouble+" AND LONGITUDE double["+lngDouble+"]");

        LatLng locationPerRow = new LatLng(latDouble,lngDouble);
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(locationPerRow, 15);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);
        //end focus on the selected location

        //start addMarker
        drawMarker(latDouble, lngDouble);
        //end addMarker

        //onClick Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                final CustomInfoView customInfo = new CustomInfoView(getLayoutInflater(), marker.getId());
                double lngT = marker.getPosition().longitude;
                double latT = marker.getPosition().latitude;
                String s_lat = "";
                String s_lon = "";
                String s_nam = "";
                String s_add = "";

                Log.d(TAG,"OnMarker click lat["+lat+"] lon["+lng+"]");

                customInfo.latitude = String.valueOf(lat);
                customInfo.longitude = String.valueOf(lng);
                customInfo.name = barcode;//s_nam;
                customInfo.address = address1;//s_add;//s_add;
                customInfo.address2= address2;//s_add2;

                mMap.setInfoWindowAdapter(customInfo);
                marker.showInfoWindow();

                return false;
            }
        });
        //end onClick Marker

    }

    private void drawMarker(double lat, double lng){
        // Creating an instance of MarkerOptions
        //MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        final LatLng position = new LatLng(lat, lng);
        final MarkerOptions options = new MarkerOptions().position(position);
        mMap.addMarker(options);

    }
    //end display all the location as marker on google maps
}

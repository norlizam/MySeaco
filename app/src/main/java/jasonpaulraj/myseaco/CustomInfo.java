package jasonpaulraj.myseaco;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

class CustomInfo extends Config implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater = null;

    String id;
    String name;
    String address;
    String address2;
    String longitude;
    String latitude;

    CustomInfo(LayoutInflater inflater, String a) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = inflater.inflate(R.layout.custom_popup, null);


        TextView nam = (TextView) popup.findViewById(R.id.nView);
        TextView add = (TextView) popup.findViewById(R.id.aView);
        TextView add2 = (TextView) popup.findViewById(R.id.a2View);
        TextView lat = (TextView) popup.findViewById(R.id.latView);
        TextView lon = (TextView) popup.findViewById(R.id.longView);

        nam.setText(name);
        add.setText(address);
        add2.setText(address2);
        lat.setText(latitude);
        lon.setText(longitude);

        Log.d("custominfo", " Name: [" + name + "] Address:  [" + "" + address + "]");

        return (popup);

    }


}

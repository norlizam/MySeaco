//package jasonpaulraj.myseaco;
//
//
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.Marker;
//import com.google.maps.android.geojson.GeoJsonFeature;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
////public class CustomInfo implements GoogleMap.InfoWindowAdapter{
////
////    private LayoutInflater mInflater;
////    public CustomInfo(LayoutInflater inflater) {this.mInflater=inflater;}
////
////
////    @Override
////    public View getInfoContents(Marker marker) {
////        View popup = mInflater.inflate(R.layout.custommarker, parent, false);
////        TextView tv=(TextView)popup.findViewById(R.id.title);
////        tv.setText(marker.getTitle());
////        tv=(TextView)popup.findViewById(R.id.address1);
////        tv.setText(marker.getSnippet());
////
////        return popup;
////    }
////
////    @Override
////    public View getInfoWindow(Marker marker) {
////        View popup = mInflater.inflate(R.layout.custommarker, null);
////        TextView tv=(TextView)popup.findViewById(R.id.title);
////        tv.setText(marker.getTitle());
////        tv=(TextView)popup.findViewById(R.id.address2);
////        tv.setText(marker.getSnippet());
////
////        return popup;
////    }}
//class MapsOverlay extends Config implements GoogleMap.InfoWindowAdapter {
//    LayoutInflater inflater = null;
//
//
//    private String JSON_STRING;
//
//
//    MapsOverlay(LayoutInflater inflater, String a) {
//        this.inflater = inflater;
//
//
//    }
//
//    @Override
//    public View getInfoWindow(Marker marker) {
//        return (null);
//    }
//
//    @Override
//    public View getInfoContents(Marker marker) {
//        View popup = inflater.inflate(R.layout.marker_add, null);
//        return (popup);
//
//    }
//}

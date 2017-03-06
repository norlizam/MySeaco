package jasonpaulraj.myseaco;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class MapsOnlineListDetail extends Activity implements View.OnClickListener {

    private static final String TAG = "MapsOnlineListDetail";
    public String action = "common";
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private Button buttonPrev, buttonNext;
    TextView textPageNumber;
    String mapsIdHistory, mapsId, mapsBarcode, mapsLatitude, mapsLongitude, mapsAddress, mapsFullAddress2, mapInsertBy, mapHouseCreatedDate, mapsModifiedBy,mapsModifiedDate, JSON_STRING;
    Integer currentPage, totalLocationPerUser;
    MySQLiteHelper db;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_detail);

        //get Intent
        mapsIdHistory = getIntent().getExtras().getString("mapsId");
        mapsBarcode = getIntent().getExtras().getString("mapsBarcode");
        mapsLatitude = getIntent().getExtras().getString("mapsLatitude");
        mapsLongitude = getIntent().getExtras().getString("mapsLongitude");
        mapsAddress = getIntent().getExtras().getString("mapsAddress");
        mapsFullAddress2 = getIntent().getExtras().getString("mapsFullAddress2");
        mapHouseCreatedDate = getIntent().getExtras().getString("mapHouseCreatedDate");
        mapInsertBy = getIntent().getExtras().getString("mapInsertBy");

        tableLayout = (TableLayout) findViewById(R.id.tableLayoutEdit);

        // set the custom dialog components - text, image and button
        TextView mapsOffline_id_dialog  = (TextView) findViewById(R.id.mapsOffDetail_id_dialog);
        TextView mapsOffline_seacoBarcode_dialog  = (TextView) findViewById(R.id.mapsOffDetail_barcode_dialog);
        TextView mapsOffline_latlon_dialog  = (TextView) findViewById(R.id.mapsOffDetail_latlon_dialog);
        TextView mapsOffline_address_dialog  = (TextView) findViewById(R.id.mapsOffDetail_address_dialog);
        TextView mapsOffline_address2_dialog  = (TextView) findViewById(R.id.mapsOffDetail_address2_dialog);
        TextView mapsOffline_createDate_dialog  = (TextView) findViewById(R.id.mapsOffDetail_createdDate_dialog);
        TextView mapsOffline_createBy_dialog  = (TextView) findViewById(R.id.mapsOffDetail_createdBy_dialog);

        mapsOffline_id_dialog.setText(mapsIdHistory);
        mapsOffline_seacoBarcode_dialog.setText(mapsBarcode);
        mapsOffline_latlon_dialog.setText(mapsLatitude+","+mapsLongitude);
        mapsOffline_address_dialog.setText(mapsFullAddress2);
        mapsOffline_address2_dialog.setText(mapsAddress);
        mapsOffline_createDate_dialog.setText(mapHouseCreatedDate);
        mapsOffline_createBy_dialog.setText(mapInsertBy);

        //button prev, next and search
        buttonPrev = (Button) findViewById(R.id.buttonPrevious);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                action = "prev";
                currentPage = getCurrentPage(action);
                getJSON(mapsIdHistory, currentPage);
                getTotalPage(mapsIdHistory, currentPage);

            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                action = "next";
                currentPage = getCurrentPage(action);
                Log.d(TAG,"NEXT 1:["+currentPage+"]");
                getJSON(mapsIdHistory, currentPage);
                getTotalPage(mapsIdHistory, currentPage);

            }
        });

        //first time load
        currentPage = getCurrentPage(action);
        getJSON(mapsIdHistory, currentPage);
        getTotalPage(mapsIdHistory, currentPage);
        //end first time load

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
    }

    // To retrieve data from database
    private void getJSON(final String mapsId, final Integer currentPage){
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                //showPhotosCommon(rootView, inflater, buttonPrev, buttonNext);
                displayOnlinePerUserListDetail();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramSearch = new HashMap<String, String>();
                paramSearch.put(Config.KEY_ID,mapsId);
                paramSearch.put(Config.KEY_SEARCH_CURRPAGE,currentPage.toString());

                Log.d(TAG,"mapsId["+mapsId+"] currentPage ["+currentPage.toString()+"]");

                String s = rh.sendPostRequest(Config.URL_ALL_LOCATION_DETAIL,paramSearch);
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    //show all data firstime enter this page
    private void displayOnlinePerUserListDetail(){

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject
                    .getJSONArray(Config.TAG_JSON_ARRAY);

            //total data
            totalLocationPerUser =result.length();

            textPageNumber = (TextView)findViewById(R.id.pageNumber);

            int i = 0;

            //start refresh layout
            tableLayout.removeAllViews();
            tableLayout.refreshDrawableState();
            //end refresh layout

            while(i<result.length()){

                JSONObject jo = result.getJSONObject(i);
                mapsId = jo.getString(Config.TAG_ID);
                mapsFullAddress2 = jo.getString(Config.TAG_ADDRESS2);
                mapsModifiedDate = jo.getString(Config.TAG_ADDRESS2_MODIFIEDDATE);
                mapsModifiedBy = jo.getString(Config.TAG_ADDRESS2_MODIFIEDBY);

                linearLayout = (LinearLayout) View.inflate(this,R.layout.table_mapsonline_list_edit_item, null);

                TextView mapsonline_date  = (TextView) linearLayout.findViewById(R.id.mapsonline_date);
                TextView mapsOffline_address2  = (TextView) linearLayout.findViewById(R.id.mapsonline_seacoAddress);

                mapsonline_date.setText(mapsModifiedDate);
                mapsOffline_address2.setText(mapsFullAddress2);

                i++;
                tableLayout.addView(linearLayout);
            }

        } catch (JSONException e) {
        }
    }

    // get total page from database
    private void getTotalPage(final String mapsId, final Integer currentPage) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                JSON_STRING = s;

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject
                            .getJSONArray(Config.TAG_JSON_ARRAY);

                    JSONObject jo = result.getJSONObject(0);
                    String pageCount = jo.getString(Config.TAG_PAGE_COUNT);

                    textPageNumber.setText("Page "+(currentPage)+" of "+pageCount);

                    // Disabled Button Next
                    int pageCounts =Integer.parseInt(pageCount);

                    if(currentPage >= pageCounts){
                        buttonNext.setEnabled(false);
                    }else{
                        buttonNext.setEnabled(true);
                    }

                    // Disabled Button Previous
                    if(currentPage <= 1){
                        buttonPrev.setEnabled(false);
                    }else{
                        buttonPrev.setEnabled(true);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramSearch = new HashMap<String, String>();
                paramSearch.put(Config.KEY_ID,mapsId);
                paramSearch.put(Config.KEY_SEARCH_CURRPAGE,currentPage.toString());

                String s = rh.sendPostRequest(Config.URL_ALL_LOCATION_DETAIL_PAGE,paramSearch);

                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    //return current page
    public Integer getCurrentPage(String action){
        if(action.equalsIgnoreCase("common")){
            currentPage = 1;
        }
        if(action.equalsIgnoreCase("next")){
            currentPage++;
        }
        if(action.equalsIgnoreCase("prev")){
            currentPage--;
        }
        return currentPage;
    }
    //end return current page



}


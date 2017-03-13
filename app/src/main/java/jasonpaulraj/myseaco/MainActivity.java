package jasonpaulraj.myseaco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    //private Button btn_viewMap, btn_editMap, btn_editMapOff;
    private ImageButton imgBtnLoc, imgBtnAddLoc, imgBtnListLoc;
    private RadioGroup radGrpModeType;
    private RadioButton radBtnModeOn, radBtnModeOff;
    public final String TAG = "MainActivity";
    GPSTracker gps;
    String userID, userFullName, userName, userPassword, userRegtype, radModeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgBtnLoc = (ImageButton) findViewById(R.id.imgBtnViewLoc);
        imgBtnAddLoc = (ImageButton) findViewById(R.id.imgBtnAddLoc);
        imgBtnListLoc = (ImageButton) findViewById(R.id.imgBtnViewList);

        //get Intent
        userID = getIntent().getExtras().getString("userID");
        userFullName = getIntent().getExtras().getString("userFullName");
        userName = getIntent().getExtras().getString("userName");
        userPassword = getIntent().getExtras().getString("userPassword");
        userRegtype = getIntent().getExtras().getString("userRegtype");
        radModeType = getIntent().getExtras().getString("radModeType");

        Log.d(TAG,"userID["+userID+"] fullname["+userFullName+"] userName["+userName+"] userPassword["+userPassword+"] regType ["+userRegtype+"]");

        gps = new GPSTracker(this);

        imgBtnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ENTER VIEW LOCATION");
                Intent intent = new Intent(MainActivity.this, BarcodeScanner3.class);
                //Intent intent = new Intent(MainActivity.this, BarcodeScanner2.class);
                /*Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userRegtype", userRegtype);
                intent.putExtra("radModeType", radModeType);*/
                startActivity(intent);
            }
        });


        imgBtnAddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ENTER ADD LOCATION");
                viewModeDialog();
            }
        });

        imgBtnListLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ENTER VIEW LIST LOCATION");
                Intent intent = new Intent(MainActivity.this, MapsOnlineList.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userRegtype", userRegtype);
                intent.putExtra("radModeType", radModeType);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //view edit maps dialog
    public void viewModeDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_custom_mode);
        dialog.setTitle("Mode");

        // set the custom dialog components - text, image and button
        radGrpModeType = (RadioGroup) dialog.findViewById(R.id.radGrpModeType);

        radBtnModeOn = (RadioButton) dialog.findViewById(R.id.radBtnMode_Online);
        radBtnModeOff = (RadioButton) dialog.findViewById(R.id.radBtnMode_Offline);

        //radio button function
        radGrpModeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radBtnMode_Online:
                        Intent intentOn = new Intent(MainActivity.this, MapsEditOnline.class);
                        intentOn.putExtra("userID", userID);
                        intentOn.putExtra("userRegtype", userRegtype);
                        intentOn.putExtra("radModeType", radModeType);
                        startActivity(intentOn);
                        dialog.dismiss();
                        break;

                    case R.id.radBtnMode_Offline:
                        Intent intentOff = new Intent(MainActivity.this, MapsOfflineList.class);
                        intentOff.putExtra("userID", userID);
                        intentOff.putExtra("userRegtype", userRegtype);
                        intentOff.putExtra("radModeType", radModeType);
                        startActivity(intentOff);
                        dialog.dismiss();
                        break;
                }
            }
        });

        dialog.show();
    }
    //end view dialog mode
}

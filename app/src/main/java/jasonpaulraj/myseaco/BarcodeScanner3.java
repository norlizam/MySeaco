package jasonpaulraj.myseaco;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class BarcodeScanner3 extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    Button btnBarScanner;
    TextView txtViewBarcode;

    //qr code scanner object
    private IntentIntegrator qrScan;

    public static final String TAG="BarcodeScanner3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        //View objects
        btnBarScanner = (Button) findViewById(R.id.btnBarScanner);
        txtViewBarcode = (TextView) findViewById(R.id.txtViewBarScannerVal);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        btnBarScanner.setOnClickListener(this);

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    /*textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));*/
                    Log.d(TAG,"enter 1");
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    txtViewBarcode.setText(result.getContents());
                    Log.d(TAG,"enter 2["+result.getContents().toString()+"]");

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
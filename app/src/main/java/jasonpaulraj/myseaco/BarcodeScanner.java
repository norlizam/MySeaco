package jasonpaulraj.myseaco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BarcodeScanner extends Activity {

    public final String TAG = "BarcodeScanner";
    Button btnBarScanner;
    TextView txtViewBarcode;
    IntentIntegrator scanIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        btnBarScanner = (Button) findViewById(R.id.btnBarScanner);
        txtViewBarcode = (TextView) findViewById(R.id.txtViewBarcode);

        //1. Create class IntentIntegrator
        //2. Create class IntentResult
        //3. Add function to view scanner
        btnBarScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanIntegrator.initiateScan();
            }
        });
        //end Add function to view scanner
        //4. Add method onActivityResult() in this class
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Log.d(TAG,"scanContent: ["+scanContent+"] scanFormat [");
            //formatTxt.setText("FORMAT: " + scanFormat);
            //contentTxt.setText("CONTENT: " + scanContent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}

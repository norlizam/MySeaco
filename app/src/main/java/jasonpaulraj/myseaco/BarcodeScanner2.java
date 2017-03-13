package jasonpaulraj.myseaco;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BarcodeScanner2 extends AppCompatActivity {

    static final String SCAN = "com.google.zxing.client.android";
    Button btnBarScanner;
    TextView txtViewBarcode;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.scanner );
        //Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        //setSupportActionBar( toolbar );
        btnBarScanner = (Button) findViewById(R.id.btnBarScanner);
        txtViewBarcode = (TextView) findViewById(R.id.txtViewBarcode);

        btnBarScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Snackbar.make( v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction( "Action", null ).show();
                ScanBar(v);
            }
        });

    }
    // fucntion to scan barcode
    public void ScanBar ( View view ) {
        try {
            //this intent is used to call start for bar code
            Intent in = new Intent( SCAN );
            in.putExtra( "SCAN_MODE", "PRODUCT_MODE" );
            startActivityForResult( in, 0 );
        } catch ( ActivityNotFoundException e) {
            showDialog( BarcodeScanner2.this,"No scanner found", "Download Scanner code Activity?"," Yes", "No" ).show();
        }
    }
    private Dialog showDialog ( final Activity act, CharSequence title,CharSequence message, CharSequence yes, CharSequence no ) {
        // a subclass of dialog that can display buttons and message
        AlertDialog.Builder download = new AlertDialog.Builder( act );
        download.setTitle( title );
        download.setMessage ( message );
        download.setPositiveButton ( yes, new DialogInterface.OnClickListener ( ) {
            @Override
            public void onClick( DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
                //uri to download barcode scanner
                Uri uri = Uri.parse( "market://search?q=pname:" + "com.google.zxing.client.android" );
                Intent in = new Intent ( Intent.ACTION_VIEW, uri );
                try {
                    act.startActivity ( in );
                } catch ( ActivityNotFoundException e) {
                }
            }
        });
        download.setNegativeButton ( no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick ( DialogInterface dialog, int i ) {
                // TODO Auto-generated method stub
            }
        });
        return download.show();
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent in ) {
        // TODO Auto-generated method stub
        if( requestCode == 0 ){
            if( resultCode == RESULT_OK ){
                //use to get scan result
                String contents = in.getStringExtra( "SCAN_RESULT" );
                String format =  in.getStringExtra( "SCAN_RESULT_FORMAT" ) ;
                Toast toast = Toast.makeText( this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG );
                toast.show();
            }
        }
    }
}
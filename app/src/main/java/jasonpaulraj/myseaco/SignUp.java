package jasonpaulraj.myseaco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    public final String TAG = "SignUp";

    private Button btnCreateAccount,btnBack;
    public String JSON_STRING;
    String actionType, editTxtNameVal, editTxtUsernameVal, editTxtUserpassVal, userRegtypeVal;
    EditText editTxtName, editTxtUsername, editTxtUserpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //button create Account, back, edit text
        btnCreateAccount = (Button) findViewById(R.id.btn_createAccount);
        btnBack = (Button) findViewById(R.id.btn_back);
        editTxtName = (EditText) findViewById(R.id.editTxt_fullName);
        editTxtUsername = (EditText) findViewById(R.id.editTxt_username);
        editTxtUserpass = (EditText) findViewById(R.id.editTxt_userPassword);

        //start listener for button create Account, back
        btnCreateAccount.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //start get editText value
                        editTxtNameVal = editTxtName.getText().toString();
                        editTxtUsernameVal = editTxtUsername.getText().toString();
                        editTxtUserpassVal = editTxtUserpass.getText().toString();
                        userRegtypeVal = "S0"; //DEFAULT FOR ALL STAFF S0: ALL STAFF S1:TL
                        //end get editText value

                        createAccount(editTxtNameVal, editTxtUsernameVal, editTxtUserpassVal, userRegtypeVal);
                    }
                }
        );

        btnBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back();
                    }
                }
        );
        //end listener for button

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

    //start btn create Account function
    private void createAccount(final String editTxtNameVal, final String editTxtUsernameVal, final String editTxtUserpassVal, final String userRegtypeVal){

        actionType = "S";//SIGNUP

        if (!validateSignup()) {
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //check user if their username and password already have at db
                        Log.d(TAG,"CreateAccount() :FULL NAME ["+editTxtNameVal+"] USERNAME ["+editTxtUsernameVal+"] PASSWORD ["+editTxtUserpassVal+"] REGTYPE ["+userRegtypeVal+"]");
                        addUser(editTxtNameVal,editTxtUsernameVal, editTxtUserpassVal, userRegtypeVal, progressDialog);
                    }
                    //					}
                }, 3000);

    }
    //end btn create Account function

    //start btn back function
    private void back() {
        Intent i = new Intent(SignUp.this, SignIn.class);
        startActivity(i);
        finish();
    }
    //end btn back function

    //start validate
    public boolean validateSignup() {

        Log.d(TAG,"enter validate signup method");

        boolean valid = true;

        if (editTxtNameVal.isEmpty() || editTxtNameVal.length() < 3) {
            editTxtName.setError("Please enter at least 3 characters");
            valid = false;
        }else{
            editTxtName.setError(null);
        }

        if (editTxtUsernameVal.isEmpty() || editTxtUsernameVal.length() < 3) {
            editTxtUsername.setError("Please enter at least 3 characters");
            valid = false;
        }else{
            editTxtUsername.setError(null);
        }

        if (editTxtUserpassVal.isEmpty() || editTxtUserpassVal.length() < 4 || editTxtUserpassVal.length() > 10) {
            editTxtUserpass.setError("Please enter between 4 and 10 alphanumeric characters");
            valid = false;
        }else{
            editTxtUserpass.setError(null);
        }
        return valid;
    }
    //end validate

    //start clear textfield
    public void emptyInputVal(String actionType){

        if(actionType.equalsIgnoreCase("L")){
            //textfield login
            editTxtUsername.setText("");
            editTxtUserpass.setText("");
        }else{
            //textfield signup
            editTxtName.setText("");
            editTxtUsername.setText("");
            editTxtUserpass.setText("");
        }
    }
    //end clear textfield

    //start Check and Insert User
    private void addUser(final String editTxtNameVal, final String editTxtUsernameVal, final String editTxtUserpassVal, final String userRegtypeVal, final ProgressDialog progressDialog){

        class AddUser extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"postExecute checkUser");

                JSON_STRING = s;
                Log.d(TAG,"onPostExecute ["+s+"]");

                if(s.contains("success")){
                    //add if data not already in db
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Signup Successfully", Toast.LENGTH_LONG).show();
                    emptyInputVal(actionType);
                    Intent i = new Intent(SignUp.this, SignIn.class);
                    startActivity(i);
                    finish();
                }else{
                    //fail if data already in db
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Signup failed. Your ID is already exist.", Toast.LENGTH_LONG).show();
                    emptyInputVal(actionType);
                    Intent i = new Intent(SignUp.this, SignIn.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                Log.d(TAG,"doInBackground() :FULL NAME ["+editTxtNameVal+"] USERNAME ["+editTxtUsernameVal+"] PASSWORD ["+editTxtUserpassVal+"] REGTYPE ["+userRegtypeVal+"]");
                HashMap<String,String> paramUser = new HashMap<String, String>();
                paramUser.put(Config.KEY_USER_FULLNAME,editTxtNameVal);
                paramUser.put(Config.KEY_USER_NAME,editTxtUsernameVal);
                paramUser.put(Config.KEY_USER_PASSWORD,editTxtUserpassVal);
                paramUser.put(Config.KEY_USER_Regtype,userRegtypeVal);

                String s = rh.sendPostRequest(Config.URL_ADD_USER,paramUser);
                Log.d(TAG,"doInBackground ["+s+"]");
                return s;
            }
        }
        AddUser addUser = new AddUser();
        addUser.execute();
    }

}

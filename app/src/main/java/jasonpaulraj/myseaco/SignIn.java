package jasonpaulraj.myseaco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;

import jasonpaulraj.myseaco.module.MySQLiteHelper;

public class SignIn extends AppCompatActivity {

    public final String TAG = "SignIn";

    private Button login_button, register_button;
    RadioGroup radBtnOnlineMode;
    public String JSON_STRING;
    String actionType, editTxtNameVal, editTxtUsernameVal, editTxtUserpassVal, userRegtypeVal, radModeType;
    EditText editTxtName, editTxtUsername, editTxtUserpass;
    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //button create Login, Register, edit text
        register_button  = (Button) findViewById(R.id.button_register);
        login_button = (Button)findViewById(R.id.button_login);
        editTxtUsername = (EditText) findViewById(R.id.editTxt_username);
        editTxtUserpass = (EditText) findViewById(R.id.editTxt_password);
        radBtnOnlineMode = (RadioGroup) findViewById(R.id.radOnlineMode);

        radModeType ="Y";//set onlineMode value


        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //start get editText value
                        editTxtUsernameVal = editTxtUsername.getText().toString();
                        editTxtUserpassVal = editTxtUserpass.getText().toString();
                        //end get editText value

                        login(editTxtUsernameVal, editTxtUserpassVal);
                    }
                }
        );

        register_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignIn.this, SignUp.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        //radio button function
        radBtnOnlineMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radBtnOn:
                        radModeType = "Y"; //set language value
                        break;

                    case R.id.radBtnOff:
                        radModeType = "N"; //set language value
                        break;
                }
            }
        });

        db = new MySQLiteHelper(this);

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

    //start btn Login function
    public void login(final String editTxtUsernameVal, final String editTxtUserpassVal){

        Log.d(TAG,"enter login method");

        actionType = "L";//LOGIN

        if (!validateLogin()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait. Your authentication is in progress");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //checking if user select online mode, proceed with checking in live DB OTHERWISE checking in local DB
                        if (radModeType.equalsIgnoreCase("Y")){
                            checkUser(editTxtUsernameVal, editTxtUserpassVal, progressDialog);
                        }else{
                            checkUserinLocalDevice(editTxtUsernameVal, editTxtUserpassVal, progressDialog);
                        }
                        //end checking
                    }
                    //}
                }, 3000);
    }
    //end btn Login function

    //start validate
    public boolean validateLogin() {

        Log.d(TAG,"enter validate signup method");

        boolean valid = true;

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

    //start checkuser in db
    private void checkUser(final String editTxtUsernameVal, final String editTxtUserpassVal, final ProgressDialog progressDialog){
        class CheckUser extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG,"postExecute checkUser");

                JSON_STRING = s;

                try {
                    if(s.contains("failure")){
                        progressDialog.dismiss();

                        Toast.makeText(getBaseContext(), "Please signup. You are not register yet.", Toast.LENGTH_LONG).show();
                        emptyInputVal(actionType);
                    }else{

                        //get data from live DB
                        Users users = Users.myMethod(s);

                        //save to own device
                        Log.d(TAG,"users["+users.getUserId()+"] fullname["+users.getUserFullName()+"] username["+users.getUserName()+"] password["+users.getUserPassword()+"] regtype["+users.getUserRegtype()+"] regDate["+users.getRegDate()+"]");

                        db.checkUsernInsertInDB(users.getUserId(), users.getUserFullName(), users.getUserName(), users.getUserPassword(), users.getUserRegtype(), users.getRegDate());
                        Toast.makeText(getBaseContext(), "Successfully login", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(SignIn.this, MainActivity.class);
                        i.putExtra("userID", users.getUserId());
                        i.putExtra("userFullName", users.getUserFullName());
                        i.putExtra("userName", users.getUserName());
                        i.putExtra("userPassword", users.getUserPassword());
                        i.putExtra("userRegtype", users.getUserRegtype());
                        i.putExtra("radModeType", radModeType);

                        startActivity(i);
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String,String> paramUser = new HashMap<String, String>();
                //paramUser.put(Config.KEY_USER_EMAIL,userEmail);
                paramUser.put(Config.KEY_USER_NAME,editTxtUsernameVal);
                paramUser.put(Config.KEY_USER_PASSWORD,editTxtUserpassVal);

                String s = rh.sendPostRequest(Config.URL_CHECK_USER,paramUser);
                Log.d(TAG,"doInBackground ["+s+"]");
                return s;
            }
        }
        CheckUser checkUser = new CheckUser();
        checkUser.execute();
    }
    //end checkuser in db

    public void checkUserinLocalDevice(final String userName, final String userPassword, final ProgressDialog progressDialog){

        boolean existInDevice = db.checkUserInDB(userName, userPassword);

        if(!existInDevice){

            progressDialog.dismiss();

            Toast.makeText(getBaseContext(), "Please signup. You are not register yet.", Toast.LENGTH_LONG).show();
            emptyInputVal(actionType);

        }else{

            //get data from local DB
            UserDetail singleUser = db.getUserDetail(userName, userPassword);

            Toast.makeText(getBaseContext(), "Successfully login", Toast.LENGTH_LONG).show();

            Intent i = new Intent(SignIn.this, MainActivity.class);

            //get Intent
            i.putExtra("userID", singleUser.getUserId());
            i.putExtra("userFullName", singleUser.getUserFullName());
            i.putExtra("userName", singleUser.getUserName());
            i.putExtra("userPassword", singleUser.getUserPassword());
            i.putExtra("userRegtype", singleUser.getUserRegtype());
            i.putExtra("radModeType", radModeType);
            startActivity(i);
            progressDialog.dismiss();
        }
    }

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
}
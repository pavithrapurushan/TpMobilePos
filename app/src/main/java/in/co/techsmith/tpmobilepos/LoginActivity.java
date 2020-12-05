package in.co.techsmith.tpmobilepos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

//Modified by Pavithra on 07-07-2020
//Modified by Pavithra on 09-07-2020
//Modified by Pavithra on 15-07-2020
//Modified by Pavithra on 22-07-2020
//Modified by Pavithra on 23-07-2020
//Modified by Pavithra on 29-07-2020
//Modified by Pavithra on 31-07-2020
//Modified by Pavithra on 25-09-2020
//Modified by Pavithra on 30-09-2020

public class LoginActivity extends AppCompatActivity {

    EditText etBillingDate;
    EditText etUserName;
    EditText etPassword;

    String strFromValidateUser = "";
    String strErrorMsg = "";

    String user_name = "";
    String password = "";
    String bill_date = "";

    SharedPreferences prefs;

    TextView tvValidateLoginMsg; //Added by Pavithra on 09-07-2020
    TsCommonMethods tsCommonMethods;

    String Device_id = "";

    ImageButton imgBtnSettings;

    TextView tvBiilingDate;  //Added by Pavithra on 05-08-2020


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.d("LA", "" + e);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_login);

//        String strUUID  = String.valueOf( UUID.randomUUID());
//        strUUID = strUUID.replace("-", "");
//        String DeviceUniqueId = strUUID.substring(0,15).toUpperCase();

        tsCommonMethods = new TsCommonMethods(LoginActivity.this);
        tsCommonMethods.allowPermissionsDynamically(); //Added by Pavithra on 14-09-2020
        Device_id = tsCommonMethods.GetDeviceUniqueId();

//        Toast.makeText(this, "" + tsCommonMethods.GetDeviceUniqueId(), Toast.LENGTH_SHORT).show();
//        Log.d("LA", "DeviceId = " + tsCommonMethods.GetDev    iceUniqueId());

        etBillingDate = (EditText) findViewById(R.id.etBillingDate);
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvValidateLoginMsg = (TextView) findViewById(R.id.tvValidateLoginMsg); //Added by Pavithra on 09-07-2020
        imgBtnSettings = (ImageButton) findViewById(R.id.imgBtnSettings); //Added by Pavithra on 31-07-2020

        tvBiilingDate = (TextView)findViewById(R.id.tvBiilingDate); //Added by Pavithra on 05-08-2020

        float textsize = tvBiilingDate.getTextSize();
        Toast.makeText(this, ""+textsize, Toast.LENGTH_SHORT).show();
        Log.d("LA",""+textsize);

        //added by Pavithra on 31-07-2020
        imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intnt);
            }
        });
    }

    public void login(View view) {

        user_name = etUserName.getText().toString();
        password = etPassword.getText().toString();
        bill_date = etBillingDate.getText().toString();

        if (user_name.equals("") || password.equals("") || bill_date.equals("")) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            if (Device_id.equalsIgnoreCase("") || Device_id == null) {
                Device_id = tsCommonMethods.GetDeviceUniqueId();
            }
            new MobPOSValidateUserAPITask().execute();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        tsMessage("Do You want to exit app?");
    }

    private class MobPOSValidateUserAPITask extends AsyncTask<String,String,String> {

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("processing...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            mobPOSValidateUser();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(progressDialog.isShowing())
                progressDialog.dismiss();

            if (strFromValidateUser.equals("httperror")) {
//                    tsMessages(strErrorMsg);
                tsErrorMessage("Http error occured\n\n"+strErrorMsg);
                Toast.makeText(LoginActivity.this, "" + strErrorMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (strFromValidateUser.equals("") || strFromValidateUser == null) {
                Toast.makeText(LoginActivity.this, "No result  from web", Toast.LENGTH_SHORT).show();
                tsErrorMessage(""+strErrorMsg);
            } else {
                Gson gson = new Gson();
                ValidateUserResponse validateUserrObj = gson.fromJson(strFromValidateUser, ValidateUserResponse.class);
                if (validateUserrObj.ErrorStatus == 0) {
                    tvValidateLoginMsg.setText("");  //Added by Pavithra on 09-07-2020
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this); //Added by Pavithra on 09-07-2020
                    SharedPreferences.Editor editor = mPrefs.edit();//Added by Pavithra on 09-07-2020
                    editor.clear();   //Added by Pavithra on 09-07-2020
                    editor.commit();   //Added by Pavithra on 09-07-2020

                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

/*************************************************************************************************************************************/
                    //Added by Pavithra on 10-07-2020
                    prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    editor = prefs.edit();
                    editor.putString("BillingDate",bill_date);
                    editor.putString("DeviceId",Device_id);
                    editor.putInt("CounterId",validateUserrObj.CounterId); //Added by Pavithra on 23-07-2020
                    editor.putInt("ShiftId",validateUserrObj.ShiftId);     //Added by Pavithra on 23-07-2020
                    editor.commit();

/*************************************************************************************************************************************/

                    Intent loginintent = new Intent(LoginActivity.this, customtoolbar.class);
                    startActivity(loginintent);
                    finish();  //Added by Pavithra on 15-07-2020

                } else {
//                    Toast.makeText(LoginActivity.this, "" + validateUserrObj.Message, Toast.LENGTH_SHORT).show();
//                    tvValidateLoginMsg.setText("Username or Password incorrect");  //Added by Pavithra on 09-07-2020  //Commented by pavithra on 29-07-2020
                    tsErrorMessage(validateUserrObj.Message);  //Added by Pavithra on 29-07-2020
                    String userName = validateUserrObj.UserName;
                    String userId = validateUserrObj.UserId;
                }
            }
        }
    }

    private void  mobPOSValidateUser() {

        try {
            strErrorMsg = "";    //added by Pavithra on 25-09-2020
            strFromValidateUser = "";    //added by Pavithra on 25-09-2020
            //Added by Pavithra on 23-07-2020
            User usrObj = new User();
//            usrObj.UserId = user_name;
//            usrObj.Password = password;
//            usrObj.BillDate = bill_date;
//            usrObj.DeviceId = Device_id;

            UserPL userPLObj = new UserPL();
            userPLObj.User = usrObj;

            Gson gson = new Gson();
            String userObjJsonstr = gson.toJson(userPLObj);


            Login loginObj = new Login();
            loginObj.StoreCode = "OP02";
            loginObj.SubStoreCode = "MAIN";
            loginObj.UserId = user_name;
            loginObj.Password = password;
            loginObj.Date = bill_date;
            loginObj.DeviceId = Device_id;
            loginObj.Session = "ABC";

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.Login = loginObj;

            gson = new Gson();
            String loginRequestJsonStr = gson.toJson(loginRequest);

//            URL url = new URL(AppConfig.app_url + "ValidateUser"); //give product id as filter
//            URL url = new URL(AppConfig.app_url + "UserValidation"); //give product id as filter
            URL url = new URL("http://tsmith.co.in/MobPOS2/api/UserValidation"); //give product id as filter

//            http://tsmith.co.in/MobPOS2/api/UserValidation
//            Api Url : http://tsmith.co.in/MobPOS2/api/UserValidation //Added by Pavithra on 23-11-2020


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(30000);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("auth_key", "6E5C3126-B09C-4236-8E57-73C11BB64106"); //Modified by PAvithra on 23-11-2020
//            connection.setRequestProperty("auth_key", "BFD2E5AC-101F-47ED-AB49-C2D18EE5EA97");
            connection.setRequestProperty("user_key", "");

            //Following 3 lines commented by Pavithra on 23-07-2020
//            connection.setRequestProperty("user_name", user_name);
//            connection.setRequestProperty("pswd", password);
//            connection.setRequestProperty("bill_date", bill_date);//Added by Pavithra on 22-07-2020

            //Added by Pavithra on 23-07-2020

//            {"User":{"BillDate":"23/07/2020","DeviceId":"1EAF283696504E7","Password":"abcd","UserId":"Admin"}}
//            connection.setRequestProperty("xml_str", userObjJsonstr);  //Commented by Pavithra on 30-09-2020
            connection.connect();

//            {"Login":{"Date":"18/11/2020","DeviceId":"DB524156498D4B5","Password":"tspltpad","Session":"ABC","StoreCode":"OP02","SubStoreCode":"MAIN","UserId":"Admin"}}

            //following added by 30-09-2020
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//            wr.writeBytes(userObjJsonstr); //Commented by Pavithra on 23-11-2020
            wr.writeBytes(loginRequestJsonStr); //Added by Pavithra on 23-11-2020
            wr.flush();
            wr.close();

            int responsecode = connection.getResponseCode();   //added by Pavithra on 25-09-2020
            if(responsecode == 200) {
                try {
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder sb = new StringBuilder();
                    String inputLine = "";

                    while ((inputLine = reader.readLine()) != null) {
                        sb.append(inputLine);
                        break;
                    }

                    reader.close();
                    String result = sb.toString();
                    strFromValidateUser = result;

//                {"UserId":"Admin","UserName":"Admin","Id":1,"ErrorStatus":0,"Message":"Validation is Successfull"}
                } finally {
                    connection.disconnect();
                }
            }else{
                strErrorMsg = connection.getResponseMessage();
                strFromValidateUser="httperror";
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            strErrorMsg = e.getMessage();
        }
    }


    //Added by Pavithra on 29-07-2020
    public void tsErrorMessage(String error_massage) {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.custom_save_popup);
        final String title = "Message";

        TextView dialogTitle = (TextView) dialog.findViewById(R.id.txvSaveTitleDialog);
        dialogTitle.setText(title);
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//Commented by Pavithra on 20-08-2020 followin 2 lines
//        int height_of_popup = 500;
//        int width_of_popup = 400;
//        int height_of_popup = 700;
//        int width_of_popup = 500;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screen_height = displayMetrics.heightPixels;
        int screen_width = displayMetrics.widthPixels;



        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (screen_width * 0.3f);
        int dialogWindowHeight = (int) (screen_height * 0.3f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);


//        int height_of_popup = (int) ((screen_height * 40) / 100);
//        int width_of_popup = (int) ((screen_width * 25) / 100);


//        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
//
//        dialog.getWindow().setLayout(width,  ViewGroup.LayoutParams.MATCH_PARENT);


//        dialog.getWindow().setLayout(width_of_popup, height_of_popup);
        dialog.show();

        final TextView tvSaveStatus = (TextView) dialog.findViewById(R.id.tvSaveStatus);
//        tvSaveStatus.setText("Successfully saved \n Token No = "+tokenNo);
        tvSaveStatus.setText("" + error_massage);

        Button btnOkPopup = (Button) dialog.findViewById(R.id.btnOkPopUp);

        btnOkPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public void tsMessage(String msg) {
        try {
            AlertDialog.Builder b = new AlertDialog.Builder(LoginActivity.this);
            b.setTitle("Exit App");
            b.setMessage(msg);
            b.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

//                    if (android.os.Build.VERSION.SDK_INT >= 21) {
//                        finishAndRemoveTask();
//                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                        homeIntent.addCategory(Intent.CATEGORY_HOME);
//                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(homeIntent);
////                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                    } else {
//
//                        finish();
////                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
////                        homeIntent.addCategory(Intent.CATEGORY_HOME);
////                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        startActivity(homeIntent);
////                        android.os.Process.killProcess(android.os.Process.myPid());
//                        System.exit(1);
//                    }
                }
            });

            b.setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.show();

        }catch(Exception ex){
            Toast.makeText(LoginActivity.this,""+ex.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        DatePickerFragment.setDatePickerFragment(LoginActivity.this, etBillingDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        static Context cntxt;
        static EditText editText;
        //    @SuppressLint("ValidFragment")
        public static void setDatePickerFragment(Context cxt,EditText et){
            cntxt = cxt;
            editText = et;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            int month= monthOfYear+1;
            String fm = ""+month;
            String fd = ""+dayOfMonth;

            if(month < 10) {
                fm = "0" + month;
            }
            if (dayOfMonth < 10) {
                fd = "0" + dayOfMonth;
            }

            String date = ""+fd+"/"+fm+"/"+year;
            editText.setText(date);

        }
    }
}

package com.underwaterdivingtech.myapp13;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edUserid = findViewById(R.id.edt_name);
        SharedPreferences setting = getSharedPreferences("atm", MODE_PRIVATE);
        edUserid.setText(setting.getString("PREF_USERID", ""));
    }

    public void login(View v){
        EditText edtName = (EditText) findViewById(R.id.edt_name);
        EditText edtPassword = (EditText) findViewById(R.id.edt_password);
        String uid = edtName.getText().toString();
        String pw = edtPassword.getText().toString();
        SharedPreferences setting =
                getSharedPreferences("atm", MODE_PRIVATE);
        setting.edit().putString("PREF_USERID", uid).apply();

        String url = new StringBuilder(
                "http://atm201605.appspot.com/login?uid=")
                .append(uid)
                .append("&pw=")
                .append(pw)
                .toString();
        new LoginTask().execute(url);
    }



    class LoginTask extends AsyncTask<String, Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean logon = false;
            try{
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                int data = is.read();
                Log.d("HTTP", String.valueOf(data));
                if(data == 49){
                    logon = true;
                }
                is.close();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return logon;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){
                Toast.makeText(LoginActivity.this, "登入成功",
                        Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, getIntent());
                finish();
            }else{
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Atm")
                        .setMessage("登入失敗")
                        .setPositiveButton("OK",null)
                        .show();
            }
        }

        public void cancel(View v){
        }
    }
}

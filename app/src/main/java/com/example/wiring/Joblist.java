package com.example.wiring;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;


public class Joblist extends AppCompatActivity {
    JSONObject jsonObj;
    ProgressDialog pDialog;
    CheckBox checkLabel;
    CheckBox checkWrapping;
    CheckBox checkRegulator;
    EditText txtKeterangan;
    EditText txtSolusi;
    Spinner spinStatus;
    JSONParse checkListTask;
    TextView txtDeviceTag;
    TextView txtID;
    String main_wiring;
    private String Label, Wrapping, Regulator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joblist);

        txtDeviceTag = (TextView) findViewById(R.id.txtDeviceTag);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtID = (TextView) findViewById(R.id.txtID);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        txtKeterangan = (EditText) findViewById(R.id.txtKeterangan);
        txtSolusi = (EditText) findViewById(R.id.txtSolusi);
        spinStatus = (Spinner) findViewById(R.id.spinStatus);
        String devicetag = getIntent().getStringExtra("devicetag").toString();
        String description = getIntent().getStringExtra("description").toString();
        String id = getIntent().getStringExtra("id").toString();
        String status = getIntent().getStringExtra("status").toString();
        main_wiring = getIntent().getStringExtra("main_wiring").toString();
        txtDeviceTag.setText(devicetag);
        spinStatus.setSelection(getIndex(spinStatus, status));
        txtDescription.setText(description);
        txtID.setText(id);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkListTask = new JSONParse();
                    checkListTask.execute((Void) null);
                    //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
                }

            }
        });

    }

    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        HashMap<String, String> param;
        JSONParse() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //listdata.setText(hasil);

            pDialog = new ProgressDialog(Joblist.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            param  = new HashMap<>();
            param.put("devicetag", txtDeviceTag.getText().toString());
            param.put("masalah", txtSolusi.getText().toString());
            param.put("tindakan", txtKeterangan.getText().toString());
            param.put("status", spinStatus.getSelectedItem().toString());
            param.put("user", global.user);
            param.put("main_wiring", main_wiring);
            param.put("id", txtID.getText().toString());

            Log.d("hashmap param",param.toString());
        }

        @Override


        protected Boolean doInBackground(Void... params) {
            try {

                jsonObj = Fungsi.postJoblist(param);
                //jsonObj = new JSONObject();

            } catch (Exception e){
                Log.d("error jsonObj",jsonObj.toString());
            }

            return true;
            // Getting JSON from URL


        }
        @Override
        protected void onPostExecute ( final Boolean success){
            pDialog.dismiss();
            try {
                // Getting JSON Array

                Toast.makeText(getApplicationContext(), spinStatus.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    String status = jsonObj.getString("status").toString();
                    Log.d("device",status);
                    if(status.equals("success")){
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), jsonObj.toString(), Toast.LENGTH_LONG).show();
                    }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}

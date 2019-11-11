package com.example.wiring;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;



public class Checklist extends AppCompatActivity {
    JSONParse postTask = null;
    JSONObject jsonObj;
    ProgressDialog pDialog;
    CheckBox checkLabel;
    CheckBox checkWrapping;
    CheckBox checkRegulator;
    EditText txtKeterangan;
    JSONParse checkListTask;
    TextView txtID;
    private String Label, Wrapping, Regulator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        TextView txtDeviceTag = (TextView) findViewById(R.id.txtDeviceTag);
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtID = (TextView) findViewById(R.id.txtID);
        checkLabel =  (CheckBox) findViewById(R.id.checkLabel);
        checkWrapping =  (CheckBox) findViewById(R.id.checkWrapping);
        checkRegulator =  (CheckBox) findViewById(R.id.checkRegulator);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        txtKeterangan = (EditText) findViewById(R.id.txtKeterangan);
        String devicetag = getIntent().getStringExtra("devicetag").toString();
        String description = getIntent().getStringExtra("description").toString();
        String id = getIntent().getStringExtra("id").toString();
        txtDeviceTag.setText(devicetag);
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

            pDialog = new ProgressDialog(Checklist.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            Wrapping = String.valueOf(checkWrapping.isChecked());
            Label = String.valueOf(checkLabel.isChecked());
            Regulator = String.valueOf(checkRegulator.isChecked());
            param  = new HashMap<>();
            param.put("regulator", Regulator);
            param.put("wrapping", Wrapping);
            param.put("label", Label);
            param.put("keterangan", txtKeterangan.getText().toString());
            param.put("user", global.user);
            param.put("main_wiring", txtID.getText().toString());

            Log.d("hashmap param",param.toString());
        }

        @Override


        protected Boolean doInBackground(Void... params) {
            try {

                jsonObj = Fungsi.postChecklist(param);

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
}

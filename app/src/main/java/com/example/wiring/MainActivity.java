package com.example.wiring;

import androidx.appcompat.app.AppCompatActivity;
//import com.google.gson.Gson;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity{
    EditText txtCari;
    Button btnCari;
    ListView list;
    JSONParse mAuthTask = null;
    JSONArray jsonArray;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListAdapter adapter;
    HashMap<String, String> map;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCari = (EditText) findViewById(R.id.txtCari);
        btnCari = (Button) findViewById(R.id.btnCari);
        list = (ListView) findViewById(R.id.listView);
        Log.d("mytag","aaaaa");
        /*
        try {
            mAuthTask = new JSONParse("315");
            mAuthTask.execute((Void) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAuthTask = null;
        */

        btnCari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    mAuthTask = new JSONParse(txtCari.getText().toString());
                    mAuthTask.execute((Void) null);
                    //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
    }

    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        String mkeyword;
        JSONParse(String keyword) {

            mkeyword = keyword;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //listdata.setText(hasil);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


            adapter = null;
            //map.clear();
            oslist.clear();
        }

        @Override


        protected Boolean doInBackground(Void... params) {

            jsonArray = Fungsi.cari(mkeyword);
            try {
                JSONObject json;
                //final String[] str1 = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    //str1[i] = json.getString("id");
                    map = new HashMap<String, String>();
                    map.put("id", json.getString("id"));
                    map.put("devicetag", json.getString("devicetag"));
                    map.put("description", json.getString("description"));
                    map.put("brand", json.getString("brand"));
                    map.put("card_type", json.getString("card_type"));
                    map.put("signal_type", json.getString("signal_type"));
                    map.put("address", json.getString("node")+":"+json.getString("card")+":"+json.getString("channel"));
                    map.put("tb_set", json.getString("tb_set"));
                    map.put("terminal", json.getString("terminal"));
                    map.put("terminal2", json.getString("terminal2"));
                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getApplicationContext(), oslist, R.layout.search_wiring, new String[] {
                                "id","devicetag","description","brand","card_type","signal_type","address","tb_set","terminal","terminal2" }, new int[] {
                                    R.id.id,R.id.tagname,R.id.description,R.id.plc,R.id.txtTipe,R.id.txtSignal,R.id.txtAddress,R.id.txtTbSet,R.id.txtTerminal1,R.id.txtTerminal2});

                    //oslist = new ArrayList<HashMap<String, String>>();;
                    //registerForContextMenu(list);
                }
            } catch (Exception e){

            }


            return true;
            // Getting JSON from URL


        }
        @Override
        protected void onPostExecute ( final Boolean success){
            pDialog.dismiss();
            try {
                // Getting JSON Array
                //listdata.setText(jsonArray.toString());
                //oslist.clear();
                list.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }



}


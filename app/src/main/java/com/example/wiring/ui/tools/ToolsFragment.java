package com.example.wiring.ui.tools;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wiring.Fungsi;
import com.example.wiring.R;
import com.example.wiring.global;
import com.example.wiring.ui.gallery.GalleryFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    ProgressDialog pDialog;
    DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText txtTgl;
    Button btnCari, btnTampil;
    ListView list;
    LinearLayout relLayOut2;
    String selectedSpinner;
    Spinner txtPilih;
    JSONParse mAuthTask = null;
    JSONArray jsonArray;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListAdapter adapter;
    HashMap<String, String> map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        txtTgl = (EditText)  root.findViewById(R.id.txtTgl);
        btnCari = (Button)  root.findViewById(R.id.btnCari);
        btnTampil = (Button)  root.findViewById(R.id.btnTampil);
        list = (ListView)  root.findViewById(R.id.listView);
        txtPilih = (Spinner)  root.findViewById(R.id.txtPilih);
        relLayOut2 = (LinearLayout)  root.findViewById(R.id.relLayOut2);
        txtTgl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        txtPilih.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedSpinner = txtPilih.getSelectedItem().toString();
                if(selectedSpinner.equals("Tambah (add)")){
                    relLayOut2.setVisibility(View.GONE);
                }else if(selectedSpinner.equals("View Tanggal")){
                    relLayOut2.setVisibility(View.VISIBLE);
                }else{
                    relLayOut2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        btnTampil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    selectedSpinner = txtPilih.getSelectedItem().toString();
                    if(selectedSpinner.equals("Tambah (add)")){
                        relLayOut2.setVisibility(View.GONE);
                    }else if(selectedSpinner.equals("View Tanggal")){
                        relLayOut2.setVisibility(View.VISIBLE);
                    }else{
                        relLayOut2.setVisibility(View.GONE);
                    }
                    //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
                }

            }
        });
        btnCari.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    mAuthTask = new JSONParse(txtTgl.getText().toString());
                    mAuthTask.execute((Void) null);
                    //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
                }
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        relLayOut2.setVisibility(View.GONE);
        return root;
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                txtTgl.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        String mtgl,mtgl2,muser;
        JSONParse(String tgl) {

            mtgl = tgl;
            mtgl2 = tgl;
            muser = global.user;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
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

            try {

                jsonArray = Fungsi.cariChecklist(mtgl,mtgl2,muser);

                JSONObject json;

                Log.d("jsonArray",jsonArray.length() + jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);

                    Log.d("map",json.toString());
                    map = new HashMap<String, String>();
                    map.put("id", json.getString("id"));
                    map.put("devicetag", json.getString("devicetag"));
                    map.put("description", json.getString("description"));
                    map.put("brand", json.getString("brand"));
                    map.put("regulator", json.getString("regulator"));
                    map.put("wrapping", json.getString("wrapping"));
                    map.put("label", json.getString("label"));
                    map.put("user", json.getString("user"));
                    map.put("tanggal", json.getString("tanggal"));
                    map.put("keterangan", json.getString("keterangan"));

                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getActivity().getApplicationContext(), oslist, R.layout.search_checklist, new String[] {
                            "id","devicetag","description","brand","regulator","wrapping","label","tanggal","keterangan"}, new int[] {
                            R.id.id,R.id.tagname,R.id.description,R.id.plc,R.id.txtRegulator,R.id.txtWrapping,R.id.txtLabel,R.id.txtTanggal,R.id.txtKeterangan});

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
            Log.d("oslist",oslist.toString());
            list.setAdapter(adapter);

        }
    }

    private class JSONParseUnclear extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        String mtgl,mtgl2,muser;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
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

            try {

                jsonArray = Fungsi.cariChecklist(mtgl,mtgl2,muser);

                JSONObject json;

                Log.d("jsonArray",jsonArray.length() + jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);

                    Log.d("map",json.toString());
                    map = new HashMap<String, String>();
                    map.put("id", json.getString("id"));
                    map.put("devicetag", json.getString("devicetag"));
                    map.put("description", json.getString("description"));
                    map.put("brand", json.getString("brand"));
                    map.put("regulator", json.getString("regulator"));
                    map.put("wrapping", json.getString("wrapping"));
                    map.put("label", json.getString("label"));
                    map.put("user", json.getString("user"));
                    map.put("tanggal", json.getString("tanggal"));
                    map.put("keterangan", json.getString("keterangan"));

                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getActivity().getApplicationContext(), oslist, R.layout.search_checklist, new String[] {
                            "id","devicetag","description","brand","regulator","wrapping","label","tanggal","keterangan"}, new int[] {
                            R.id.id,R.id.tagname,R.id.description,R.id.plc,R.id.txtRegulator,R.id.txtWrapping,R.id.txtLabel,R.id.txtTanggal,R.id.txtKeterangan});

                }
            } catch (Exception e){

            }


            return true;
            // Getting JSON from URL


        }
        @Override
        protected void onPostExecute ( final Boolean success){
            pDialog.dismiss();
            Log.d("oslist",oslist.toString());
            list.setAdapter(adapter);

        }
    }
}
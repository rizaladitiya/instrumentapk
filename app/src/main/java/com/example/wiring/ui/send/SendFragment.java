package com.example.wiring.ui.send;

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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wiring.Fungsi;
import com.example.wiring.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;
    EditText txtCari;
    Button btnCari;
    ListView list;
    JSONParse mAuthTask = null;
    JSONArray jsonArray;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ListAdapter adapter;
    HashMap<String, String> map;
    ProgressDialog pDialog;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        txtCari = (EditText)  root.findViewById(R.id.txtCari);
        btnCari = (Button)  root.findViewById(R.id.btnCari);
        list = (ListView)  root.findViewById(R.id.listView);
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
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        return root;
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

            jsonArray = Fungsi.carigudang(mkeyword);
            try {
                JSONObject json;
                //final String[] str1 = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    //str1[i] = json.getString("id");
                    map = new HashMap<String, String>();
                    map.put("id", json.getString("id"));
                    map.put("kode", json.getString("kode"));
                    map.put("namabarang", json.getString("namabarang"));
                    map.put("namasatuan", json.getString("namasatuan"));
                    map.put("namakelompok", json.getString("namakelompok"));
                    map.put("stock", json.getString("stock"));
                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getActivity().getApplicationContext(), oslist, R.layout.search_stokgudang, new String[] {
                            "namabarang","namasatuan","kode","stock","namakelompok" }, new int[] {
                            R.id.txtNamabarang,R.id.txtSatuan,R.id.txtKode,R.id.txtStock,R.id.txtKelompok});

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
                list.setAdapter(adapter);
                registerForContextMenu(list);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        TextView tagname = (TextView) view.findViewById(R.id.tagname);
                        String uname = oslist.get(+position).get("devicetag").toString();
                        Log.d("device",uname);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }
}
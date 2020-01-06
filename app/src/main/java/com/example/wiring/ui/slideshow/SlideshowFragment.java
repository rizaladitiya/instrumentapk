package com.example.wiring.ui.slideshow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wiring.Checklist;
import com.example.wiring.Fungsi;
import com.example.wiring.Joblist;
import com.example.wiring.MainActivity;
import com.example.wiring.R;
import com.example.wiring.UtamaActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
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
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        txtCari = (EditText)  root.findViewById(R.id.txtCari);
        btnCari = (Button)  root.findViewById(R.id.btnCari);
        list = (ListView)  root.findViewById(R.id.listView);
        /*
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
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
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        return root;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Pilih Menu");
        menu.add(0, v.getId(), 0, "Checklist");
        menu.add(0, v.getId(), 0, "Joblist");

    }

    @Override

    public boolean onContextItemSelected(MenuItem item) {
        Log.d("contextmenu",item.getTitle().toString());


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        String devicetag = oslist.get(+info.position).get("devicetag").toString();
        String description = oslist.get(+info.position).get("description").toString();
        String id = oslist.get(+info.position).get("id").toString();

        if(item.getTitle()=="Checklist")
        {
            Intent modify_intent = new Intent(getActivity(), Checklist.class);
            modify_intent.putExtra("devicetag", devicetag);
            modify_intent.putExtra("description", description);
            modify_intent.putExtra("id", id);
            startActivity(modify_intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
        } else if(item.getTitle()=="Joblist")
        {
            Intent modify_intent = new Intent(getActivity(), Joblist.class);
            modify_intent.putExtra("devicetag", devicetag);
            modify_intent.putExtra("description", description);
            modify_intent.putExtra("main_wiring", id);
            modify_intent.putExtra("status", "To Do");
            modify_intent.putExtra("id", "0");
            startActivity(modify_intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);

            Log.d("contextmenu","joblist");

        }else{
            Log.d("contextmenu","tidak ada yang dipilih");
        }

        return super.onContextItemSelected(item);
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

            jsonArray = Fungsi.cari(mkeyword);
            try {
                JSONObject json;
                //final String[] str1 = new String[jsonArray.length()];
                Log.d("json",jsonArray.toString());
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
                    map.put("io_panel", json.getString("io_panel"));
                    map.put("tb_set", json.getString("tb_set"));
                    map.put("terminal", json.getString("terminal"));
                    map.put("terminal2", json.getString("terminal2"));
                    map.put("lastchecked", json.getString("lastchecked"));
                    map.put("lastuser", json.getString("lastuser"));
                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getActivity().getApplicationContext(), oslist, R.layout.search_wiring, new String[] {
                            "id","devicetag","description","brand","card_type","signal_type","address","tb_set","terminal","terminal2","lastchecked","lastuser","io_panel" }, new int[] {
                            R.id.id,R.id.tagname,R.id.description,R.id.plc,R.id.txtTipe,R.id.txtSignal,R.id.txtAddress,R.id.txtTbSet,R.id.txtTerminal1,R.id.txtTerminal2,R.id.txtLastChecked,R.id.txtUser,R.id.panel});

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
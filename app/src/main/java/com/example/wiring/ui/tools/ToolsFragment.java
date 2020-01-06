package com.example.wiring.ui.tools;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.example.wiring.Joblist;
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
    EditText txtTgl, txtCari;
    Button btnCari, btnTampil;
    ListView list;
    LinearLayout relLayOut2, layoutIsi;
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
        txtCari = (EditText)  root.findViewById(R.id.txtCari);
        relLayOut2 = (LinearLayout)  root.findViewById(R.id.relLayOut2);
        layoutIsi = (LinearLayout)  root.findViewById(R.id.layoutIsi);
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

                switch (selectedSpinner) {
                    case "Tambah (add)":
                        relLayOut2.setVisibility(View.GONE);
                        break;
                    case "View Tanggal":
                        relLayOut2.setVisibility(View.VISIBLE);
                        txtCari.setVisibility(View.GONE);
                        txtTgl.setVisibility(View.VISIBLE);
                        break;
                    case "Cari":
                        relLayOut2.setVisibility(View.VISIBLE);
                        txtCari.setVisibility(View.VISIBLE);
                        txtTgl.setVisibility(View.GONE);
                        break;
                    case "View To Do":
                        relLayOut2.setVisibility(View.GONE);
                        break;
                    case "View Doing":
                        relLayOut2.setVisibility(View.GONE);
                        break;
                    case "View Done":
                        relLayOut2.setVisibility(View.VISIBLE);
                        txtCari.setVisibility(View.GONE);
                        txtTgl.setVisibility(View.VISIBLE);
                        break;
                    default:
                        relLayOut2.setVisibility(View.GONE);
                        break;
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
                    switch (selectedSpinner) {
                        case "Tambah (add)":
                            relLayOut2.setVisibility(View.GONE);
                            Intent modify_intent = new Intent(getActivity(), Joblist.class);
                            modify_intent.putExtra("devicetag", "");
                            modify_intent.putExtra("description", "");
                            modify_intent.putExtra("id", "0");
                            modify_intent.putExtra("main_wiring", "0");
                            modify_intent.putExtra("status", "To Do");
                            startActivity(modify_intent);
                            ((Activity) getActivity()).overridePendingTransition(0, 0);
                            break;
                        case "View Tanggal":
                            relLayOut2.setVisibility(View.VISIBLE);
                            txtCari.setVisibility(View.GONE);
                            txtTgl.setVisibility(View.VISIBLE);
                            break;
                        case "Cari":
                            relLayOut2.setVisibility(View.VISIBLE);
                            txtCari.setVisibility(View.VISIBLE);
                            txtTgl.setVisibility(View.GONE);
                            break;
                        case "View To Do":
                            relLayOut2.setVisibility(View.GONE);
                            sendRequest(selectedSpinner);
                            break;
                        case "View Doing":
                            relLayOut2.setVisibility(View.GONE);
                            sendRequest(selectedSpinner);
                            break;
                        case "View Done":
                            txtCari.setVisibility(View.GONE);
                            txtTgl.setVisibility(View.VISIBLE);
                            txtCari.setVisibility(View.GONE);
                            txtTgl.setVisibility(View.VISIBLE);
                            break;
                        default:
                            relLayOut2.setVisibility(View.GONE);
                            break;
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
                sendRequest(selectedSpinner);
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

    private void sendRequest(String selected){
        try {
            mAuthTask = new JSONParse(selected);
            mAuthTask.execute((Void) null);
            //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
        }
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Pilih Menu");
        menu.add(0, v.getId(), 0, "To Do");
        menu.add(0, v.getId(), 1, "Doing");
        menu.add(0, v.getId(), 2, "Done");

    }

    @Override

    public boolean onContextItemSelected(MenuItem item) {
        Log.d("contextmenu",item.getTitle().toString());


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        String devicetag = oslist.get(+info.position).get("devicetag").toString();
        String description = oslist.get(+info.position).get("description").toString();
        String id = oslist.get(+info.position).get("id").toString();
        String status;
        /*
        if(item.getTitle()=="Checklist")
        {
            Intent modify_intent = new Intent(getActivity(), Joblist.class);
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
            modify_intent.putExtra("id", "0");
            startActivity(modify_intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);

            Log.d("contextmenu","joblist");

        }else{
            Log.d("contextmenu","tidak ada yang dipilih");
        }
        */
        switch (item.getTitle().toString()) {
            case "To Do":
                status = "To Do";
                break;
            case "Doing":
                status = "Doing";
                break;
            case "Done":
                status = "Done";
                break;
            default:
                status = "To Do";
                break;
        }
        Intent modify_intent = new Intent(getActivity(), Joblist.class);
        modify_intent.putExtra("devicetag", devicetag);
        modify_intent.putExtra("description", description);
        modify_intent.putExtra("status", status);
        modify_intent.putExtra("main_wiring", id);
        modify_intent.putExtra("id", "0");
        startActivity(modify_intent);
        ((Activity) getActivity()).overridePendingTransition(0, 0);

        Log.d("contextmenu","joblist");
        return super.onContextItemSelected(item);
    }
    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        String mtgl,mtgl2,muser,maction,mcari;
        JSONParse(String action) {

            mtgl = txtTgl.getText().toString();
            mtgl2 = txtTgl.getText().toString();
            mcari = txtCari.getText().toString();
            maction = action;
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
                switch (maction) {
                    case "Tambah (add)":

                        break;
                    case "Cari":
                        jsonArray = Fungsi.cariJoblist(mcari);
                        break;
                    case "View Tanggal":
                        jsonArray = Fungsi.cariJoblistTanggal(mtgl,mtgl2);
                        break;
                    case "View To Do":
                        jsonArray = Fungsi.cariJoblistToDo();
                        break;
                    case "View Doing":
                        jsonArray = Fungsi.cariJoblistDoing();
                        break;
                    case "View Done":
                        jsonArray = Fungsi.cariJoblistDone(mtgl,mtgl2);
                        break;
                    default:

                        break;
                }


                JSONObject json;
                Log.d("action",maction);
                Log.d("jsonArray",jsonArray.length() + jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);

                    map = new HashMap<String, String>();
                    map.put("id", json.getString("id"));
                    map.put("devicetag", json.getString("devicetag").replace("null", ""));
                    map.put("description", json.getString("description").replace("null", ""));
                    map.put("masalah", json.getString("masalah").replace("null", ""));
                    map.put("tindakan", json.getString("tindakan").replace("null", ""));
                    map.put("koreksi", json.getString("koreksi").replace("null", ""));
                    map.put("keterangan", json.getString("keterangan").replace("null", ""));
                    map.put("created", Fungsi.parseTanggal(json.getString("created")));
                    map.put("doing", Fungsi.parseTanggal(json.getString("doing").replace("null", "")));
                    map.put("user_created", json.getString("user_created").replace("null", ""));
                    map.put("user_doing", json.getString("user_doing").replace("null", ""));
                    map.put("user_done", json.getString("user_done").replace("null", ""));
                    map.put("status", json.getString("status").replace("null", ""));
                    map.put("done", Fungsi.parseTanggal(json.getString("done").replace("null", "")));

                    Log.d("map",json.toString());
                    oslist.add(map);
                    adapter = new SimpleAdapter(
                            getActivity().getApplicationContext(), oslist, R.layout.search_joblist, new String[] {
                            "id","devicetag","description","masalah","tindakan","user_created","user_doing","user_done","created","doing","done"}, new int[] {
                            R.id.id,R.id.tagname,R.id.description,R.id.textMasalah,R.id.txtSolusi,R.id.txtUserCreated,R.id.txtUserDoing,R.id.txtUserDone,R.id.txtTodo,R.id.txtDoing,R.id.txtDone});

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

            layoutIsi.setVisibility(View.VISIBLE);
            pDialog.dismiss();
            list.setAdapter(adapter);
            registerForContextMenu(list);
            Log.d("oslist",oslist.toString());

        }
    }

}
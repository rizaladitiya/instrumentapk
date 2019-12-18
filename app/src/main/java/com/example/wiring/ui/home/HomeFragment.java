package com.example.wiring.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wiring.Fungsi;
import com.example.wiring.R;
import com.example.wiring.SessionManager;
import com.example.wiring.ui.login.LoginActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    TextView txtSpeed1,txtSpeed2,txtSpeed3, txtHasil1, txtHasil2,txtHasil3, txtHasil3q1, txtHasil3q2, txtHasil3q3,txtHasil3q4;
    TextView txtBreak1,txtBreak2,txtBreak3, txtHasil1q1, txtHasil1q2, txtHasil1q3,txtHasil1q4, txtHasil2q1, txtHasil2q2, txtHasil2q3,txtHasil2q4;
    private HomeViewModel homeViewModel;
    LinearLayout total1,total2,total3;
    Button btnLogout;
    JSONParse mAuthTask = null;
    JSONParseProd prodTask = null;
    Drawable drawableRed;
    Drawable drawableGreen;
    String token;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnLogout = (Button)  root.findViewById(R.id.btnLogout);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        total1 = root.findViewById(R.id.total1);
        total2 = root.findViewById(R.id.total2);
        total3 = root.findViewById(R.id.total3);
        TextView txtNama = root.findViewById(R.id.txtNama);
        txtSpeed1 = root.findViewById(R.id.Speed);
        txtSpeed2 = root.findViewById(R.id.Speed2);
        txtSpeed3 = root.findViewById(R.id.Speed3);

        txtBreak1 = root.findViewById(R.id.txtBreak);
        txtBreak2 = root.findViewById(R.id.txtBreak2);
        txtBreak3 = root.findViewById(R.id.txtBreak3);
        txtHasil1 = root.findViewById(R.id.txtHasil1);
        txtHasil1q1 = root.findViewById(R.id.txtHasil1q1);
        txtHasil1q2 = root.findViewById(R.id.txtHasil1q2);
        txtHasil1q3 = root.findViewById(R.id.txtHasil1q3);
        txtHasil1q4 = root.findViewById(R.id.txtHasil1q4);
        txtHasil2 = root.findViewById(R.id.txtHasil2);
        txtHasil2q1 = root.findViewById(R.id.txtHasil2q1);
        txtHasil2q2 = root.findViewById(R.id.txtHasil2q2);
        txtHasil2q3 = root.findViewById(R.id.txtHasil2q3);
        txtHasil2q4 = root.findViewById(R.id.txtHasil2q4);
        txtHasil3 = root.findViewById(R.id.txtHasil3);
        txtHasil3q1 = root.findViewById(R.id.txtHasil3q1);
        txtHasil3q2 = root.findViewById(R.id.txtHasil3q2);
        txtHasil3q3 = root.findViewById(R.id.txtHasil3q3);
        txtHasil3q4 = root.findViewById(R.id.txtHasil3q4);
        txtNama.setText(SessionManager.getLoggedInUser(getActivity()));
        btnLogout.setOnClickListener(new
            View.OnClickListener() {
            @Override public void onClick(View v) {
                SessionManager.clearLoggedInUser(getActivity());
                startActivity(new
                        Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        drawableRed = ContextCompat.getDrawable(
                getActivity(),
                R.drawable.circle_shape_red
        );
        drawableGreen = ContextCompat.getDrawable(
                getActivity(),
                R.drawable.circle_shape_green
        );

        try {
            //mAuthTask.cancel(true);
            mAuthTask = new JSONParse();
            mAuthTask.execute((Void) null);

            prodTask = new JSONParseProd();
            prodTask.execute((Void) null);
            //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
        }

        return root;
    }
    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;

        JSONArray jsonArray;
        JSONObject json;
        String speed1,speed2,speed3;
        String break1,break2,break3;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //listdata.setText(hasil);

        }

        @Override


        protected Boolean doInBackground(Void... params) {
            //while(!this.isCancelled()) {
                // doyourjobhere


                jsonArray = Fungsi.statusRun();
                try {
                    //final String[] str1 = new String[jsonArray.length()];
                    Log.d("json", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        json = jsonArray.getJSONObject(i);

                        if (json.getString("machinename").equals("PM1") && json.getString("name").equals("speed")) {
                            speed1 = json.getString("value");
                        }

                        if (json.getString("machinename").equals("PM2") && json.getString("name").equals("speed")) {
                            speed2 = json.getString("value");
                        }

                        if (json.getString("machinename").equals("PM3") && json.getString("name").equals("speed")) {
                            speed3 = json.getString("value");
                        }

                        if(json.getString("machinename").equals("PM1") && json.getString("name").equals("break")){
                            break1 = json.getString("value");
                        }

                        if (json.getString("machinename").equals("PM2") && json.getString("name").equals("break")) {
                            break2 = json.getString("value");
                        }
                        if (json.getString("machinename").equals("PM3") && json.getString("name").equals("break")) {
                            break3 = json.getString("value");
                        }
                        //speed1 = json.getString("speed1");
                        //speed2 = json.getString("speed2");
                    }
                } catch (Exception e) {

                }
            //}

            return true;
            // Getting JSON from URL


        }
        @Override
        protected void onPostExecute ( final Boolean success){
            try {
                // Getting JSON Array


                txtSpeed1.setText(speed1);
                txtSpeed2.setText(speed2);
                txtSpeed3.setText(speed3);
                //txtBreak2.setText(break2);
                //txtBreak3.setText(break3);

                if(break1.equals("1")) {
                    txtBreak1.setCompoundDrawablesWithIntrinsicBounds(
                            drawableRed, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak1.setText("Stop");
                }else {
                    txtBreak1.setCompoundDrawablesWithIntrinsicBounds(
                            drawableGreen, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak1.setText("Run");
                }

                if(break2.equals("1")) {
                    txtBreak2.setCompoundDrawablesWithIntrinsicBounds(
                            drawableRed, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak2.setText("Stop");
                }else {
                    txtBreak2.setCompoundDrawablesWithIntrinsicBounds(
                            drawableGreen, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak2.setText("Run");
                }

                if(break3.equals("1")) {
                    txtBreak3.setCompoundDrawablesWithIntrinsicBounds(
                            drawableRed, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak3.setText("Stop");
                }else {
                    txtBreak3.setCompoundDrawablesWithIntrinsicBounds(
                            drawableGreen, // Drawable left
                            null, // Drawable top
                            null, // Drawable right
                            null // Drawable bottom
                    );
                    txtBreak3.setText("Run");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }

    private class JSONParseProd extends AsyncTask<Void, Void, Boolean> {
        //private ProgressDialog pDialog;
        JSONArray jsonArray;
        JSONObject json;
        String hasil1,hasil2,hasil3;
        String hasil1q1,hasil2q1,hasil3q1;
        String hasil1q2,hasil2q2,hasil3q2;
        String hasil1q3,hasil2q3,hasil3q3;
        String hasil1q4,hasil2q4,hasil3q4;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //listdata.setText(hasil);
            total1.setVisibility(View.GONE);
            total2.setVisibility(View.GONE);
            total3.setVisibility(View.GONE);

        }

        @Override


        protected Boolean doInBackground(Void... params) {
            //while(!this.isCancelled()) {
            // doyourjobhere

            int jam = Fungsi.hour();
            if(jam>=9){
                jsonArray = Fungsi.hasilProduksiBaru(Fungsi.getDateString(Fungsi.now()));
            }else {
                jsonArray = Fungsi.hasilProduksiBaru(Fungsi.getDateString(Fungsi.yesterday()));
            }
            try {
                //final String[] str1 = new String[jsonArray.length()];
                    /*
                    hasil1 = Fungsi.thousandFormat(json.getString("pm1hasilkg"))+" Kg";
                    hasil1q1 = "Q1 "+Fungsi.thousandFormat(json.getString("pm1hasilkgq1"))+" Kg";
                    hasil1q2 = "Q2 "+Fungsi.thousandFormat(json.getString("pm1hasilkgq2"))+" Kg";
                    hasil1q3 = "Q3 "+Fungsi.thousandFormat(json.getString("pm1hasilkgq3"))+" Kg";
                    hasil1q4 = "Q4 "+Fungsi.thousandFormat(json.getString("pm1hasilkgq4"))+" Kg";
                    hasil2 = Fungsi.thousandFormat(json.getString("pm2hasilkg"))+" Kg";
                    hasil2q1 = "Q1 "+Fungsi.thousandFormat(json.getString("pm2hasilkgq1"))+" Kg";
                    hasil2q2 = "Q2 "+Fungsi.thousandFormat(json.getString("pm2hasilkgq2"))+" Kg";
                    hasil2q3 = "Q3 "+Fungsi.thousandFormat(json.getString("pm2hasilkgq3"))+" Kg";
                    hasil2q4 = "Q4 "+Fungsi.thousandFormat(json.getString("pm2hasilkgq4"))+" Kg";

                     */
                Log.d("json", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);


                    /* Hasil Produksi PM1 */
                    if (json.getString("pm").equals("1") && json.getString("mutu").equals("1")) {
                        hasil1q1 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("1") && json.getString("mutu").equals("2")) {
                        hasil1q2 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("1") && json.getString("mutu").equals("3")) {
                        hasil1q3 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("1") && json.getString("mutu").equals("4")) {
                        hasil1q4 = json.getString("jumlah");
                    }


                    /* Hasil Produksi PM2 */
                    if (json.getString("pm").equals("2") && json.getString("mutu").equals("1")) {
                        hasil2q1 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("2") && json.getString("mutu").equals("2")) {
                        hasil2q2 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("2") && json.getString("mutu").equals("3")) {
                        hasil2q3 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("2") && json.getString("mutu").equals("4")) {
                        hasil2q4 = json.getString("jumlah");
                    }


                    /* Hasil Produksi PM3 */
                    if (json.getString("pm").equals("3") && json.getString("mutu").equals("1")) {
                        hasil3q1 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("3") && json.getString("mutu").equals("2")) {
                        hasil3q2 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("3") && json.getString("mutu").equals("3")) {
                        hasil3q3 = json.getString("jumlah");
                    }
                    if (json.getString("pm").equals("3") && json.getString("mutu").equals("4")) {
                        hasil3q4 = json.getString("jumlah");
                    }


                }


            } catch (Exception e) {

            }
            //}

            return true;
            // Getting JSON from URL


        }
        @Override
        protected void onPostExecute ( final Boolean success){
            try {
                // Getting JSON Array


                hasil1 = String.valueOf(Fungsi.cekNull(hasil1q1)+Fungsi.cekNull(hasil1q2)+Fungsi.cekNull(hasil1q3)+Fungsi.cekNull(hasil1q4));
                hasil2 = String.valueOf(Fungsi.cekNull(hasil2q1)+Fungsi.cekNull(hasil2q2)+Fungsi.cekNull(hasil2q3)+Fungsi.cekNull(hasil2q4));
                hasil3 = String.valueOf(Fungsi.cekNull(hasil3q1)+Fungsi.cekNull(hasil3q2)+Fungsi.cekNull(hasil3q3)+Fungsi.cekNull(hasil3q4));
                hasil1q1 = Fungsi.totalKg(hasil1q1);
                hasil1q2 = Fungsi.totalKg(hasil1q2);
                hasil1q3 = Fungsi.totalKg(hasil1q3);
                hasil1q4 = Fungsi.totalKg(hasil1q4);
                hasil2q1 = Fungsi.totalKg(hasil2q1);
                hasil2q2 = Fungsi.totalKg(hasil1q2);
                hasil2q3 = Fungsi.totalKg(hasil2q3);
                hasil2q4 = Fungsi.totalKg(hasil2q4);
                hasil3q1 = Fungsi.totalKg(hasil3q1);
                hasil3q2 = Fungsi.totalKg(hasil3q2);
                hasil3q3 = Fungsi.totalKg(hasil3q3);
                hasil3q4 = Fungsi.totalKg(hasil3q4);
                hasil1 = Fungsi.totalKg(hasil1);
                hasil2 = Fungsi.totalKg(hasil2);
                hasil3 = Fungsi.totalKg(hasil3);

                txtHasil1.setText(hasil1);
                txtHasil1q1.setText("Q1 : "+hasil1q1);
                txtHasil1q2.setText("Q2 : "+hasil1q2);
                txtHasil1q3.setText("Q3 : "+hasil1q3);
                txtHasil1q4.setText("Q4 : "+hasil1q4);
                txtHasil2.setText(hasil2);
                txtHasil2q1.setText("Q1 : "+hasil2q1);
                txtHasil2q2.setText("Q2 : "+hasil2q2);
                txtHasil2q3.setText("Q3 : "+hasil2q3);
                txtHasil2q4.setText("Q4 : "+hasil2q4);
                txtHasil3.setText(hasil3);
                txtHasil3q1.setText("Q1 : "+hasil3q1);
                txtHasil3q2.setText("Q2 : "+hasil3q2);
                txtHasil3q3.setText("Q3 : "+hasil3q3);
                txtHasil3q4.setText("Q4 : "+hasil3q4);

                total1.setVisibility(View.VISIBLE);
                total2.setVisibility(View.VISIBLE);
                total3.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }
}
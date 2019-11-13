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

    TextView txtSpeed1,txtSpeed2,txtSpeed3;
    TextView txtBreak1,txtBreak2,txtBreak3;
    private HomeViewModel homeViewModel;
    Button btnLogout;
    JSONParse mAuthTask = null;
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
        TextView txtNama = root.findViewById(R.id.txtNama);
        txtSpeed1 = root.findViewById(R.id.Speed);
        txtSpeed2 = root.findViewById(R.id.Speed2);
        txtSpeed3 = root.findViewById(R.id.Speed3);

        txtBreak1 = root.findViewById(R.id.txtBreak);
        txtBreak2 = root.findViewById(R.id.txtBreak2);
        txtBreak3 = root.findViewById(R.id.txtBreak3);
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                //your 1st command

                //you can use a for here and check if the command was executed or just wait and execute the 2nd command
                try {
                    try {
                        //mAuthTask.cancel(true);
                        mAuthTask = new JSONParse();
                        mAuthTask.execute((Void) null);
                        //Toast.makeText(getApplicationContext(), txtCari.getText().toString(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("JSON", "Malformed: \"" + e.toString() + "\"");
                    }
                    Thread.sleep(10000); //wait 2 seconds
                    mAuthTask.cancel(true);
                    Log.e("JSON", "Sleep 1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //your 2nd command
            }
        }).start();

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
}
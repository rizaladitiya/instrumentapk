package com.example.wiring;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Rizal on 07/10/2015.
 */
public class Fungsi {
    public static JSONObject loginUser(String email, String password) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/login/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("user", email);
        params.put("password", MD5(password));
        JSONObject json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("login",json.toString());
        return json;
    }

    public static String loginUser2(String email, String password) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/login/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        String json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cari(String keyword) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/wiring/search");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        JSONArray json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray carigudang(String keyword) {
        HttpRequest req = null;
        String url = "http://36.67.32.45:898/api/baranggudang/cari?q="+keyword;
        try {
            req = new HttpRequest(url);
            Log.d("keyword",url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = req.preparePost().sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariChecklist(String from, String to, String user) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/checklist/viewtgluser");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("user", user);
        JSONArray json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariJoblistTanggal(String from, String to) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/viewtgl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        JSONArray json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariJoblist(String keyword) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/search");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        JSONArray json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariJoblistToDo() {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/viewtodo");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = req.preparePost().sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariJoblistDoing() {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/viewdoing");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = req.preparePost().sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray cariJoblistDone(String from, String to) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/viewdone");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        JSONArray json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray statusRun() {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/statusrun/viewall");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = req.preparePost().sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject hasilProduksi(String tanggal) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45:898/laporan/halaman/produksi.php?aksi=hasil&tgl="+tanggal);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject json = null;
        try {
            json = req.preparePost().sendAndReadJSON();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONArray hasilProduksiBaru(String tanggal) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45:898/api/barang/hasilprodtotal/"+tanggal);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = req.preparePost().sendAndReadJSONArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public static Integer cekNull(String number) {
        int num = 0;
        try
        {
            if(number != null)
                num = Integer.parseInt(number);
        }
        catch (NumberFormatException e)
        {
            num = 0;
        }
        return num;
    }

    public static String thousandFormat(String number) {
        int num = 0;
        try
        {
            if(number != null)
                num = Integer.parseInt(number);
        }
        catch (NumberFormatException e)
        {
            num = 0;
        }
        String str = String.format("%,d", num);
        return str;
    }

    public static String totalKg(String total) {
        String hasil;
        hasil = thousandFormat(total)+" Kg";
        return hasil;
    }

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date now() {
        final Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static int hour() {
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static String getDateString(Date datetime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(datetime);
    }

    public static JSONObject postChecklist(HashMap<String, String> params) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/checklist/add");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject json = null;
        try {
            json = req.preparePost().withData(params).sendAndReadJSON();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject postJoblist(HashMap<String, String> params) {
        HttpRequest req = null;
        try {
            req = new HttpRequest("http://36.67.32.45/buanamegah/joblist/add");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONObject json = null;
        try {

            String jsonStr = req.preparePost().sendAndReadString();
            Log.d("jsonstr",jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            json = req.preparePost().withData(params).sendAndReadJSON();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String MD5(String md5) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String parseTanggal(String tgl)
    {
        String result = null;
        Date date = null;

        String originalStringFormat = "yyyy-MM-dd HH:mm:ss";
        String desiredStringFormat = "dd MMM yyyy @ HH:mm";

        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);
        if(tgl == null){
            result="";
        }else {
            try {
                date = readingFormat.parse(tgl);

                result = outputFormat.format(date).toString();
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
        return result;
    }

}
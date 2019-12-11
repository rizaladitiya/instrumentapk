package com.example.wiring;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

}
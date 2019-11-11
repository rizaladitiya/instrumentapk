package com.example.wiring.data;

import android.util.Log;

import com.example.wiring.Fungsi;
import com.example.wiring.data.model.LoggedInUser;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        JSONObject json;
        try {
            // TODO: handle loggedInUser authentication


            /*
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);

            */
            json = Fungsi.loginUser(username,password);

            String status = json.getString("status");
            String user ="";
            String akses = "";
            if(status.equals("success")) {
                akses = json.getString("akses");
                user = json.getString("user");
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                akses,
                                user);

                return new Result.Success<>(fakeUser);
            } else {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                akses,
                                user);
                return new Result.Error(new IOException("Error logging in"));
            }




        } catch (Exception e) {
            Log.d("login2",e.toString());
            return new Result.Error(new IOException("Error logging in", e));

        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}

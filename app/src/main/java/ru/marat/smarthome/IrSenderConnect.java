package ru.marat.smarthome;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IrSenderConnect extends AsyncTask<String, Void, String> {

    private Context mContext;

    public IrSenderConnect(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... command) {
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(command[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line=null;
            while ((line=reader.readLine()) != null) {
                response.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(mContext, result,
                Toast.LENGTH_SHORT).show();
    }
}

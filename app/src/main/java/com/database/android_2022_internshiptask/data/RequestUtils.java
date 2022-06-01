package com.database.android_2022_internshiptask.data;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestUtils {
    private RequestUtils(){}


    public static URL buildUrl(String stringUrl,Map<String,String>params){
        URL url = null;
        String convertedParams="";
        try {
            if(!params.isEmpty()){
                convertedParams = getQuery(params);
            }
            url = new URL(stringUrl+"?"+convertedParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String[] getResultUsingGetMethod(URL url, Map<String,String>headers)throws IOException {

        HttpURLConnection connection= null;
        InputStream stream = null;

        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if(!headers.isEmpty()){
                for (Map.Entry<String,String> header :headers.entrySet()){
                    connection.setRequestProperty(header.getKey(),header.getValue());
                }
            }

            stream = connection.getInputStream();

            JSONObject jsonObject = new JSONObject(readFromStream(stream));
            return new String[]{Integer.toString(connection.getResponseCode()),jsonObject.toString()} ;
        }catch (Exception e){
            Log.e("error",e.toString());
            String code = null;
            if(connection!=null){
               code = Integer.toString(connection.getResponseCode());
            }

            return new String[]{code,"An Error , Please make sure that you add a valid data"} ;
        }
        finally {
            if (connection!=null){
                connection.disconnect();
            }
            if (stream!=null){
                stream.close();
            }


        }

    }


    public static String[] getResultUsingPOSTMethod(URL url, Map<String,String>headers,Map<String,String>body)throws IOException {
        HttpURLConnection connection= null;
        InputStream stream = null;
        OutputStream outputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json") ;
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            if(!headers.isEmpty()){
                for (Map.Entry<String,String> header :headers.entrySet()){
                    connection.setRequestProperty(header.getKey(),header.getValue());
                }
            }

            if(!body.isEmpty()){

                String jsonInputString = new JSONObject(body).toString();

                outputStream = connection.getOutputStream();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input);

            }


            stream = connection.getInputStream();

            JSONObject jsonObject = new JSONObject(readFromStream(stream));
            return new String[]{Integer.toString(connection.getResponseCode()),jsonObject.toString()} ;
        }catch (Exception e){
            Log.e("error",e.toString());
            String code = null;
            if(connection!=null){
                code = Integer.toString(connection.getResponseCode());
            }

            return new String[]{code,"An Error , Please make sure that you add a valid data"} ;
        }
        finally {
            if (connection!=null){
                connection.disconnect();
            }
            if (stream!=null){
                stream.close();
            }
            if (outputStream!=null){
                outputStream.close();
            }
        }


    }




    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static boolean validFiled(EditText key, EditText value) {
        boolean valid = true;
        if (key.getText().toString().isEmpty()) {
            key.setError("");
            valid = false;
        } else {
            key.setError(null);
        }
        if (value.getText().toString().isEmpty()) {
            value.setError("");
            valid = false;
        } else {
            value.setError(null);
        }

        return valid;
    }


    private static String getQuery(Map<String,String>params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String,String> param :params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(param.getValue(), "UTF-8"));
        }

        return result.toString();
    }



}

package com.database.android_2022_internshiptask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.database.android_2022_internshiptask.adapters.TapAdapter;
import com.database.android_2022_internshiptask.data.RequestUtils;
import com.database.android_2022_internshiptask.databinding.ActivityMainBinding;
import com.database.android_2022_internshiptask.databinding.ConnectivityBinding;
import com.database.android_2022_internshiptask.fragments.BodyFragment;
import com.database.android_2022_internshiptask.fragments.HeadersFragment;
import com.database.android_2022_internshiptask.fragments.ParamsFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ConnectivityBinding connectivityBinding;

    public static Map<String, String> headers = new HashMap<>();
    public static Map<String, String> body = new HashMap<>();
    public static Map<String, String> params = new HashMap<>();
    String selectedMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        connectivityBinding = ConnectivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        tapConfiguration();
        spinnerConfiguration();
        btnListeners();

    }


    private void btnListeners() {
        binding.sendRequest.setOnClickListener(view -> {
            selectedMethod = binding.methodSpinner.getSelectedItem().toString();
            if (isNetworkAvailable()) {
                if (selectedMethod.equals("GET")) {
                    if (getHeaders() && validUrl() && getRequestParams()) {
                        String url = binding.urlEd.getText().toString();
                        sendRequest(url);
                    }
                } else {
                    if (getHeaders() && validUrl() && getBody() && getRequestParams()) {
                        String url = binding.urlEd.getText().toString();
                        sendRequest(url);
                    }
                }
            } else {
                setContentView(connectivityBinding.getRoot());
            }


        });


        connectivityBinding.reloadBtn.setOnClickListener(view -> {
            if (isNetworkAvailable()) {
                setContentView(binding.getRoot());
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean validUrl() {
        boolean valid = true;

        if (binding.urlEd.getText().toString().isEmpty()) {
            valid = false;
            binding.urlEd.setError("url is required");
        } else {
            binding.urlEd.setError(null);
        }
        return valid;
    }


    private void sendRequest(String requestUrl) {
        try {
            URL url = RequestUtils.buildUrl(requestUrl, params);
            new RequestQueryTask().execute(url);
        } catch (Exception e) {
            Log.e("result", e.getMessage());
        }
    }

    private boolean getHeaders() {
        boolean headerIssue = true;
        int length = HeadersFragment.headerFields.getChildCount();
        for (int i = 0; i < length; i++) {
            LinearLayout headerItem = (LinearLayout) HeadersFragment.headerFields.getChildAt(i);
            EditText key = headerItem.findViewById(R.id.filed_key);
            EditText value = headerItem.findViewById(R.id.filed_value);
            if (!RequestUtils.validFiled(key, value)) {
                headerIssue = false;
                break;
            }
            headers.put(key.getText().toString(), value.getText().toString());
        }
        return headerIssue;
    }

    private boolean getRequestParams() {
        boolean paramIssue = true;
        if (ParamsFragment.paramsFields != null) {
            int length = ParamsFragment.paramsFields.getChildCount();
            for (int i = 0; i < length; i++) {
                LinearLayout paramItem = (LinearLayout) ParamsFragment.paramsFields.getChildAt(i);
                EditText key = paramItem.findViewById(R.id.filed_key);
                EditText value = paramItem.findViewById(R.id.filed_value);
                if (!RequestUtils.validFiled(key, value)) {
                    paramIssue = false;
                    break;
                }
                params.put(key.getText().toString(), value.getText().toString());
            }
        }
        return paramIssue;
    }

    private boolean getBody() {
        boolean bodyIssue = true;
        if (BodyFragment.bodyFields != null) {
            int length = BodyFragment.bodyFields.getChildCount();
            for (int i = 0; i < length; i++) {
                LinearLayout bodyItem = (LinearLayout) BodyFragment.bodyFields.getChildAt(i);
                EditText key = bodyItem.findViewById(R.id.filed_key);
                EditText value = bodyItem.findViewById(R.id.filed_value);
                if (!RequestUtils.validFiled(key, value)) {
                    bodyIssue = false;
                    break;
                }
                body.put(key.getText().toString(), value.getText().toString());
            }
        }

        return bodyIssue;
    }


    public class RequestQueryTask extends AsyncTask<URL, Void, String[]> {
        @Override
        protected void onPreExecute() {
            binding.requestProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL requestUrl = urls[0];
            String[] result = null;
            try {
                if (selectedMethod.equals("GET")) {
                    result = RequestUtils.getResultUsingGetMethod(requestUrl, headers);
                } else {
                    result = RequestUtils.getResultUsingPOSTMethod(requestUrl, headers, body);
                }


            } catch (IOException e) {
                result = new String[]{null, "An Error , Please make sure that you add a valid data"};
            }

            return result;
        }

        @Override
        protected void onPostExecute(String[] s) {
            if (s[0] != null) {
                binding.responseCode.setText("Response code : " + s[0]);
            } else {
                binding.responseCode.setText("");
            }

            binding.textView.setText(s[1]);
            binding.requestProgress.setVisibility(View.INVISIBLE);
        }

    }

    private void spinnerConfiguration() {
        String[] requestMethods = {"GET", "POST"};
        ArrayAdapter<String> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, requestMethods);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.methodSpinner.setAdapter(adapterCourses);
    }


    private void tapConfiguration() {
        TapAdapter tapAdapter = new TapAdapter(getSupportFragmentManager(), getLifecycle());
        binding.requestInfoTap.addTab(binding.requestInfoTap.newTab().setText("Headers"));
        binding.requestInfoTap.addTab(binding.requestInfoTap.newTab().setText("params"));
        binding.requestInfoTap.addTab(binding.requestInfoTap.newTab().setText("Body"));
        binding.requestInfoVP.setAdapter(tapAdapter);

        binding.requestInfoTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.requestInfoVP.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.requestInfoVP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.requestInfoTap.selectTab(binding.requestInfoTap.getTabAt(position));

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (position == 0) {
                    if (HeadersFragment.headerFields != null) {
                        HeadersFragment.headerFields.requestLayout();
                    }

                } else if (position == 1) {
                    if (ParamsFragment.paramsFields != null) {
                        ParamsFragment.paramsFields.requestLayout();
                    }

                } else {
                    if (BodyFragment.bodyFields != null) {
                        BodyFragment.bodyFields.requestLayout();
                    }
                }
            }

        });
    }


}
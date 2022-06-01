package com.database.android_2022_internshiptask.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.database.android_2022_internshiptask.MainActivity;
import com.database.android_2022_internshiptask.R;
import com.database.android_2022_internshiptask.databinding.FragmentBodyBinding;

public class BodyFragment extends Fragment {
    private FragmentBodyBinding binding;

    public static  LinearLayout bodyFields;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        binding = FragmentBodyBinding.inflate(inflater);
        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bodyFields = binding.bodyFields;

        binding.addBodyFiled.setOnClickListener(view1 -> {
            addView();
        });

    }

    private void addView() {
        View bodyFiled = getLayoutInflater().inflate(R.layout.request_field_item,null,false);

        ImageView remove = (ImageView) bodyFiled.findViewById(R.id.remove_field);

        remove.setOnClickListener(view -> {
            removeFiled(bodyFiled);
        });


        bodyFields.addView(bodyFiled);
    }

    private void removeFiled(View view) {

        EditText key = view.findViewById(R.id.filed_key);
        MainActivity.body.remove(key.getText().toString());
        bodyFields.removeView(view);
    }





}

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
import com.database.android_2022_internshiptask.databinding.FragmentHeadersBinding;
import com.database.android_2022_internshiptask.databinding.FragmentParamsBinding;

public class ParamsFragment  extends Fragment {

    private FragmentParamsBinding binding;
    public static LinearLayout paramsFields;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        binding = FragmentParamsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paramsFields = binding.paramsFields;
        binding.addParamsFiled.setOnClickListener(view1 -> {
            addView();
        });
    }

    private void addView() {
        View paramFiled = getLayoutInflater().inflate(R.layout.request_field_item, null, false);

        ImageView remove = (ImageView) paramFiled.findViewById(R.id.remove_field);

        remove.setOnClickListener(view -> {
            removeFiled(paramFiled);
        });


        paramsFields.addView(paramFiled);
    }

    private void removeFiled(View view) {

        EditText key = view.findViewById(R.id.filed_key);
        MainActivity.params.remove(key.getText().toString());
        paramsFields.removeView(view);
    }
}

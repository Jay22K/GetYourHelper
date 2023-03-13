package com.srijan.empmgmt.ui.loadingPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.srijan.empmgmt.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class MainLoad extends Fragment {
    private CircularProgressButton btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loading, container, false);
        btn=root.findViewById(R.id.loading);
        btn.startAnimation();
        btn.setBackgroundResource(R.drawable.button);
        return root;
    }


}

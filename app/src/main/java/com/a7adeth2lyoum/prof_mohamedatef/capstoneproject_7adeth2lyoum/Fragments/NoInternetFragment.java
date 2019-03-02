package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;


/**
 * Created by Prof-Mohamed Atef on 2/3/2019.
 */

public class NoInternetFragment extends Fragment {
    ImageView btn_reload;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_nointernet_connection, container, false);
        btn_reload=(ImageView)mainView.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((onReloadInternetServiceListener)getActivity()).ReloadInternetService();
            }
        });
        return mainView;
    }

    public interface onReloadInternetServiceListener{
        void ReloadInternetService();
    }
}

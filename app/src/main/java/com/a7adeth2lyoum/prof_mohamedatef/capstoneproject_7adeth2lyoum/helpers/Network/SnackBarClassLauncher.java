package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Network;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;

/**
 * Created by Prof-Mohamed Atef on 2/3/2019.
 */

public class SnackBarClassLauncher {
    private Snackbar snackbar;

    public void SnackBarInitializer(Snackbar snackbar) {
        ShowSnackMessage(snackbar);
    }

    private void ShowSnackMessage(Snackbar snackbar) {
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void SnackBarLoadedData(View view, Context mContext) {
        snackbar = Snackbar
                .make(view, mContext.getResources().getString(R.string.loadedsuccess), Snackbar.LENGTH_LONG);
        ShowSnackMessage(snackbar);
    }


}
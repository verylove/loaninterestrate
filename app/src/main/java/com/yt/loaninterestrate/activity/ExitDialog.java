package com.yt.loaninterestrate.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.yt.loaninterestrate.R;

/**
 * Created by Yang on 2015/4/3.
 */
public class ExitDialog  extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.exit_layout);
    }

    public ExitDialog(Context context, int theme) {
        super(context, theme);
    }
}

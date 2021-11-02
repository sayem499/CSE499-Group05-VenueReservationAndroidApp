package com.reservation.app.ui.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;

/**
 * @author Fatema
 * since 9/1/21.
 */
public class DialogBuilder {

    public static Dialog buildOkDialog(Context context, String message) {
        return new AlertDialog
                .Builder(context)
                .setPositiveButton(android.R.string.ok, null)
                .setMessage(message)
                .create();
    }

    public static Dialog buildDialog(Context context, String message) {
        return new AlertDialog
                .Builder(context)
                .setMessage(message)
                .create();
    }

    public static Dialog buildProgressDialog(Context context) {
        return new SpotsDialog.Builder().setContext(context).build();
    }
}

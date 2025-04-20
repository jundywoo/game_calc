package ng.ken.gamecalc.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogUtils {

    public static void confirm(Context context, String title, OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("是", listener)
                .setNegativeButton("否", null)
                .show();
    }

    public static void warning(Context context, String title) {
        warning(context, title, "明白");
    }

    public static void warning(Context context, String title, String text) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(text, null)
                .show();
    }

}

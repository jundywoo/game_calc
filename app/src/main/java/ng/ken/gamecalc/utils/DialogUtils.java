package ng.ken.gamecalc.utils;

import static ng.ken.gamecalc.utils.Constants.NO;
import static ng.ken.gamecalc.utils.Constants.UNDERSTOOD;
import static ng.ken.gamecalc.utils.Constants.YES;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogUtils {

    public static void confirmWithView(
            Context context,
            String title,
            View view,
            String okText,
            OnClickListener okListener,
            String cancelText,
            OnClickListener cancelListener
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title);
        if (okText != null) {
            builder.setPositiveButton(okText, okListener);
        }
        if (cancelText != null) {
            builder.setNegativeButton(cancelText, cancelListener);
        }

        if (view != null) {
            builder.setView(view);
        }

        builder.show();
    }

    public static void confirm(Context context, String title, OnClickListener listener) {
        confirmWithView(context, title, null, YES, listener, NO, null);
    }

    public static void warning(Context context, String title) {
        warning(context, title, UNDERSTOOD);
    }

    public static void warning(Context context, String title, String text) {
        confirmWithView(context, title, null, text, null, null, null);
    }

}

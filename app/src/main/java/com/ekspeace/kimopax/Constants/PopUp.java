package com.ekspeace.kimopax.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.R;
import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class PopUp extends AppCompatActivity {

    public static void Network(Context context) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);
        TextView tvTitle = alertDialog.findViewById(R.id.dialog_title);
        TextView tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        ImageView imIcon = alertDialog.findViewById(R.id.dialog_icon);
        CardView cardView = alertDialog.findViewById(R.id.pop_up_cardView);
        MaterialButton btnClose = alertDialog.findViewById(R.id.dialog_close);
        MaterialButton btnConfirm = alertDialog.findViewById(R.id.dialog_confirm);

        tvTitle.setText("Connection");
        tvDesc.setText("Please check your internet connectivity and try again");
        imIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wifi));


        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

      btnConfirm.setOnClickListener(v -> {
          alertDialog.cancel();
          alertDialog.dismiss();
          EventBus.getDefault().postSticky(new NetworkConnectionEvent(true));

      });
        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            alertDialog.onBackPressed();
        });

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();
        }


    }

}

package net.tubusu.movapic_uploader;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.text.SpannableStringBuilder;

public class AppModule {
  public static SharedPreferences config;

  public static void initConfig(SharedPreferences c) {
    config = c;
  }

  public static void setMovapicMail(Activity act) {
    final View diagv = act.getLayoutInflater().inflate(R.layout.email_dialog, null);
    final Dialog diag = new Dialog(act);
    diag.setContentView(diagv);
    diag.setTitle("Setup");
    diag.findViewById(R.id.ok_btn_email_diag).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            EditText t = (EditText)diagv.findViewById(R.id.textbox_email_diag);
            String em = ((SpannableStringBuilder)t.getText()).toString();
            if (em.length() != 0) {
              SharedPreferences.Editor e = config.edit();
              e.putString("mail",em);
              e.commit();
              diag.dismiss();
            }
          }
        }
        );
    String emo = config.getString("mail","");
    ((EditText)diag.findViewById(R.id.textbox_email_diag)).setText(emo, TextView.BufferType.NORMAL);
    diag.show();
  }

}

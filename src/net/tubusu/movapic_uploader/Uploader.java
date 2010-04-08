package net.tubusu.movapic_uploader;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;
import android.net.Uri;
import android.widget.EditText;
import android.text.SpannableStringBuilder;

public class Uploader extends Activity
{
    private String img_path;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Activity u = this;
        Intent intent = getIntent();
        img_path = (String)intent.getSerializableExtra("img");
        setContentView(R.layout.main);
        findViewById(R.id.post_upl).setOnClickListener(
          new View.OnClickListener() {
            public void onClick(View v) {
              Intent mi = new Intent(Intent.ACTION_SEND);
              mi.putExtra(Intent.EXTRA_EMAIL, new String[] {getSharedPreferences("movapic_uploader", 0).getString("mail","")});
              mi.putExtra(Intent.EXTRA_TEXT, ((EditText)findViewById(R.id.comment_upl)).getText());
              mi.setType("image/jpeg");
              mi.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+img_path));
              mi.putExtra(Intent.EXTRA_SUBJECT, ((SpannableStringBuilder)((EditText)findViewById(R.id.command_upl)).getText()).toString());
              startActivity(Intent.createChooser(mi, "Choose mail client"));
              u.finish();
            }
          }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, 0, 0, "Edit movapic email address").setIcon(android.R.drawable.ic_menu_manage);
      return true;
    }

    @Override
    public boolean onMenuItemSelected(int feature_id, MenuItem item) {
      super.onMenuItemSelected(feature_id, item);
      switch(item.getItemId()) {
        case 0:
          AppModule.initConfig(getSharedPreferences("movapic_uploader", 0));
          AppModule.setMovapicMail(this);
          break;
        default:
          break;
      }
      return true;
    }
}

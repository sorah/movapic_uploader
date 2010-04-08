package net.tubusu.movapic_uploader;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.os.Bundle;

public class CameraActivity extends Activity {
  CameraView camera_view;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    camera_view = new CameraView(this);
    AppModule.initConfig(getSharedPreferences("movapic_uploader", 0));
    if(AppModule.config.getString("mail","").length() == 0) {
      AppModule.setMovapicMail(this);
    }
    setContentView(camera_view);
  }

  @Override
  protected void onResume() {
    super.onResume();
    camera_view.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    camera_view.onPause();
  }
}

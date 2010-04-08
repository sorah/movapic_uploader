package net.tubusu.movapic_uploader;

import java.io.FileOutputStream;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class CameraView extends SurfaceView
       implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.AutoFocusCallback {
  private              SurfaceHolder   surface_holder;
  private              Camera          camera;
  private static       ContentResolver content_resolver = null;
  private static final String          SD_CARD          = "/sdcard/";
  private Context cont;
  private Activity acti;

  public CameraView(Context context) {
      super(context);
      cont = context;
      acti = (Activity)context;

      surface_holder = getHolder();
      surface_holder.addCallback(this);
      surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
      content_resolver = context.getContentResolver();
  }

  public void surfaceCreated(SurfaceHolder holder) {
    camera = Camera.open();
    setCameraParameters(camera,320,480);
    try {
      camera.setPreviewDisplay(holder);
    } catch (Exception e) {
      cameraRelease();
    }
  }

  private void setCameraParameters(Camera camera,
                                   int height, int width) {
    Camera.Parameters params = camera.getParameters();
    params.setPictureSize(width,height);
    camera.setParameters(params);
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    cameraRelease();
  }

  public void surfaceChanged(SurfaceHolder holder,
                             int format,
                             int width, int height) {
    setCameraParameters(camera,width,height);
    camera.startPreview();
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    final CameraView m = this;
    if (e.getAction() == MotionEvent.ACTION_DOWN) {
      android.util.Log.w("Movapic","Focus");
      camera.autoFocus(m);
    }
    return true;
  }

  public void onAutoFocus(boolean success, Camera cam) {
      android.util.Log.w("Movapic","takePicture");
      try {
        cam.takePicture(null, null, this);
      } catch (Exception e) {
        cameraRelease();
      }
  }

  public void onPictureTaken(byte[] data, Camera camera) {
    try {
      android.util.Log.w("Movapic","Taken");
      String data_name = SD_CARD + "movapic_" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
      savePic(data, data_name);
      Intent i = new Intent(cont,Uploader.class);
      i.putExtra("img", data_name);
      i.setAction(Intent.ACTION_VIEW);
      acti.startActivity(i);
      acti.finish();
    } catch (Exception e) {
      cameraRelease();
    }
  }

  private void savePic(byte[] data, String data_name) throws Exception {
    FileOutputStream f = null;
    try {
      f = new FileOutputStream(data_name);
      f.write(data);
    } catch (Exception e) {
      cameraRelease();
    } finally {
      if (f != null) {
        f.close();
      }
    }
  }

  private void cameraRelease() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  public void onResume() {
    cameraRelease();
  }

  public void onPause() {
    cameraRelease();
  }
}

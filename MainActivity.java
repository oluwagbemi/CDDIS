
package com.example.cddis;

import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.DialogInterface;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
	TextView displayText;
	ImageView imagetoUpload;
    Button camera,send;
    int TAKE_PHOTO_CODE = 0;
    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/CassavaImages/";
    private String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		displayText = (TextView)findViewById(R.id.distxt);
		camera  = (Button)findViewById(R.id.camera);
		send  = (Button)findViewById(R.id.send);
		imagetoUpload = (ImageView)findViewById(R.id.imagetoUpload);
		imagetoUpload.setOnClickListener(this);
		camera.setOnClickListener(this);
		send.setOnClickListener(this);
	
		 File newdir = new File(dir);
	        newdir.mkdirs();
		
		 }
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.imagetoUpload:
			
			 break;
		case R.id.camera:
			
			Toast.makeText(getApplicationContext(),"Openinig Camera",Toast.LENGTH_LONG).show();
			 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		        String imageFileName = "JPEG_" + timeStamp + "_";
			String file = dir+imageFileName+".jpg";
            path = file;
            File newfile = new File(file);
            try {
                newfile.createNewFile();
            }
            catch (IOException e)
            {
            }

            Uri outputFileUri = Uri.fromFile(newfile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
		       break; 
		       
		case R.id.send:
			if(check(displayText.getText().toString())) {
			Toast.makeText(getApplicationContext(),"Opening Browser",Toast.LENGTH_LONG).show();
			//dis.setText(convertImgToBase64());
			CallAPI myAPI = new CallAPI(MainActivity.this);
      myAPI.execute(convertImgToBase64());
			}
			else {validate();}
				
			 break;
		}
		
	}
	
	private String convertImgToBase64(){
		//encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            displayText.setText(path);
            imagetoUpload.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
	
	private boolean check(String t) {
		if(!t.equals("Captured image location"))
			return true;
		else 
			return false;
	}
	
	protected void validate() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setCancelable(false);
		dialog.setTitle("CDDIS");
		dialog.setMessage("Capture a cassava leaf image");
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int id) {
		        //Action for "Delete".
		    }
		})
		        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            //Action for "Cancel".
		            }
		        });

		final AlertDialog alert = dialog.create();
		alert.show();
		
	}
	
}


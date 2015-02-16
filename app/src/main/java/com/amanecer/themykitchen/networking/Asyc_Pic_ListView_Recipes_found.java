package com.amanecer.themykitchen.networking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.amanecer.themykithcen.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amanecer on 14/12/2014.
 */
public class Asyc_Pic_ListView_Recipes_found extends AsyncTask<String,Integer,Bitmap> {

    Activity activity;

    public Asyc_Pic_ListView_Recipes_found(Activity activity) {
       this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap pic = downloadImage(params[0]);

        return pic;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        ImageView  recipeImg=(ImageView)activity.findViewById(R.id.Path);
        recipeImg.setImageBitmap(bitmap);
    }

    private Bitmap downloadImage(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            InputStream is = httpCon.getInputStream();
            int fileLength = httpCon.getContentLength();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead, totalBytesRead = 0;
            byte[] data = new byte[2048];
            //mDialog.setMax(fileLength);
            // Read the image bytes in chunks of 2048 bytes
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                totalBytesRead += nRead;
                //publishProgress(totalBytesRead);
            }
            buffer.flush();
            byte[] image = buffer.toByteArray();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

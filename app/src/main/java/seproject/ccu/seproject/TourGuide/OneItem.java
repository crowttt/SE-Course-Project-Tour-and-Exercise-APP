package seproject.ccu.seproject.TourGuide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OneItem {
    private String name ;
    private String describe ;
    private Bitmap bitmap;

    public OneItem(final Attraction ac ){

        name = ac.getName() ;
        describe = ac.getDescribe() ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = getBitmapFromURL( ac.getPhoto() );
            }}).start();
    }


    public static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() { return name; }
    public String getDescribe() { return describe; }
    public Bitmap getBitmap() { return bitmap ; }

}

package cn.a6_79.wepay_buyer.NetPack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageTask {
    public static Bitmap getBitmap(String s) {
        Bitmap bitmap, bitmapFinal = null;
        try {
            URL url = new URL(s);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.connect();
            InputStream is = mHttpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//            for (int i = 0; i < width * height; i++)
//                if ((pixels[i] & 0X00FFFFFF) == 0X00FFFFFF)
//                    pixels[i] = 0X00000000;
            bitmapFinal = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapFinal.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapFinal;
    }
}

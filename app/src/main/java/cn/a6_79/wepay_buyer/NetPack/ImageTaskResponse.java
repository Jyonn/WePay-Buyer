package cn.a6_79.wepay_buyer.NetPack;

import android.graphics.Bitmap;

public class ImageTaskResponse {
    private Bitmap bitmap;
    ImageTaskResponse(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() { return bitmap; }
}


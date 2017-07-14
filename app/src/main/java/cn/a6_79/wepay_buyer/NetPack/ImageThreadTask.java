package cn.a6_79.wepay_buyer.NetPack;

import android.os.AsyncTask;

public class ImageThreadTask extends AsyncTask<Void, Integer, ImageTaskResponse> {
    private OnAsyncImageTaskListener listener;
    private String avatarUrl;
    public ImageThreadTask(String avatarUrl, OnAsyncImageTaskListener listener) {
        this.avatarUrl = avatarUrl;
        this.listener = listener;
    }

    @Override
    protected ImageTaskResponse doInBackground(Void... voids) {
        return new ImageTaskResponse(ImageTask.getBitmap(avatarUrl));
    }

    @Override
    protected void onPostExecute(ImageTaskResponse imageTaskResponse) {
        super.onPostExecute(imageTaskResponse);
        if (isCancelled())
            return;
        if (listener != null) {
            listener.callback(imageTaskResponse);
        }
    }
}

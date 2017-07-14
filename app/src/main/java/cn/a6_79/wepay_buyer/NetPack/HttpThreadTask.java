package cn.a6_79.wepay_buyer.NetPack;

import android.os.AsyncTask;

import org.json.JSONException;

public class HttpThreadTask extends AsyncTask<Void, Integer, HttpTaskResponse> {
    private OnAsyncHttpTaskListener listener;
    private HttpTaskRequest httpTaskRequest;
    public HttpThreadTask(HttpTaskRequest httpTaskRequest, OnAsyncHttpTaskListener listener) {
        this.httpTaskRequest = httpTaskRequest;
        this.listener = listener;
    }

    @Override
    protected HttpTaskResponse doInBackground(Void... voids) {
        return HttpTask.htmlTask(httpTaskRequest);
    }

    @Override
    protected void onPostExecute(HttpTaskResponse httpTaskResponse) {
        super.onPostExecute(httpTaskResponse);
        if (isCancelled())
            return;
        if (listener != null) {
            try {
                listener.callback(httpTaskResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

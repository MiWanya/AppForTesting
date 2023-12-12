package com.example.tests;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileDownload {
    public static void downloadFile(final Context context, String fileUrl, final FileDownloadListener listener) {
        if (!isNetworkAvailable(context)) {
            // Show message after a delay of 10 seconds
            showToastDelayed(context, "Не стабильное или отсутствует подключение к интернету", 15000);
            return;
        }

        FileDownloadApi fileDownloadApi = RetrofitClient.getClient().create(FileDownloadApi.class);

        Call<ResponseBody> call = fileDownloadApi.downloadFile(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Save the file
                    if (listener != null) {
                        listener.onDownloadComplete(response.body());
                    }
                } else {
                    if (listener != null) {
                        listener.onDownloadFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onDownloadFailed();
                }
            }
        });
    }

    private static void showToastDelayed(final Context context, final String message, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                // Close the activity or perform other actions if needed
                ((Activity) context).finish();
            }
        }, delayMillis);
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public interface FileDownloadListener {
        void onDownloadComplete(ResponseBody responseBody);

        void onDownloadFailed();
    }
}

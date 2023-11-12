package com.example.tests;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileDownload {
    public static void downloadFile(String fileUrl, final FileDownloadListener listener) {
        FileDownloadApi fileDownloadApi = RetrofitClient.getClient().create(FileDownloadApi.class);

        Call<ResponseBody> call = fileDownloadApi.downloadFile(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Сохранение файла
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

    public interface FileDownloadListener {
        void onDownloadComplete(ResponseBody responseBody);
        void onDownloadFailed();
    }
}

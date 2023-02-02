package com.example.flickrbrowser2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlikrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlikrJsonData";
    private List<Photo> mPhotoList;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;
    private boolean runningOnSameThread = false;
    private final OnDownloadAvailable mCallback;

    interface OnDownloadAvailable {
        void onDownloadAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlikrJsonData(OnDownloadAvailable callback, String baseUrl, String language, boolean matchAll) {
        Log.d(TAG, "GetFlikrJsonData: called");
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallback = callback;
    }

    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);

    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if (mCallback != null) {
            mCallback.onDownloadAvailable(mPhotoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(strings[0], mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return mPhotoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri: starts");
//        Uri uri=Uri.parse(mBaseUrl);
//        Uri.Builder builder=uri.buildUpon();
//        builder=builder.appendQueryParameter("tags", searchCriteria);
//        builder=builder.appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY");
//        builder=builder.appendQueryParameter("lang", lang);
//        builder=builder.appendQueryParameter("format", "json");
//        builder=builder.appendQueryParameter("nojsoncallback", "1");
//        uri=builder.build();
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Status" + status);
        if (status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
//                    String authorId = jsonPhoto.getString("authorId");
                    String tags = jsonPhoto.getString("tags");
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");
                    String link = photoUrl.replaceFirst("_m.", "_b.");
                    Photo photoObject = new Photo(title, author, tags, link, photoUrl);
                    mPhotoList.add(photoObject);
                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                ;
                Log.e(TAG, "onDownloadComplete: Error processing Json data" + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if (runningOnSameThread && mCallback != null) {
            mCallback.onDownloadAvailable(mPhotoList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}


package httptest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Test1 {

    interface SuccessResult {
        void OnSuccess(String result);
    }

     interface FailedResult {
        void OnFailed();
    }



    public Test1(final String url, final HttpMethed1 httpMethed1,
                 final SuccessResult successresult, final FailedResult result2,
                 final String... kvs) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < kvs.length; i += 2) {
                    buffer.append(kvs[i]).append("=").append(kvs[i + 1])
                            .append("&");
                }
                Log.e("TAGG", buffer.toString());
                URLConnection uc;
                try {
                    switch (httpMethed1) {
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);

                            BufferedWriter bw = new BufferedWriter(
                                    new OutputStreamWriter(uc.getOutputStream(),
                                            HttpConfig.CHARSET));
                            bw.write(buffer.toString());
                            bw.flush();
                            break;

                        default:
                            uc = new URL(url + "?" + buffer.toString())
                                    .openConnection();
                            break;

                    }
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(uc.getInputStream(),
                                    HttpConfig.CHARSET));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    Log.e("TAGG", buffer.toString());
                    Log.e("TAGG", result.toString());
                    return result.toString();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }

            }

            protected void onPostExecute(String result) {
                if (result != null) {
                    if (successresult != null) {
                        successresult.OnSuccess(result);
                    }
                } else {
                    if (result2 != null) {
                        result2.OnFailed();
                    }
                }

            }

        }.execute();
    }
}

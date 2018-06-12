package httputil;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequest {


    public static String getString(String url) {
        System.out.println("getString url : " + url);
        StringBuilder response = new StringBuilder();

        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);

            int responseCode = conn.getResponseCode();

            System.out.println("Response Code : " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("get response", response.toString());
        return response.toString();

    }

    public static String postString(String requestURL, Map<String, String> postDataParams) throws IOException {
        System.out.println("postString url : " + requestURL);
        URL url;
        StringBuilder response = new StringBuilder();
        url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(6000);
        conn.setConnectTimeout(6000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getEncodedString(postDataParams));
        writer.flush();
        writer.close();
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String line;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
        }

        Log.i("Post response", response.toString());

        return response.toString();
    }


    public static String getEncodedString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));// MIME
                // applicaion/x-www-form-urlencoded
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
        }

        return result.toString();
    }


    public static class MultipartUpload {
        private String CRLF = "\r\n";
        private String boundary = Long.toHexString(System.currentTimeMillis());// ����һЩ����ַ���
        private String charset = "UTF-8";
        private PrintWriter writer;
        private HttpURLConnection connection;
        private int i = 0, j = 0;
        private OutputStream output;

        public MultipartUpload(String url) throws IOException {

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            output = new BufferedOutputStream(connection.getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

        }

        public MultipartUpload addText(String param) {

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param" + (i++) + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();
            return this;
        }

        public MultipartUpload addBinaryFile(String filepath) throws IOException {
            File file = new File(filepath);
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"file" + (j++) + "\";filename=\"" + file.getName()
                            + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }
            in.close();
            output.flush();
            writer.append(CRLF).flush();

            return this;
        }

        public MultipartUpload addBinaryFile(InputStream inputStream, String filename) throws IOException {
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"images\";filename=\"" + filename     //
                            + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(filename)).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            InputStream in = new BufferedInputStream(inputStream);
            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }
            in.close();
            output.flush();
            writer.append(CRLF).flush();

            return this;

        }

        public MultipartUpload addTextFile(String filepath) throws IOException {
            File file = new File(filepath);
            writer.append("--" + boundary).append(CRLF);
            writer.append(
                    "Content-Disposition: form-data; name=\"images\";filename=\"" + file.getName()
                            + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).flush();
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] buffer = new byte[1024];
            while ((bytes = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }
            in.close();
            output.flush();
            writer.append(CRLF).flush();

            return this;

        }

        public String finish() throws IOException {
            writer.append("--" + boundary + "--").append(CRLF).flush();
            writer.close();
            StringBuilder response = new StringBuilder();

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
            } else {
                throw new IOException();
            }

//			System.out.println("MultiPartUpload Response: " + response.toString());

            return response.toString();
        }

    }
}

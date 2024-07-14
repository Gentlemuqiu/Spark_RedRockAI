package com.example.api.orc;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.api.orc.dataBean.DataClass;
import com.example.api.orc.dataBean.JsonParse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * author : lytMoon
 * date: 2024/4/19 12:52
 * version : 1.0
 * description : 请通过OCR.getInstance()方法获取实例，并且传入一个参数图片通过base64编码后的String
 * 通过 val result = OCR.getInstance().getOCRResult 获取最终返回的数据
 * saying : 这世界天才那么多，也不缺我一个
 */
public class OCR {

    public static String requestUrl;
    public static String appID;
    public static String apiSecret;

    static {
        requestUrl = "https://api.xf-yun.com/v1/private/hh_ocr_recognize_doc";
        appID = "82d5a760";
        apiSecret = "OGY2M2FhMGJiMTMxN2QzMWRiYTZjMzc5";
    }

    private String imageBase64;

    private static final Gson json = new Gson();

    private static volatile OCR ocrInstance;

    /**
     * 传入图片的base64解码后的String
     */
    public static OCR getInsance(String imageBase64) {
        if (ocrInstance == null) {
            synchronized (OCR.class) {
                if (ocrInstance == null) {
                    ocrInstance = new OCR(imageBase64);
                }
            }
        }
        return ocrInstance;
    }

    private OCR(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    private OCR() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DataClass getOCRResult() {
        try {
            String resp = doRequest();
            JsonParse myJsonParse = json.fromJson(resp, JsonParse.class);
            String base64String = myJsonParse.getPayload().getRecognizeDocumentRes().getText();
            String decodedString = null;
            decodedString = new String(Base64.getDecoder().decode(base64String));
            Gson gson = new Gson();

            return gson.fromJson(decodedString, DataClass.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求主方法
     *
     * @return 返回服务结果
     * @throws Exception 异常
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String doRequest() throws Exception {

        URL realUrl = new URL(buildRequestUrl());
        URLConnection connection = realUrl.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-type", "application/json");

        OutputStream out = httpURLConnection.getOutputStream();
        String params = buildParam();

        out.write(params.getBytes());
        out.flush();
        InputStream is = null;
        try {
            is = httpURLConnection.getInputStream();
        } catch (Exception e) {
            is = httpURLConnection.getErrorStream();
            throw new Exception("make request error:" + "code is " + httpURLConnection.getResponseMessage() + readAllBytes(is));
        }
        return readAllBytes(is);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String buildRequestUrl() {
        URL url = null;
        String httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://", "https://");
        try {
            url = new URL(httpRequestUrl);

            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            String host = url.getHost();


            String builder = "host: " + host + "\n" +
                    "date: " + date + "\n" +
                    "POST " + url.getPath() + " HTTP/1.1";

            Charset charset = StandardCharsets.UTF_8;
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.getBytes(charset));
            String sha = null;
            sha = Base64.getEncoder().encodeToString(hexDigits);
            String apiKey = "7a1bbccf7648b6a20218ea30eb529db5";
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            String authBase = null;
            authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
            return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));
        } catch (Exception e) {
            throw new RuntimeException("assemble requestUrl error:" + e.getMessage());
        }
    }


    private String buildParam() throws Exception {
        String param = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            param = "{" +
                    "    \"header\": {" +
                    "        \"app_id\": \"" + appID + "\"," +
                    "        \"status\": 3" +
                    "    }," +
                    "    \"parameter\": {" +
                    "        \"hh_ocr_recognize_doc\": {" +
                    "            \"recognizeDocumentRes\": {" +
                    "                \"encoding\": \"utf8\"," +
                    "                \"compress\": \"raw\"," +
                    "                \"format\": \"json\"" +
                    "            }" +
                    "        }" +
                    "    }," +
                    "    \"payload\": {" +
                    "        \"image\": {" +
                    "            \"encoding\": \"jpg\"," +
                    "            \"image\": \"" + imageBase64 + "\"," +
                    "            \"status\": 3" +
                    "        }" +
                    "    }" +
                    "}";
        }
        return param;
    }


    private String readAllBytes(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.lines().collect(Collectors.joining(System.lineSeparator()));
    }


}



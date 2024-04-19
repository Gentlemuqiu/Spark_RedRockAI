package com.lytmoon.identify;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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
 * author : 29364
 * date: 2024/4/19 12:17
 * version : 1.0
 * description :请通过OCR.getInstance()方法获取实例，并且传入一个参数图片通过base64编码后的String
 * 通过 val result = OCR.getInstance().getOCRResult 获取最终返回的数据
 */
public class OCR {

    private String imageBase64;
    private String requestUrl = "https://api.xf-yun.com/v1/private/hh_ocr_recognize_doc";
    private String appid = "82d5a760";
    private String apiSecret = "OGY2M2FhMGJiMTMxN2QzMWRiYTZjMzc5";
    private static Gson json = new Gson();

    private static volatile OCR ocrInstance;

    /**
     * 传入图片的base64解码后的String
     */
    public static OCR getInstance(String imageBase64) {
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
            String base64String = myJsonParse.payload.recognizeDocumentRes.text;
            String decodedString = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decodedString = new String(Base64.getDecoder().decode(base64String));
            }
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
        //System.out.println("params=>"+params);
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
        // 替换调schema前缀 ，原因是URL库不支持解析包含ws,wss schema的url
        String httpRequestUrl = requestUrl.replace("ws://", "http://").replace("wss://", "https://");
        try {
            url = new URL(httpRequestUrl);
            //获取当前日期并格式化
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            String host = url.getHost();
			/*if (url.getPort()!=80 && url.getPort() !=443){
				host = host +":"+String.valueOf(url.getPort());
			}*/
            StringBuilder builder = new StringBuilder("host: ").append(host).append("\n").//
                    append("date: ").append(date).append("\n").//
                    append("POST ").append(url.getPath()).append(" HTTP/1.1");

            Charset charset = StandardCharsets.UTF_8;
            Mac mac = Mac.getInstance("hmacsha256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
            String sha = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                sha = Base64.getEncoder().encodeToString(hexDigits);
            }

            String apiKey = "7a1bbccf7648b6a20218ea30eb529db5";
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            String authBase = null;
            authBase = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
            return String.format("%s?authorization=%s&host=%s&date=%s", requestUrl, URLEncoder.encode(authBase), URLEncoder.encode(host), URLEncoder.encode(date));
        } catch (Exception e) {
            throw new RuntimeException("assemble requestUrl error:" + e.getMessage());
        }
    }

    /**
     * 组装请求参数
     * 直接使用示例参数，
     * 替换部分值
     *
     * @return 参数字符串
     */
    private String buildParam() throws Exception {
        String param = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            param = "{" +
                    "    \"header\": {" +
                    "        \"app_id\": \"" + appid + "\"," +
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


    class JsonParse {
        public Header header;
        public Payload payload;

        public JsonParse(Header header, Payload payload) {
            this.header = header;
            this.payload = payload;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }

        @Override
        public String toString() {
            return "JsonParse{" +
                    "header=" + header +
                    ", payload=" + payload +
                    '}';
        }
    }

    class Header {
        public int code;
        public String message;
        public String sid;

        public Header(int code, String message, String sid) {
            this.code = code;
            this.message = message;
            this.sid = sid;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        @NonNull
        @Override
        public String toString() {
            return "Header{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", sid='" + sid + '\'' +
                    '}';
        }
    }

    class Payload {
        public Result recognizeDocumentRes;

        public Payload(Result recognizeDocumentRes) {
            this.recognizeDocumentRes = recognizeDocumentRes;
        }

        public Result getRecognizeDocumentRes() {
            return recognizeDocumentRes;
        }

        public void setRecognizeDocumentRes(Result recognizeDocumentRes) {
            this.recognizeDocumentRes = recognizeDocumentRes;
        }

        @NonNull
        @Override
        public String toString() {
            return "Payload{" +
                    "recognizeDocumentRes=" + recognizeDocumentRes +
                    '}';
        }
    }

    class Result {
        public String compress;
        public String encoding;
        public String format;
        public String text;

        public Result(String compress, String encoding, String format, String text) {
            this.compress = compress;
            this.encoding = encoding;
            this.format = format;
            this.text = text;
        }

        public String getCompress() {
            return compress;
        }

        public void setCompress(String compress) {
            this.compress = compress;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @NonNull
        @Override
        public String toString() {
            return "Result{" +
                    "compress='" + compress + '\'' +
                    ", encoding='" + encoding + '\'' +
                    ", format='" + format + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

}



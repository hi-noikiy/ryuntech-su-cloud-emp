package com.ryuntech.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class HttpUtils {

    public static String METHOD_GET = "GET";
    public static String METHOD_POST = "POST";

    public static int DEF_CONNECT_TIMEOUT = 5 * 1000;
    public static int DEF_READ_TIMEOUT = 25 * 1000;
    public static Charset DEF_CHARSET = Charset.forName("UTF-8");

    public static String Get(String urlString) throws Exception {
        return HttpGo(urlString, METHOD_GET, null, null, DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String Get(String urlString, Map<String, String> headers) throws Exception {
        return HttpGo(urlString, METHOD_GET, headers, null, DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String Get(String urlString, Map<String, String> headers, Map<String, String> params) throws Exception {
        if (params != null && params.isEmpty() == false) {
            StringBuffer url = new StringBuffer(urlString);
            try {
                boolean isFirst = true;
                if (urlString.contains("?")) {
                    if (urlString.endsWith("&") == false && urlString.contains("&")) {
                        isFirst = false;
                    }
                } else {
                    url.append('?');
                }
                String paramsEncoding = DEF_CHARSET.name();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        url.append('&');
                    }
                    url.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                    url.append('=');
                    url.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                }
            } catch (Exception e) {
            }
            return Get(url.toString(), headers);
        } else {
            return Get(urlString, headers);
        }
    }

    public static String Post(String urlString, String contentType, byte[] content) throws Exception {
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", contentType);
        return HttpGo(urlString, METHOD_POST, headers, content, DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String FormPost(String urlString, String content) throws Exception {
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", String.format("application/x-www-form-urlencoded; charset=%s", DEF_CHARSET.name()));
        return HttpGo(urlString, METHOD_POST, null, content.getBytes(DEF_CHARSET), DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String XmlPost(String urlString, String content) throws Exception {
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", String.format("text/html; charset=%s", DEF_CHARSET.name()));
        return HttpGo(urlString, METHOD_POST, headers, content.getBytes(DEF_CHARSET), DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String JsonPost(String urlString, Object content) throws Exception {
        return JsonPost(urlString, JSONObject.toJSONString(content, SerializerFeature.DisableCircularReferenceDetect));
    }

    public static String JsonPost(String urlString, String content) throws Exception {
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", String.format("application/json; charset=%s", DEF_CHARSET.name()));
        return HttpGo(urlString, METHOD_POST, headers, content.getBytes(DEF_CHARSET), DEF_CONNECT_TIMEOUT, DEF_READ_TIMEOUT);
    }

    public static String JsonPost(String urlString, String content, int readTimeout) throws Exception {
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", String.format("application/json; charset=%s", DEF_CHARSET.name()));
        return HttpGo(urlString, METHOD_POST, headers, content.getBytes(DEF_CHARSET), DEF_CONNECT_TIMEOUT, readTimeout);
    }

    public static String HttpGo(String urlString, String method, Map<String, String> headers, byte[] content, int connectTimeout, int readTimeout) throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (content != null) {
                if (headers == null || headers.containsKey("Content-Length") == false) {
                    conn.addRequestProperty("Content-Length", Integer.toString(content.length));
                }
                OutputStream output = null;
                try {
                    output = conn.getOutputStream();
                    output.write(content);
                    output.flush();
                } finally {
                    if (output != null) {
                        try { output.close(); } catch (Exception e) { }
                    }
                }
            }
            return readContent(conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream(), getCharset(conn));
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String encodeParams(Map<String, String> params, String paramsEncoding) throws Exception {
        boolean isFirst = true;
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                encodedParams.append('&');
            }
            encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
            encodedParams.append('=');
            encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
        }
        return encodedParams.toString();
    }

    public static volatile String CHARSET_DEF = DEF_CHARSET.name();
    private static String CHARSET_STR = "charset=";
    private static int CHARSET_STR_LEN = CHARSET_STR.length();
    private static String getCharset(HttpURLConnection conn) {
        String contentType = conn.getHeaderField("Content-Type");
        int length = contentType != null ? contentType.length() : 0;
        if (length < CHARSET_STR_LEN) {
            return CHARSET_DEF;
        }
        int pos = contentType != null ? contentType.indexOf("charset=") : -1;
        if (pos < 0) {
            return CHARSET_DEF;
        }
        return contentType.substring(pos + CHARSET_STR_LEN);
    }

    private static String readContent(InputStream input, String charset) throws Exception {
        try {
            int APPEND_LEN = 4 * 1024;
            int offset = 0;
            byte[] data = new byte[APPEND_LEN];
            while (true) {
                int len = input.read(data, offset, data.length - offset);
                if (len == -1) {
                    break;
                }
                offset += len;
                if (offset >= data.length) {
                    data = Arrays.copyOf(data, offset + APPEND_LEN);
                }
            }
            return charset != null ? new String(data, 0, offset, charset) : new String(data, 0, offset);
        } finally {
            if (input != null) try { input.close(); } catch (Exception e) { }
        }
    }
}

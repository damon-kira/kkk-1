package com.common.lib.net.logger;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.project.util.AESNormalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public HttpLogInterceptor() {
        this(new HttpLogger());
    }

    public HttpLogInterceptor(HttpLogger logger) {
        this.logger = logger;
    }

    private final HttpLogger logger;

    private volatile Set<String> headersToRedact = Collections.emptySet();

    public void redactHeader(String name) {
        Set<String> newHeadersToRedact = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        newHeadersToRedact.addAll(headersToRedact);
        newHeadersToRedact.add(name);
        headersToRedact = newHeadersToRedact;
    }

    private volatile HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpLogInterceptor setLevel(HttpLoggingInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return level;
    }

    private static final String TAG = "debug_HttpLogInterceptor";
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        if (AppEnv.DEBUG) {
//            HttpLoggingInterceptor.Level level = this.level;
//
//            Request request = chain.request();
//            String requestUrl=request.url().toString();
//            if (level == HttpLoggingInterceptor.Level.NONE) {
//                return chain.proceed(request);
//            }
//
//            boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
//            boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;
//
//            RequestBody requestBody = request.body();
//            boolean hasRequestBody = requestBody != null;
//
//            Connection connection = chain.connection();
//            String requestStartMessage = "--> "
//                    + request.method()
//                    + ' ' + request.url()
//                    + (connection != null ? " " + connection.protocol() : "");
//            if (!logHeaders && hasRequestBody) {
//                requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
//            }
//            logger.log(requestStartMessage);
//
//            if (logHeaders) {
//                if (hasRequestBody) {
//                    // Request body headers are only present when installed as a network interceptor. Force
//                    // them to be included (when available) so there values are known.
//                    if (requestBody.contentType() != null) {
//                        logger.log("Content-Type: " + requestBody.contentType());
//                    }
//                    if (requestBody.contentLength() != -1) {
//                        logger.log("Content-Length: " + requestBody.contentLength());
//                    }
//                }
//
//                Headers headers = request.headers();
//                for (int i = 0, count = headers.size(); i < count; i++) {
//                    String name = headers.name(i);
//                    // Skip headers from the request body as they are explicitly logged above.
//                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
//                        logHeader(headers, i);
//                    }
//                }
//
//                if (!logBody || !hasRequestBody) {
//                    logger.log("--> END " + request.method());
//                } else if (bodyHasUnknownEncoding(request.headers())) {
//                    logger.log("--> END " + request.method() + " (encoded body omitted)");
//                } else {
//                    Buffer buffer = new Buffer();
//                    requestBody.writeTo(buffer);
//
//                    Charset charset = UTF8;
//                    MediaType contentType = requestBody.contentType();
//                    if (contentType != null) {
//                        charset = contentType.charset(UTF8);
//                    }
//                    if (isPlaintext(buffer)) {
//                        logger.log("--------request map start--------"+requestUrl);
//                        String requestMap = buffer.readString(charset);
//                        logger.log(requestMap);
//                        logger.log("--------request map end--------"+requestUrl);
//                        String requestData = requestMap.replace("data=", "");
//                        logger.log("--------request map decrypt start--------"+requestUrl);
//                        logger.log(AESNormalUtil.mexicoDecrypt(requestData, true));
//                        logger.log("--------request map decrypt end--------"+requestUrl);
//
//                        logger.log("--> END " + request.method()
//                                + " (" + requestBody.contentLength() + "-byte body)");
//                    } else {
//                        logger.log("--> END " + request.method() + " (binary "
//                                + requestBody.contentLength() + "-byte body omitted)");
//                    }
//                }
//            }
//
//            long startNs = System.nanoTime();
//            Response response;
//            try {
//                response = chain.proceed(request);
//            } catch (Exception e) {
//                logger.log(HttpLogger.END_HTTP_FAILED + " url = " + requestUrl + "\n" + e);
//                throw e;
//            }
//            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
//
//            ResponseBody responseBody = response.body();
//            long contentLength = responseBody.contentLength();
//            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
//            String responseUrl=response.request().url().toString();
//            logger.log("<-- "
//                    + response.code()
//                    + (response.message().isEmpty() ? "" : ' ' + response.message())
//                    + ' ' + responseUrl
//                    + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
//
//            if (logHeaders) {
//                Headers headers = response.headers();
//                for (int i = 0, count = headers.size(); i < count; i++) {
//                    logHeader(headers, i);
//                }
//
//                if (!logBody || !HttpHeaders.hasBody(response)) {
//                    logger.log(HttpLogger.END_HTTP);
//                } else if (bodyHasUnknownEncoding(response.headers())) {
//                    logger.log(HttpLogger.END_HTTP + " (encoded body omitted)");
//                } else {
//                    BufferedSource source = responseBody.source();
//                    source.request(Long.MAX_VALUE); // Buffer the entire body.
//                    Buffer buffer = source.buffer();
//
//                    Long gzippedLength = null;
//                    if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
//                        gzippedLength = buffer.size();
//                        GzipSource gzippedResponseBody = null;
//                        try {
//                            gzippedResponseBody = new GzipSource(buffer.clone());
//                            buffer = new Buffer();
//                            buffer.writeAll(gzippedResponseBody);
//                        } finally {
//                            if (gzippedResponseBody != null) {
//                                gzippedResponseBody.close();
//                            }
//                        }
//                    }
//
//                    Charset charset = UTF8;
//                    MediaType contentType = responseBody.contentType();
//                    if (contentType != null) {
//                        charset = contentType.charset(UTF8);
//                    }
//
//                    if (!isPlaintext(buffer)) {
//                        logger.log("");
//                        logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
//                        return response;
//                    }
//
//                    if (contentLength != 0) {
//                        logger.log("########response Data start########"+responseUrl);
//                        try{
//                            String content = buffer.clone().readString(charset);
//                            logger.log(content);
//                            logger.log("########response Data  end################"+responseUrl);
//
//                            logger.log("########decrypt response Data start########"+responseUrl);
//                            logger.log(decryptJsonData(content));
//                            logger.log("########decrypt response Data  end########"+responseUrl);
//                        }catch (Exception e){
//                            logger.log("**********"+e.getMessage()+"**********");
//                        }
//
//
//                    }
//
//                    if (gzippedLength != null) {
//                        logger.log("<-- END HTTP (" + buffer.size() + "-byte, "
//                                + gzippedLength + "-gzipped-byte body)");
//                    } else {
//                        logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
//                    }
//                }
//            }
//            return response;
//        } else {
//            final Request request = chain.request();
//            final Response response = chain.proceed(request);
//            return response;
//        }
//
//    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor.Level level = this.level;
        Request request = chain.request();
        if (level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        String trackNo = UUID.randomUUID().toString();

        logRequest(request, chain.connection(), trackNo);

        long startNs = System.nanoTime();

        try {
            Response response = chain.proceed(request);
            logResponse(response, null, request.url().toString(), trackNo, startNs);
            return response;
        } catch (Exception e) {
            logResponse(null, e, request.url().toString(), trackNo, startNs);
            throw e;
        }
    }


    private String decryptJsonData(String content) {
        try {
            if (content.isEmpty()) return "";
//            JSONObject jsonObject = new JSONObject(content);
//            String dataStr = jsonObject.optString("data");
            String dataStr = content;
            if (!TextUtils.isEmpty(dataStr)) {
                String d = AESNormalUtil.mexicoDecrypt(dataStr, false);
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


    private void logRequest(Request request, Connection connection, String trackNo) {
        List<String> requestLogs = new ArrayList<>();
        requestLogs.add("--> START request " + request.method() + ' ' + request.url() + (connection != null ? " " + connection.protocol() : ""));
        try {
            boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
            boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            if (!logHeaders && hasRequestBody) {
                requestLogs.add("request contentLength (" + requestBody.contentLength() + "-byte body)");
            }

            if (logHeaders) {
                if (hasRequestBody) {
                    // Request body headers are only present when installed as a network interceptor. Force
                    // them to be included (when available) so there values are known.
                    if (requestBody.contentType() != null) {
                        requestLogs.add("Content-Type: " + requestBody.contentType());
                    }
                    if (requestBody.contentLength() != -1) {
                        requestLogs.add("Content-Length: " + requestBody.contentLength());
                    }
                }

                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        String value = headersToRedact.contains(name) ? "██" : headers.value(i);
                        requestLogs.add(name + ": " + value);
                    }
                }

                if (!logBody || !hasRequestBody) {
                    requestLogs.add("--> END request " + request.method() + " " + request.url());
                } else if (bodyHasUnknownEncoding(request.headers())) {
                    requestLogs.add("--> END request" + request.method() + request.method() + " " + request.url() + " (encoded body omitted)");
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    if (isPlaintext(buffer)) {
                        String rawReqData = buffer.readString(charset);
                        requestLogs.add("--> request raw data :" + rawReqData);
                        if (rawReqData.startsWith("data=")) {
                            String value = rawReqData.substring("data=".length());
                            String decryptData = AESNormalUtil.mexicoDecrypt(value, true);
                            requestLogs.add("--> request decrypt data :" + decryptData);
                        }
                    }
                    requestLogs.add("--> END request " + request.method() + " " + request.url());
                }
            }
        } catch (Exception e) {
            requestLogs.add("--> END request failed -> " + request.url() + "\n" + e);
        }
        logger.log(trackNo, requestLogs.toArray(new String[0]), false);

    }


    private void logResponse(@Nullable Response response, @Nullable Exception e, String rawRequestUrl, String trackNo, long startNs){
        List<String> respLogs = new ArrayList<>();
        respLogs.add("--> START response url -> " + rawRequestUrl);
        boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);


        String endSuffix = rawRequestUrl;
        try {
            if (e != null) {
                respLogs.add("--> error \n" + e);
                return;
            }
            if (response == null) {
                return;
            }
            String responseUrl = response.request().url().toString();

            ResponseBody responseBody = response.body();
            long contentLength = -1;
            if(responseBody != null){
                contentLength = responseBody.contentLength();
            }
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            respLogs.add("--> " + response.code() + (response.message().isEmpty() ? "" : ' ' + response.message()) + ' ' + responseUrl + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');


            if (logHeaders) {
                Headers headers = response.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    String value = headersToRedact.contains(name) ? " " : headers.value(i);
                    respLogs.add(name + ": " + value);
                }
                if (bodyHasUnknownEncoding(response.headers())) {
                    endSuffix += " (encoded body omitted)";
                    return;
                }
                if (logBody && HttpHeaders.hasBody(response)) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();


                    if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                        long  gzippedLength = buffer.size();
                        GzipSource gzippedResponseBody = null;
                        try {
                            gzippedResponseBody = new GzipSource(buffer.clone());
                            buffer = new Buffer();
                            buffer.writeAll(gzippedResponseBody);
                        } finally {
                            if (gzippedResponseBody != null) {
                                gzippedResponseBody.close();
                            }
                        }
                        respLogs.add("--> response length (" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte body)");
                    } else {
                        respLogs.add("--> response length (" + buffer.size() + "-byte body)");
                    }

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    if (isPlaintext(buffer)) {
                        String content = buffer.clone().readString(charset);
                        respLogs.add("content: ");
                        respLogs.add(strOrEmpty(content));
                        respLogs.add("----------------------------- decrypt response data -----------------------------");
                        respLogs.add("decrypt content:");
                        respLogs.add(strOrEmpty(decryptJsonData(content)));

                    }else{
                        respLogs.add("-->  response  (binary " + buffer.size() + "-byte body omitted)");
                    }
                }
            }
        } catch (Throwable e3){
            respLogs.add("--> error " + e3);
        }finally {
            respLogs.add("<-- END response " + endSuffix);
            logger.log(trackNo, respLogs.toArray(new String[0]), e != null);
        }
    }

    private String strOrEmpty(String str) {
        if (str == null) return ""; else return str;
    }


//    private void logHeader(Headers headers, int i) {
//        String value = headersToRedact.contains(headers.name(i)) ? "██" : headers.value(i);
//        logger.log(headers.name(i) + ": " + value);
//    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }
}
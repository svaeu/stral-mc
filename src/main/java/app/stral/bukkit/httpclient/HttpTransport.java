package app.stral.bukkit.httpclient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpTransport {

    private static final byte[] EMPTY = new byte[0];

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_MS = 5_000;
    private static final int MAX_RESPONSE_SIZE = 2 * 1024 * 1024; // 2 MiB

    private final String targetURL;

    public HttpTransport(String targetURL) {
        this.targetURL = targetURL;
    }

    public byte[] request(String method, String path, String[] header, byte[] body)
            throws IOException {
        final HttpURLConnection conn;
        final int status, contentLen;

        if ((header.length & 1) != 0)
            throw new IllegalArgumentException("invalid header key/value pairs");

        conn = (HttpURLConnection)
                new URL(targetURL+path).openConnection();

        try {
            conn.setRequestMethod(method);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);

            for(int i = 0; i < header.length; i += 2)
                conn.setRequestProperty(header[i], header[i+1]);

            if (body != null && body.length > 0) {
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(body.length);

                try (OutputStream out = conn.getOutputStream()) { out.write(body); }
            }

            status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_NO_CONTENT) return EMPTY;

            if (status < 200 || status >= 300) throw new TransportException(status);

            try (InputStream in = conn.getInputStream()) {
                if (in == null) return EMPTY;

                contentLen = conn.getContentLength();
                if (contentLen < 0)
                    throw new ProtocolException("no Content-Length on HTTP " +
                            status + " for " + targetURL + path);
                if (contentLen > MAX_RESPONSE_SIZE)
                    throw new ProtocolException("response too large: " +
                            contentLen + " bytes (maximum: " + MAX_RESPONSE_SIZE + ")");
                if (contentLen == 0) return EMPTY;

                return readBody(in, contentLen);
            }
        } catch (IOException e){
            conn.disconnect();
            throw e;
        }
    }

    public Reader utf8Reader(byte[] bytes) {
        return new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8);
    }

    private byte[] readBody(InputStream in, int len) throws IOException {
        final byte[] result;
        int offset, read;

        result = new byte[len];
        offset = 0;
        while (offset < len) {
            read = in.read(result, offset, len - offset);

            if (read == -1)
                throw new EOFException("unexpected EOS while reading: received " +
                        offset + " of " + len + "bytes");
            offset += read;
        }
        return result;
    }
}

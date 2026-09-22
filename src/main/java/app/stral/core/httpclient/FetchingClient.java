package app.stral.core.httpclient;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class FetchingClient {

    private static final Gson GSON = new Gson();
    private static final Type sign = new TypeToken<Map<String,String>>(){}.getType();

    private final HttpTransport transport;
    private final String[] authHeader;

    public FetchingClient(HttpTransport transport, String apiKey) {
        this.transport = transport;
        this.authHeader = new String[]{"X-API-Key", apiKey};
    }

    public String fetchLink() throws Exception {
        String embed = null;

        try (JsonReader reader = new JsonReader(transport.utf8Reader(
                transport.request("POST", "/v1/link", authHeader, null)))) {
            reader.beginObject();

            while (reader.hasNext())
                if(reader.nextName().equals("link"))
                    embed = reader.nextString();
                else reader.skipValue();

            reader.endObject();
            if(embed == null)
                throw new IOException("Missing 'link' field in response");
            return embed;
        }
    }

    public Map<String, String> fetchTerminal() throws IOException {
        final Map<String, String> status;

        try(Reader reader = transport.utf8Reader(
                transport.request("GET", "/v1/status", authHeader, null))) {
            status = GSON.fromJson(reader, sign);
            return status == null ? Collections.emptyMap() : status;
        }
    }


}

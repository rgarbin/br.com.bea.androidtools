package br.com.bea.androidtools.proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.ConnectException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

public class AppProxy implements Proxy<JSONArray> {

    private InputStream stream = null;

    @Override
    public Proxy<JSONArray> connect(final Properties properties) {
        try {
            final HttpClient httpclient = new DefaultHttpClient();
            HttpRequestBase request = null;
            if (properties.getProperty("method").equalsIgnoreCase("POST"))
                request = new HttpPost(properties.getProperty("url_connection"));
            else request = new HttpGet(properties.getProperty("url_connection"));
            final HttpResponse response = httpclient.execute(request);
            final HttpEntity entity = response.getEntity();
            stream = entity.getContent();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public List<JSONArray> request(final JSONArray vo) throws ConnectException {
        if (null == stream) throw new ConnectException("Conexão não realizada!");
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
            final StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            stream.close();
            return Arrays.asList(new JSONArray(sb.toString()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

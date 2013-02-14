package br.com.bea.androidtools.api.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.json.JSONArray;

public class AppProxy implements Proxy<JSONArray> {

    private HttpURLConnection connection;
    private InputStream input;
    private OutputStream output;

    @Override
    public void close() {
        try {
            if (null != input) input.close();
            if (null != output) output.close();
            if (null != connection) connection.disconnect();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Proxy<JSONArray> connect(final Properties properties) {
        try {
            connection = (HttpURLConnection) new URL(properties.getProperty("url_connection")).openConnection();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public List<JSONArray> request(final byte[] data) throws ConnectException {
        if (null == connection) throw new ConnectException("Conexão não realizada!");
        try {
            if (null != data) {
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(data.length);
                output = connection.getOutputStream();
                output.write(data);
                output.flush();
            }
            input = connection.getInputStream();
            final StringBuilder sb = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"), 8);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            return Arrays.asList(new JSONArray(sb.toString()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

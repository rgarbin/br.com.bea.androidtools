/*
The MIT License (MIT)
Copyright (c) 2013 B&A Tecnologia and Collaborators

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions 
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
 */

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

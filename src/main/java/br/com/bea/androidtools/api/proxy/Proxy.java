package br.com.bea.androidtools.api.proxy;

import java.net.ConnectException;
import java.util.List;
import java.util.Properties;

public interface Proxy<E> {
    void close();

    Proxy<E> connect(final Properties properties);

    List<E> request(final byte[] data) throws ConnectException;
}

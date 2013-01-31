package br.com.bea.androidtools.proxy;

import java.rmi.ConnectException;
import java.util.List;
import java.util.Properties;

public interface Proxy<E> {
    Proxy<E> connect(final Properties properties);

    List<E> request(final E vo) throws ConnectException;
}

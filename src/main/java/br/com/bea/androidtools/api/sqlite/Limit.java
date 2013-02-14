package br.com.bea.androidtools.api.sqlite;

public class Limit {

    private Long numeroPagina;

    private Long tamanhoPagina;

    public Long getNumeroPagina() {
        return numeroPagina;
    }

    public Long getTamanhoPagina() {
        return tamanhoPagina;
    }

    public void setNumeroPagina(final Long numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public void setTamanhoPagina(final Long tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
    }
}

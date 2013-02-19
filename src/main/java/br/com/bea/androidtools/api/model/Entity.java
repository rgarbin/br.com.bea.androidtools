package br.com.bea.androidtools.api.model;

public abstract class Entity<Id> extends ValueObject {

    private static final long serialVersionUID = 1L;

    public abstract Id getId();

    public abstract void setId(final Id id);
}

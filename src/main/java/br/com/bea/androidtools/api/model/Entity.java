package br.com.bea.androidtools.api.model;

public abstract class Entity<Id> extends ValueObject {

    public abstract Id getId();

    public abstract void setId(final Id id);
}

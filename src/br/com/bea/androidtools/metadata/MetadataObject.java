package br.com.bea.androidtools.metadata;

public final class MetadataObject {
    private final String fieldName;
    private final String value;

    public MetadataObject(final String fieldName, final String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

}

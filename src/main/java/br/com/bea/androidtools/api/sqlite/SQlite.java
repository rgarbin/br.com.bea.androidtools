package br.com.bea.androidtools.api.sqlite;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.bea.androidtools.api.annotations.Column;
import br.com.bea.androidtools.api.annotations.Id;
import br.com.bea.androidtools.api.annotations.Table;
import br.com.bea.androidtools.api.model.Entity;
import br.com.bea.androidtools.api.model.EntityUtils;

class SQlite<E extends Entity<?>> extends SQLiteOpenHelper {

    private final List<Class<E>> targetClasses;

    public SQlite(final Context context, final String database, final List<Class<E>> targetClasses) {
        super(context, database, null, 1);
        this.targetClasses = targetClasses;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        for (final Class<E> targetClass : targetClasses) {
            final StringBuilder builder = new StringBuilder();
            builder.append(" CREATE TABLE ").append(targetClass.getAnnotation(Table.class).name()).append("( ");
            for (final Iterator<Field> iterator = EntityUtils.columnFields(targetClass).iterator(); iterator.hasNext();) {
                final Field field = iterator.next();
                builder.append(field.getAnnotation(Column.class).name()).append("  ")
                    .append(field.getAnnotation(Column.class).type()).append("  ")
                    .append(field.isAnnotationPresent(Id.class) ? " PRIMARY KEY " : "");
                if (iterator.hasNext()) builder.append(", ");
            }
            builder.append(");");
            System.out.println(builder.toString());
            db.execSQL(builder.toString());
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int arg1, final int arg2) {
    }
}

package ui.model;

import java.util.Objects;

import java.util.function.Function;

public class Column<E, T> {
    private final String columnName;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    public Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        this.columnName = Objects.requireNonNull(columnName, "columnName");
        this.columnClass = Objects.requireNonNull(columnClass, "columnClass");
        this.valueGetter = Objects.requireNonNull(valueGetter, "valueGetter");
    }

    Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    String getColumnName() {
        return columnName;
    }

    Class<T> getColumnClass() {
        return columnClass;
    }
}

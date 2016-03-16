package com.youtube.vitess.client.cursor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;

import com.youtube.vitess.proto.Query.Field;
import org.apache.commons.collections.map.CaseInsensitiveMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A wrapper for {@code List<Field>} that also facilitates lookup by field name.
 *
 * <p>The field name maps to an index, rather than a Field, because that same
 * index is also used to find the value in a separate list.
 */
public class FieldMap {
  private final List<Field> fields;
  private final Map<String, Integer> indexMap;

  public FieldMap(Iterable<Field> fields) {
    this.fields = ImmutableList.copyOf(checkNotNull(fields));

    indexMap = new CaseInsensitiveMap();
    // columnIndex is 1-based.
    int columnIndex = 1;
    for (Field field : this.fields) {
      String colName = field.getName();
      if(null == indexMap.get(colName)) {
        indexMap.put(colName, columnIndex++);
      }
    }
  }

  public List<Field> getList() {
    return fields;
  }

  public Field get(int columnIndex) {
    // columnIndex is 1-based.
    checkArgument(columnIndex >= 1, "columnIndex out of range: %s", columnIndex);
    return fields.get(columnIndex - 1);
  }

  @Nullable
  public Integer getIndex(String columnLabel) {
    return indexMap.get(columnLabel);
  }
}

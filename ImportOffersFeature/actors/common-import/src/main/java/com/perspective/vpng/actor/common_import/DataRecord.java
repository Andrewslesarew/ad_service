package com.perspective.vpng.actor.common_import;


import com.perspective.vpng.actor.common_import.exception.DataRecordReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecord;

import java.util.Arrays;

public class DataRecord implements IDataRecord {

    private Object[] items;

    public DataRecord(Object[] items) {
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        this.items = items;
    }

    @Override
    public Object getItem(int index) throws DataRecordReadingException {
        try {
            return items[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataRecordReadingException("Record item index out of range: " + index);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(items);
    }
}

package com.perspective.vpng.actor.common_import;

public class DataReaderUtils {

    public static final String DEFAULT_CHARSET = "cp1251";

    public static Object[] trimItems(Object[] record) {
        for (int i = 0; i < record.length; i++) {
            if (record[i] instanceof String) {
                record[i] = ((String) record[i]).trim();
            }
        }
        return record;
    }

    public static Object[] prepareRecord(Object[] record) {
        if (record != null) {
            record = trimItems(record);
        }
        return record;
    }
}

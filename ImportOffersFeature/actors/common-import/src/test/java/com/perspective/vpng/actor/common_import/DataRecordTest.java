package com.perspective.vpng.actor.common_import;

import com.perspective.vpng.actor.common_import.exception.DataRecordReadingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DataRecordTest {
    private static final Object[] ORIG_RAW_RECORD = new Object[]{"10001", "Маяковского ул., 123, кв. 40", 1005.50};
    private static final int ORIG_RAW_RECORD_LENGTH = ORIG_RAW_RECORD.length;

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ArgIsNull_ExceptionThrown() throws Exception {
        new DataRecord(null);
    }

    @Test
    public void getItem_GoodRawRecord_ReturnsRightItems() throws Exception {
        DataRecord dataRecord = createDataRecord();

        for (int i = 0; i < ORIG_RAW_RECORD_LENGTH; i++) {
            assertEquals(ORIG_RAW_RECORD[i], dataRecord.getItem(i));
        }
    }

    @Test(expected = DataRecordReadingException.class)
    public void getItem_WrongItemIndexBelowBounds_ExceptionThrown() throws Exception {
        DataRecord dataRecord = createDataRecord();
        dataRecord.getItem(-1);
    }

    @Test(expected = DataRecordReadingException.class)
    public void getItem_WrongItemIndexAboveBounds_ExceptionThrown() throws Exception {
        DataRecord dataRecord = createDataRecord();
        dataRecord.getItem(ORIG_RAW_RECORD_LENGTH);
    }

    private DataRecord createDataRecord() {
        return new DataRecord(ORIG_RAW_RECORD);
    }
}
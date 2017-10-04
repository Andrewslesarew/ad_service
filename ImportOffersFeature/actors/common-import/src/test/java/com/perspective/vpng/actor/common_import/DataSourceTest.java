package com.perspective.vpng.actor.common_import;

import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecord;
import info.smart_tools.smartactors.ioc.ikey.IKey;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IOC.class, Keys.class})
public class DataSourceTest {

    @Test(expected = DataSourceReadingException.class)
    public void next_DataReaderFailsToReadARecord_ExceptionThrown() throws Exception {
        IDataReader mockReader = mock(IDataReader.class);
        when(mockReader.next()).thenThrow(new DataSourceReadingException());

        DataSource dataSource = new DataSource(mockReader, mock(DataRecordProcessor.class));

        dataSource.next();
    }

    @Test
    public void next_NextRecordIsNotAvailableInDataReader_ReturnsFalse() throws Exception {
        IDataReader mockReader = mock(IDataReader.class);
        when(mockReader.next()).thenReturn(null);

        DataSource dataSource = new DataSource(mockReader, mock(DataRecordProcessor.class));

        assertFalse(dataSource.next());
    }


    @Test
    public void next_recordAndEmptyRecord_emptyRecordIsNotProcessed() throws Exception {
        mockStatic(IOC.class);
        mockStatic(Keys.class);
        IKey keyForDataRecord = mock(IKey.class);
        Object[] record = new Object[]{};

        when(Keys.getOrAdd(IDataRecord.class.getCanonicalName())).thenReturn(keyForDataRecord);
        when(IOC.resolve(keyForDataRecord, record)).thenReturn(new DataRecord(record));
        IDataReader mockReader = mock(IDataReader.class);
        DataRecordProcessor mockProcessor = mock(DataRecordProcessor.class);
        when(mockReader.next())
                .thenReturn(record)
                .thenReturn(null);

        DataSource dataSource = new DataSource(mockReader, mockProcessor);

        dataSource.next();
        dataSource.next();

        verify(mockReader, times(2)).next();
        verify(mockProcessor, times(1)).process(anyObject());
    }
}
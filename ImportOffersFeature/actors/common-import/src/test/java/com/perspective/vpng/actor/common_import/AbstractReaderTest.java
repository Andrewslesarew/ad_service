package com.perspective.vpng.actor.common_import;

import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecord;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecordProcessor;
import com.perspective.vpng.actor.common_import.interfaces.IDataSource;
import com.perspective.vpng.actor.common_import.wrapper.ReaderMessage;
import info.smart_tools.smartactors.iobject.ds_object.DSObject;
import info.smart_tools.smartactors.iobject.ifield.IField;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.ioc.ikey.IKey;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IOC.class, Keys.class})
public class AbstractReaderTest {

    private static final Object[] DATA_RECORD = new Object[]{"Иван", "123", "45", "Омск"};
    private ReaderMessage message;
    private static IObject mapper;
    private static final Integer LIMIT = 100;

    @Before
    public void setUpData() throws Exception {
        mapper = new DSObject("{ \"any0\": 0, \"any1\": 1, \"any2\": 2, \"any3\": 3 }");
        message = mock(ReaderMessage.class);
        when(message.getCounter()).thenReturn(Optional.empty());
        when(message.getLimit()).thenReturn(LIMIT);
        when(message.getFieldMapper()).thenReturn(mapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void readRecords_MessageContainsLimit_MessageAddedLimitDocuments() throws Exception {

        IDataReader dataReader = mock(IDataReader.class);
        when(dataReader.next()).thenReturn(DATA_RECORD);
        PowerMockito.when(message.getReader()).thenReturn(dataReader);

        mockStatic(IOC.class);
        mockStatic(Keys.class);

        IKey key1 = mock(IKey.class);
        IKey keyDataSource = mock(IKey.class);
        IKey keyIObject = mock(IKey.class);
        IKey keyField = mock(IKey.class);
        IKey keyDataRecord = mock(IKey.class);

        IObject mockObject = mock(IObject.class);
        when(Keys.getOrAdd(IObject.class.getCanonicalName())).thenReturn(keyIObject);
        when(IOC.resolve(eq(keyIObject))).thenReturn(mockObject);

        IField anyField = mock(IField.class);
        when(Keys.getOrAdd(IField.class.getCanonicalName())).thenReturn(keyField);
        when(IOC.resolve(eq(keyField), any(String.class))).thenReturn(anyField);

        when(Keys.getOrAdd(IDataRecordProcessor.class.getCanonicalName())).thenReturn(key1);
        IDataRecordProcessor processor = new DataRecordProcessor(mapper);
        when(IOC.resolve(eq(key1), eq(mapper))).thenReturn(processor);

        when(Keys.getOrAdd(IDataSource.class.getCanonicalName())).thenReturn(keyDataSource);
        when(IOC.resolve(eq(keyDataSource), eq(dataReader), eq(processor))).thenReturn(new DataSource(dataReader, processor));

        when(Keys.getOrAdd(IDataRecord.class.getCanonicalName())).thenReturn(keyDataRecord);
        Mockito.when(IOC.resolve(keyDataRecord, DATA_RECORD)).thenReturn(new DataRecord(DATA_RECORD));

        assertTrue(AbstractReaderActor.readRecords(message).size() == LIMIT);
    }
}
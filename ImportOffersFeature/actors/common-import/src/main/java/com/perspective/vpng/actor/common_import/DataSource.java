package com.perspective.vpng.actor.common_import;

import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecord;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecordProcessor;
import com.perspective.vpng.actor.common_import.interfaces.IDataSource;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

public class DataSource implements IDataSource {

    private IDataReader reader;
    private IDataRecordProcessor processor;

    public DataSource(IDataReader reader, IDataRecordProcessor processor) {
        this.reader = reader;
        this.processor = processor;
    }

    @Override
    public boolean next() throws DataSourceReadingException {
        try {
            Object[] rawRecord = reader.next();
            if (rawRecord == null) {
                return false;
            }
            IDataRecord record = IOC.resolve(Keys.getOrAdd(IDataRecord.class.getCanonicalName()), rawRecord);
            processor.process(record);
        } catch (Exception e) {
            throw new DataSourceReadingException(e);
        }
        return true;
    }
}

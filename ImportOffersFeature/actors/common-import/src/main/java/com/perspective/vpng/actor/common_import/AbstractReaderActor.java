package com.perspective.vpng.actor.common_import;

import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecordProcessor;
import com.perspective.vpng.actor.common_import.interfaces.IDataSource;
import com.perspective.vpng.actor.common_import.interfaces.IObjectInjector;
import com.perspective.vpng.actor.common_import.wrapper.ReaderMessage;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.SerializeException;
import info.smart_tools.smartactors.ioc.iioccontainer.exception.ResolutionException;
import info.smart_tools.smartactors.ioc.ikey.IKey;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

import java.util.ArrayList;
import java.util.List;


public class AbstractReaderActor {

    public static List<IObject> readRecords(ReaderMessage message) throws DataSourceReadingException, ResolutionException, ReadValueException, ChangeValueException, SerializeException {
        ReadingParameters readingParameters = new ReadingParameters(message);
        List<IObject> records = new ArrayList<>();
        IDataRecordProcessor processor = createProcessor(readingParameters);
        IKey dataSourceKey = Keys.getOrAdd(IDataSource.class.getCanonicalName());
        IDataSource dataSource = IOC.resolve(dataSourceKey, readingParameters.reader, processor);
        message.setRepeatAgain(true);
        for (int i = 0; i < readingParameters.limit; i++) {
            if (dataSource.next()) {
                records.add(((IObjectInjector) processor).getIObject());
                IObject iObject = IOC.resolve(Keys.getOrAdd(IObject.class.getCanonicalName()));
                ((IObjectInjector) processor).setIObject(iObject);
            } else {
                message.setRepeatAgain(false);
                break;
            }
        }
        return records;
    }

    private static DataRecordProcessor createProcessor(ReadingParameters readingParameters) throws ResolutionException {
        DataRecordProcessor processor = IOC.resolve(Keys.getOrAdd(IDataRecordProcessor.class.getCanonicalName()),
                readingParameters.map);
        IObject iObject = IOC.resolve(Keys.getOrAdd(IObject.class.getCanonicalName()));
        ((IObjectInjector) processor).setIObject(iObject);
        return processor;
    }

    protected static class ReadingParameters {
        public IDataReader reader;
        public Integer limit;
        public IObject map;

        public ReadingParameters(ReaderMessage message) throws ReadValueException, ChangeValueException, SerializeException {
            reader = message.getReader();
            limit = message.getLimit();
            map = message.getFieldMapper();
        }
    }
}

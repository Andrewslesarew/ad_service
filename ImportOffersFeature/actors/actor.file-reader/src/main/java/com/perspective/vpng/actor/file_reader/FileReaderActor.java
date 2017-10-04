package com.perspective.vpng.actor.file_reader;

import com.perspective.vpng.actor.common_import.AbstractReaderActor;
import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.common_import.wrapper.ReaderMessage;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.SerializeException;
import info.smart_tools.smartactors.ioc.iioccontainer.exception.ResolutionException;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

import java.util.List;
import java.util.Optional;


public class FileReaderActor {

    public FileReaderActor(IObject params) {
    }

    /**
     * Sets reader to the message
     *
     * @param message dbf reader message
     */
    public void connect(FileReaderMessage message) throws ReadValueException, ChangeValueException, ResolutionException {
        setDataReader(message);
    }

    private void setDataReader(FileReaderMessage message) throws ReadValueException, ChangeValueException, ResolutionException {
        if (message.getReader() == null) {
            IDataReader reader;
            try {
                reader = IOC.resolve(Keys.getOrAdd(FileReaderActor.class.getCanonicalName() + message.getCollectionName()), message);
            } catch (Exception e) {
                try {
                    reader = IOC.resolve(Keys.getOrAdd(FileReaderActor.class.getCanonicalName() +
                            Optional.ofNullable(message.getReaderKey()).orElseThrow(NullPointerException::new)), message);
                } catch (Exception e1) {
                    throw new ReadValueException("Can't get readerKey for provider: " + message.getCollectionName(), e1);
                }
            }
            message.setReader(reader);
        }
    }

    public void read(ReaderMessage message) throws ChangeValueException, ReadValueException, ResolutionException, SerializeException, DataSourceReadingException {
        List<IObject> records = AbstractReaderActor.readRecords(message);
        message.setDocuments(records);
        message.setRepeatAgain(records.size() == message.getLimit());
    }
}

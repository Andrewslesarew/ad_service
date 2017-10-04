package com.perspective.vpng.actor.file_reader.wrapper;

import com.perspective.vpng.actor.common_import.wrapper.ReaderMessage;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;


public interface FileReaderMessage extends ReaderMessage {
        String getCharset() throws ReadValueException;
        String getFileName() throws ReadValueException;
        String getSeparator() throws ReadValueException;
        String getCollectionName() throws ReadValueException;
        IObject getFieldMapper() throws ReadValueException;
        Integer getBeginIndex () throws ReadValueException;
        Integer getLastIndex() throws ReadValueException;
        String getReaderKey() throws ReadValueException;
}

package com.perspective.vpng.actor.common_import.wrapper;

import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;

import java.util.List;
import java.util.Optional;

public interface ReaderMessage {

    IDataReader getReader() throws ReadValueException, ChangeValueException;
    Optional<Integer> getCounter() throws ReadValueException, ChangeValueException;
    Integer getLimit() throws ReadValueException, ChangeValueException;
    IObject getFieldMapper() throws ReadValueException, ChangeValueException;

    void setReader(IDataReader val) throws ReadValueException, ChangeValueException;
    void setCounter(Integer val) throws ReadValueException, ChangeValueException;
    void setRepeatAgain(Boolean val) throws ReadValueException, ChangeValueException;
    void setDocuments(List<IObject> val) throws ReadValueException, ChangeValueException;
}

package com.perspective.vpng.actor.common_import;


import com.perspective.vpng.actor.common_import.exception.DataRecordReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecord;
import com.perspective.vpng.actor.common_import.interfaces.IDataRecordProcessor;
import com.perspective.vpng.actor.common_import.interfaces.IObjectInjector;
import info.smart_tools.smartactors.base.exception.invalid_argument_exception.InvalidArgumentException;
import info.smart_tools.smartactors.iobject.ifield.IField;
import info.smart_tools.smartactors.iobject.ifield_name.IFieldName;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.ioc.iioccontainer.exception.ResolutionException;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

import java.util.Map;

public class DataRecordProcessor implements IDataRecordProcessor, IObjectInjector {

    private IObject mapper;
    private IObject iObject;

    public DataRecordProcessor(IObject mapper) {
        this.mapper = mapper;
    }

    @Override
    public void process(IDataRecord record) {
        translateDataRecordToIObject(record);
    }

    @Override
    public void setIObject(IObject iObject) {
        this.iObject = iObject;
    }

    @Override
    public IObject getIObject() {
        return iObject;
    }

    private void translateDataRecordToIObject(IDataRecord record) {
        mapper.forEach(o -> {
            try {
                addIObjectValue(record, o);
            } catch (Exception e) {
                throw new RuntimeException("Fail to translate DataRecord to IObject", e);
            }
        });
    }

    private void addIObjectValue(IDataRecord record, Map.Entry<IFieldName, Object> mapperEntry)
            throws ResolutionException, DataRecordReadingException, ChangeValueException, InvalidArgumentException {
        IField field = IOC.resolve(Keys.getOrAdd(IField.class.getCanonicalName()), mapperEntry.getKey().toString());
        field.out(iObject, record.getItem((Integer) mapperEntry.getValue()));
    }
}

package com.perspective.vpng.actor.file_reader.data_reader;

import com.perspective.vpng.actor.common_import.DataReaderUtils;
import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.file_reader.exception.FileReaderException;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import com.perspective.vpng.facade.dbf_reader.DbfFileReader;
import com.perspective.vpng.facade.dbf_reader.exception.DbfReaderFacadeException;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DbfDataReader implements IDataReader {

    private DbfFileReader reader;
    private FileReaderMessage params;

    public DbfDataReader(FileReaderMessage params) throws ReadValueException, ChangeValueException, FileNotFoundException, FileReaderException {
        this.params = params;
        InputStream in = new FileInputStream(new File(getPathParam()));
        try {
            this.reader = new DbfFileReader(in);
        } catch (DbfReaderFacadeException e) {
            throw new FileReaderException("Fail create dbf reader", e);
        }
        setCharset(reader);
    }

    @Override
    public Object[] next() throws DataSourceReadingException {
        try {
            return DataReaderUtils.prepareRecord(reader.nextRecord());
        } catch (DbfReaderFacadeException e) {
            throw new DataSourceReadingException("Failed to read the next DBF record", e);
        }
    }

    private void setCharset(DbfFileReader reader) throws ReadValueException, ChangeValueException {
        String charset = getCharsetParam();
        reader.setCharactersetName(charset);
    }

    private String getCharsetParam() throws ReadValueException, ChangeValueException {
        return params.getCharset();
    }

    private String getPathParam() throws ReadValueException, ChangeValueException {
        return params.getFileName();
    }
}

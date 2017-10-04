package com.perspective.vpng.actor.file_reader.data_reader;

import com.perspective.vpng.actor.common_import.DataReaderUtils;
import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import com.perspective.vpng.facade.csv_reader.CsvFileReader;
import com.perspective.vpng.facade.csv_reader.exception.CsvFileReaderException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class CsvDataReader implements IDataReader {

    private CsvFileReader reader;
    private FileReaderMessage params;
    private static final char DEFAULT_SEPARATOR = ',';
    private static final String COMMENT_CHAR = "#";
    private static final String CONTROL_LINE_CHAR = "=";
    private static final char DEFAULT_CSV_QUOTE_CHARACTER = '@';

    public CsvDataReader(FileReaderMessage params) throws ReadValueException, FileNotFoundException, UnsupportedEncodingException {
        this.params = params;
        String charset = Optional.ofNullable(params.getCharset()).orElse(DataReaderUtils.DEFAULT_CHARSET);
        InputStreamReader in = new InputStreamReader(new FileInputStream(getPathParam()), charset);
        this.reader = new CsvFileReader(in, getSeparatorParam(), DEFAULT_CSV_QUOTE_CHARACTER);
    }

    @Override
    public Object[] next() throws DataSourceReadingException {
        try {
            String[] strings;
            while ((strings = reader.readNext()) != null) {
                if (strings.length == 0 || (strings.length == 1 && StringUtils.isBlank(strings[0])) || strings[0].startsWith(COMMENT_CHAR) || strings[0].startsWith(CONTROL_LINE_CHAR)) {
                    continue;
                }
                return DataReaderUtils.prepareRecord(strings);
            }
            return DataReaderUtils.prepareRecord(null);
        } catch (CsvFileReaderException e) {
            throw new DataSourceReadingException("Failed to read the next CSV record", e);
        }
    }

    private char getSeparatorParam() throws ReadValueException {
        char result = DEFAULT_SEPARATOR;
        try {
            if (params.getSeparator() != null) {
                result = params.getSeparator().charAt(0);
            }
        } catch (Exception e) {
            result = DEFAULT_SEPARATOR;
        }
        return result;
    }

    private String getPathParam() throws ReadValueException {
        return params.getFileName();
    }
}

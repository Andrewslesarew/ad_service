package com.kalad.makina.facade.csv_writer;

import au.com.bytecode.opencsv.CSVWriter;
import com.kalad.makina.facade.csv_writer.exception.CsvFileWriterException;

import java.io.*;
import java.util.List;

public class CsvFileWriter {
    private CSVWriter csvWriter;
    private static final String HEADER_MARKER = "#";

    public CsvFileWriter(Writer writer, char separator, char qoutechar, char escapechar) {

        this.csvWriter = new CSVWriter(writer, separator, qoutechar, escapechar);
    }

    public CsvFileWriter(ByteArrayOutputStream byteArray, String charset, char separator) throws CsvFileWriterException {
        try {
            csvWriter = new CSVWriter(new OutputStreamWriter(byteArray, charset),
                    separator, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
        } catch (UnsupportedEncodingException e) {
            throw new CsvFileWriterException("Fail to create CSVWriter", e);
        }
    }

    public void writeNext(String[] strings) {
        csvWriter.writeNext(strings);
    }

    public void writeHeaders(List<String> headers) {
        for (String header : headers) {
            csvWriter.writeNext(new String[]{HEADER_MARKER + header});
        }
    }

    public void close() throws CsvFileWriterException {
        try {
            csvWriter.close();
        } catch (IOException e) {
            throw new CsvFileWriterException("Fail to close csvWriter", e);
        }

    }
}

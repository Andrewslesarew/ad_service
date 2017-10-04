package com.kalad.makina.facade.csv_reader;

import au.com.bytecode.opencsv.CSVReader;
import com.kalad.makina.facade.csv_reader.exception.CsvFileReaderException;

import java.io.IOException;
import java.io.Reader;

public class CsvFileReader {
    private CSVReader csvReader;

    public CsvFileReader(Reader reader, char separator) {
        csvReader = new CSVReader(reader, separator);
    }
    public CsvFileReader(Reader reader, char separator, char quoteCharacter) {
        csvReader = new CSVReader(reader, separator, quoteCharacter);
    }

    public String[] readNext() throws CsvFileReaderException {
        try {
            return csvReader.readNext();
        } catch (IOException e) {
            throw new CsvFileReaderException("Fail to read next", e);
        }

    }
}

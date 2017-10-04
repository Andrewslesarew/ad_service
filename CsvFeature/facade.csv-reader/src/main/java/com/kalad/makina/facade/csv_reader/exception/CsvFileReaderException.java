package com.kalad.makina.facade.csv_reader.exception;

import java.io.IOException;

public class CsvFileReaderException extends Exception {
    public CsvFileReaderException(String s, Throwable e) {
        super(s, e);
    }
}

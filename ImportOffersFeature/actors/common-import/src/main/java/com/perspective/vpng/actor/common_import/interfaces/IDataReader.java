package com.perspective.vpng.actor.common_import.interfaces;


import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;

public interface IDataReader {
    Object[] next() throws DataSourceReadingException;
}

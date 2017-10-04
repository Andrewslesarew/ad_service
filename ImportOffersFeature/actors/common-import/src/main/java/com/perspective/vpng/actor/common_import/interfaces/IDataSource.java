package com.perspective.vpng.actor.common_import.interfaces;

import com.perspective.vpng.actor.common_import.exception.DataSourceReadingException;

@FunctionalInterface
public interface IDataSource {
    boolean next() throws DataSourceReadingException;
}

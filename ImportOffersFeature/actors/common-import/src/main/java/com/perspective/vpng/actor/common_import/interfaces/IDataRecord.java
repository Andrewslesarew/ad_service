package com.perspective.vpng.actor.common_import.interfaces;


import com.perspective.vpng.actor.common_import.exception.DataRecordReadingException;

@FunctionalInterface
public interface IDataRecord {
    Object getItem(int index) throws DataRecordReadingException;
}

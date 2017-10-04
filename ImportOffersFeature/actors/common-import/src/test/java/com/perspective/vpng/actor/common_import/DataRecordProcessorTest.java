package com.perspective.vpng.actor.common_import;

import info.smart_tools.smartactors.iobject.ds_object.DSObject;
import info.smart_tools.smartactors.iobject.ifield.IField;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.ioc.ikey.IKey;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IOC.class, Keys.class})
public class DataRecordProcessorTest {

    @Test
    public void constructor_GoodRulesList_ProcessorCreated() throws Exception {
        IObject mapper = new DSObject("{ \"test\": 0 }");
        DataRecordProcessor processor = new DataRecordProcessor(mapper);
        assertNotNull(processor);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void process_GoodRecordAndMapper_ReturnsTranslatedIObject() throws Exception {
        mockStatic(IOC.class);
        IKey keyField = mock(IKey.class);
        when(Keys.getOrAdd(IField.class.getCanonicalName())).thenReturn(keyField);

        IField accountField = mock(IField.class);
        IField addressField = mock(IField.class);
        IField rangeField = mock(IField.class);
        IField amountField = mock(IField.class);

        when(IOC.resolve(eq(keyField), eq("ACCOUNT"))).thenReturn(accountField);
        when(IOC.resolve(eq(keyField), eq("ADDRESS"))).thenReturn(addressField);
        when(IOC.resolve(eq(keyField), eq("RANGE"))).thenReturn(rangeField);
        when(IOC.resolve(eq(keyField), eq("AMOUNT"))).thenReturn(amountField);

        /*                              0        1                           2         3       */
        /*                              ЛС       адрес                       период    сумма   */
        Object[] record = new Object[]{"10001", "Маяковского 1 д.1 кв 101", "0915", 1005.50};
        DataRecord dataRecord = new DataRecord(record);

        IObject mapper = new DSObject("{ \"ACCOUNT\": 0, \"ADDRESS\": 1, \"RANGE\": 2, \"AMOUNT\": 3 }");

        DataRecordProcessor processor = new DataRecordProcessor(mapper);

        IObject iObject = mock(IObject.class);
        processor.setIObject(iObject);
        processor.process(dataRecord);

        verify(accountField).out(eq(iObject), eq("10001"));
        verify(addressField).out(eq(iObject), eq("Маяковского 1 д.1 кв 101"));
        verify(rangeField).out(eq(iObject), eq("0915"));
        verify(amountField).out(eq(iObject), eq(1005.50));
    }
}

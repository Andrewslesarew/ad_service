package dbf_reader;

import com.google.common.base.Joiner;
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.file_reader.data_reader.DbfDataReader;
import com.perspective.vpng.actor.file_reader.exception.FileReaderException;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class DbfDataReaderTest {

    private static final String BAD_PATH_FILE = "this/path/does/not/exist.dbf";
    private static final String BAD_FORMAT_FILE = "dbf_reader/bad_format.dbf";
    private static final String GOOD_EMPTY_FILE = "dbf_reader/good_empty.dbf";
    private static final String GOOD_FILLED_FILE = "dbf_reader/good_filled.dbf";
    private static final String GOOD_FILLED_EXPECTED_FILE = "dbf_reader/good_filled.expected.txt";

    private static final int GOOD_FILLED_FILE_RECORDS_COUNT = 15;

    private static final String DEFAULT_CHARSET = "cp1251";
    private static final String BAD_CHARSET = "cp0000";

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void constructor_BadPathFile_ExceptionThrown() throws Exception {
        thrown.expect(FileNotFoundException.class);

        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(BAD_PATH_FILE);

        new DbfDataReader(params);
    }

    @Test
    public void constructor_BadFormatFile_ExceptionThrown() throws Exception {
        thrown.expect(FileReaderException.class);

        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(BAD_FORMAT_FILE));

        new DbfDataReader(params);
    }

    @Test
    public void next_GoodEmptyFile_CanReadNoRecords() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_EMPTY_FILE));
        when(params.getCharset()).thenReturn(DEFAULT_CHARSET);

        IDataReader reader = new DbfDataReader(params);
        assertNull(reader.next());
    }


    @Test
    public void next_GoodFilledFile_CanReadAsManyTimesAsRecordsAre() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn(DEFAULT_CHARSET);

        IDataReader reader = new DbfDataReader(params);

        for (int i = 0; i < GOOD_FILLED_FILE_RECORDS_COUNT; i++) {
            assertNotNull(reader.next());
        }
        assertNull(reader.next());
    }


    @Ignore
    @Test
    public void next_GoodFilledFile_CanReadAllRecordsCorrectly() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn("cp1251");

        IDataReader reader = new DbfDataReader(params);

        InputStream expectedFile = getClass().getClassLoader().getResourceAsStream(GOOD_FILLED_EXPECTED_FILE);
        Scanner scanner = new Scanner(expectedFile, "UTF-8");

        for (int i = 0; i < GOOD_FILLED_FILE_RECORDS_COUNT; i++) {
            assertEquals(scanner.nextLine(), Joiner.on(",").join(Arrays.asList(reader.next())));
        }

        assertNull(reader.next());
    }


    @Test(expected = java.nio.charset.UnsupportedCharsetException.class)
    public void next_BadCharset_ExceptionThrown() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn(BAD_CHARSET);

        IDataReader reader = new DbfDataReader(params);
        reader.next();
    }

    private String expandRelativeFilePathToResource(String relativeFilePath) {
        return DbfDataReaderTest.class.getClassLoader().getResource(relativeFilePath).getPath();
    }
}

package csv_reader;

import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.file_reader.data_reader.CsvDataReader;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class CsvDataReaderTest {

    private static final String BAD_PATH_FILE = "this/path/does/not/exist.csv";
    private static final String EMPTY_FILE = "csv_reader/empty.csv";
    private static final String GOOD_FILLED_FILE = "csv_reader/good_filled.csv";

    private static final int GOOD_FILLED_FILE_RECORDS_COUNT = 3;
    private static final String BAD_CHARSET = "cp0000";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_BadPathFile_ExceptionThrown() throws Exception {
        thrown.expect(FileNotFoundException.class);

        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(BAD_PATH_FILE);
        when(params.getCharset()).thenReturn("");

        new CsvDataReader(params);
    }

    @Test
    public void next_BadCharset_ExceptionThrown() throws Exception {
        thrown.expect(UnsupportedEncodingException.class);

        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn(BAD_CHARSET);

        new CsvDataReader(params);
    }

    @Test
    public void next_GoodEmptyFile_CanReadNoRecords() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(EMPTY_FILE));
        when(params.getSeparator()).thenReturn(";");
        when(params.getCharset()).thenReturn(null);

        IDataReader reader = new CsvDataReader(params);

        assertNull(reader.next());
    }

    @Test
    public void next_GoodFilledFile_CanReadAsManyTimesAsRecordsAre() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn(null);
        when(params.getSeparator()).thenReturn(";");

        IDataReader reader = new CsvDataReader(params);

        for (int i = 0; i < GOOD_FILLED_FILE_RECORDS_COUNT; i++) {
            assertNotNull(reader.next());
        }

        assertNull(reader.next());
    }

    @Test
    public void next_GoodFilledFile_CanReadAllRecordsCorrectly() throws Exception {
        FileReaderMessage params = mock(FileReaderMessage.class);
        when(params.getFileName()).thenReturn(expandRelativeFilePathToResource(GOOD_FILLED_FILE));
        when(params.getCharset()).thenReturn("UTF-8");
        when(params.getSeparator()).thenReturn(";");

        IDataReader reader = new CsvDataReader(params);

        Object[] expectedLines = new Object[]{
                new String[]{"Иван", "123", "45", "Омск", "2", "Улица", "0"},
                new String[]{"Петр", "456", "46", "Сочи", "3", "Улица", "0"},
                new String[]{"Иван", "789", "47", "Омск", "4", "Улица", "0"}};

        for (int i = 0; i < GOOD_FILLED_FILE_RECORDS_COUNT; i++) {
            assertTrue(Arrays.equals((Object[]) expectedLines[i], reader.next()));
        }

        assertNull(reader.next());
    }

    private String expandRelativeFilePathToResource(String relativeFilePath) {
        return CsvDataReaderTest.class.getClassLoader().getResource(relativeFilePath).getPath();
    }
}
import com.perspective.vpng.actor.common_import.interfaces.IDataReader;
import com.perspective.vpng.actor.file_reader.FileReaderActor;
import com.perspective.vpng.actor.file_reader.wrapper.FileReaderMessage;
import info.smart_tools.smartactors.iobject.ds_object.DSObject;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({IOC.class, Keys.class})
public class FileReaderTest {


    private FileReaderActor fileReaderActor;
    private static FileReaderMessage message;
    private static IObject mapper;


    private IDataReader dataReader;

    @BeforeClass
    public static void before() throws Exception {
        mapper = new DSObject("{\"testItem\": 0}");
    }

    @Before
    public void setUpData() throws Exception {
        message = mock(FileReaderMessage.class);
        FileReaderMessage config = mock(FileReaderMessage.class);
        when(config.getCharset()).thenReturn("UTF-8");
        //when(config.getFileName()).thenReturn(com.perspective.vpng.actor.dbf_reader.DbfDataReaderTest.class.getClassLoader().getResource("dbf_reader/good_filled.dbf").getPath());
        when(message.getCounter()).thenReturn(Optional.empty());
        when(message.getLimit()).thenReturn(100);
        when(message.getSeparator()).thenReturn(",");
        when(message.getFieldMapper()).thenReturn(mapper);

        fileReaderActor = new FileReaderActor(null);

        mockStatic(IOC.class);
        mockStatic(Keys.class);
        dataReader = mock(IDataReader.class);

        when(IOC.resolve(Keys.getOrAdd(FileReaderActor.class.getCanonicalName()), message)).thenReturn(dataReader);
        fileReaderActor = new FileReaderActor(null);
    }


    @Test
    public void connect_MessageContainsConnection_MessageAddedCounterAndReader1() throws Exception {
        fileReaderActor.connect(message);
        verify(message).setReader(dataReader);
    }

}

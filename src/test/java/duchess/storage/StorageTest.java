package duchess.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import duchess.exceptions.DuchessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StorageTest {
    final String emptyTestFilePath = "testEmptyData.json";
    final String nonEmptyTestFilePath = "testNonEmptyData.json";
    private final String unreadableFileMessage
            = "Unable to read file, continuing with empty list.";

    @Test
    public void load_emptyFile_exceptionThrown() {
        try {
            Storage storage = new Storage(emptyTestFilePath);
            assertEquals(storage.load(), new Store());

        } catch (DuchessException | ClassCastException e) {
            assertEquals(e.getMessage(), unreadableFileMessage);
        }
    }

    @Test
    public void load_nonEmptyFile_noExceptionThrown() {
        try {
            Storage storage = new Storage(nonEmptyTestFilePath);
            Store store = storage.load();


            assertNotEquals(store, null);
            assertNotEquals(store, new Store());
        } catch (DuchessException | ClassCastException e) {
            assertEquals(e.getMessage(), unreadableFileMessage);
        }
    }

    public String getStoreToString(Store store) {
        String jsonVal;
        try {
            jsonVal = getObjectMapper().writeValueAsString(store);
        } catch (JsonProcessingException e) {
            jsonVal = new String();
            assert (jsonVal.equals(""));
        }
        return jsonVal;
    }
}

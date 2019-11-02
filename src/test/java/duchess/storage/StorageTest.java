package duchess.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import duchess.exceptions.DuchessException;
import org.junit.jupiter.api.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorageTest {
    final String emptyTestFilePath = "testEmptyData.json";
    final String nonEmptyTestFilePath = "testNonEmptyData.json";
    private final String unreadableFileMessage
            = "Unable to read file, continuing with empty list.";
    private final String emptyRedoStackErrorMessage
            = "Redo stack is empty.";
    private final String emptyUndoStackErrorMessage
            = "Undo stack is empty.";

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

    @Test
    public void getFirstSnapshot_emptyFile_exceptionThrown() {
        Storage storage = new Storage(emptyTestFilePath);
        try {
            Store store = storage.getFirstSnapshot();
            assertEquals(store, null);
        } catch (DuchessException e) {
            assertEquals(e.getMessage(), emptyRedoStackErrorMessage);
        }
    }

    @Test
    public void getLastSnapshot_emptyFile_exceptionThrown() {
        Storage storage = new Storage(emptyTestFilePath);
        try {
            Store store = storage.getLastSnapshot();
            assertEquals(store, null);
        } catch (DuchessException e) {
            System.out.println("EMPTy");
            assertEquals(e.getMessage(), emptyUndoStackErrorMessage);
        }
    }

    @Test
    public void getUndoStack_success() {
        Storage storageA = new Storage(emptyTestFilePath);
        Storage storageB = new Storage(nonEmptyTestFilePath);

        Deque<String> testUndoStackA = storageA.getUndoStack();
        Deque<String> testUndoStackB = storageA.getUndoStack();

        assertTrue(testUndoStackA.size() == 0);
        assertTrue(testUndoStackA != null);

        assertTrue(testUndoStackB.size() == 0);
        assertTrue(testUndoStackB != null);
    }


    // WILL BE EMPTY FOR NOW
    @Test
    public void getFirstSnapshot_emptyFile_noExceptionThrown() {
        Storage storage = new Storage(nonEmptyTestFilePath);
        try {
            Store store = storage.getFirstSnapshot();
            assertEquals(store, null);
            System.out.println("SUCESS");
        } catch (DuchessException e) {
            System.out.println("Problem");
            assertEquals(e.getMessage(), emptyRedoStackErrorMessage);
        }
    }
}

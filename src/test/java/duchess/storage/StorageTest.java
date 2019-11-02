package duchess.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import duchess.exceptions.DuchessException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private static final String stringToStoreErrorMessage
            = "Unable to convert String to Store.";

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
            assertEquals(e.getMessage(), emptyUndoStackErrorMessage);
        }
    }

    @Test
    public void getUndoStack_success() {
        Storage storageA = new Storage(emptyTestFilePath);
        Storage storageB = new Storage(nonEmptyTestFilePath);

        Deque<String> testUndoStackA = storageA.getUndoStack();
        Deque<String> testUndoStackB = storageB.getUndoStack();

        assertTrue(testUndoStackA.size() == 0);
        assertTrue(testUndoStackA != null);

        assertTrue(testUndoStackB.size() == 0);
        assertTrue(testUndoStackB != null);
    }

    @Test
    public void getRedoStack_success() {
        Storage storageA = new Storage(emptyTestFilePath);
        Storage storageB = new Storage(nonEmptyTestFilePath);

        Deque<String> testRedoStackA = storageA.getRedoStack();
        Deque<String> testRedoStackB = storageB.getRedoStack();

        assertTrue(testRedoStackA.size() == 0);
        assertTrue(testRedoStackA != null);

        assertTrue(testRedoStackB.size() == 0);
        assertTrue(testRedoStackB != null);
    }

    @Test
    public void peekUndoStackAsStore_emptyUndoStack_emptyStoreObject() {
        Storage storage = new Storage(emptyTestFilePath);
        try {
            assertTrue(storage.getUndoStack().size() == 0);
            Store store = storage.peekUndoStackAsStore();
            assertNotEquals(store, null);
        } catch (DuchessException e) {
            assertEquals(e.getMessage(),stringToStoreErrorMessage);
        }
    }

    @Test
    public void peekUndoStackAsStore_nonEmptyUndoStack_nonEmptyStoreObject() {
        Storage storage = new Storage(emptyTestFilePath);
        Store storeA = new Store();
        storage.addToUndoStackPush(storeA);

        try {
            assertTrue(storage.getUndoStack().size() == 1);
            Store store = storage.peekUndoStackAsStore();
            assertNotEquals(store, null);
        } catch (DuchessException e) {
            assertEquals(e.getMessage(),stringToStoreErrorMessage);
        }
    }

    @Test
    public void addToRedoStack_success() {
        Storage storage = new Storage(emptyTestFilePath);
        Store store = new Store();
        storage.addToUndoStackPush(store);

        assertTrue(storage.getUndoStack().size() == 1);
        assertTrue(storage.getRedoStack().size() == 0);

        storage.addToRedoStack();
        assertTrue(storage.getUndoStack().size() == 1);
        assertTrue(storage.getRedoStack().size() == 1);
    }

    @Test
    public void addToUndoStackPush_validStore_success() {
        try {
            Storage storage = new Storage(nonEmptyTestFilePath);
            Store store = storage.load();

            assertTrue(storage.getUndoStack().size() == 0);
            storage.addToUndoStackPush(store);

            assertTrue(storage.getUndoStack().size() == 1);

        } catch (DuchessException | ClassCastException e) {
            assertEquals(e.getMessage(), unreadableFileMessage);
        }
    }

    @Test
    public void addToUndoStackPush_consecutiveRepeatedStore_unsuccessful() {
        try {
            Storage storage = new Storage(nonEmptyTestFilePath);
            Store storeA = new Store();
            Store storeB = new Store();

            assertTrue(storage.getUndoStack().size() == 0);
            assertTrue(storeA != null);
            storage.addToUndoStackPush(storeA);

            assertTrue(storage.getUndoStack().size() == 1);
            assertTrue(storeB != null);
            storage.addToUndoStackPush(storeB);

            assertTrue(storage.getUndoStack().size() == 1);

        } catch (ClassCastException e) {
            assertEquals(e.getMessage(), unreadableFileMessage);
        }
    }
// WILL BE EMPTY FOR NOW
    //@Test
    //public void getFirstSnapshot_emptyFile_noExceptionThrown() {
    //    Storage storage = new Storage(nonEmptyTestFilePath);
    //    try {
    //        Store store = storage.getFirstSnapshot();
    //        assertEquals(store, null);
    //        System.out.println("SUCESS");
    //    } catch (DuchessException e) {
    //        System.out.println("Problem");
    //        assertEquals(e.getMessage(), emptyRedoStackErrorMessage);
    //    }
    //}
}

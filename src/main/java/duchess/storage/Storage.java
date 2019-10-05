package duchess.storage;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import duchess.exceptions.DuchessException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

public class Storage {
    private String fileName;
    private Stack<String> undoStack = new Stack<>();

    public Storage(String fileName) {
        this.fileName = fileName;
    }

    // Unchecked type coercion is necessary here.
    // And possible cast exceptions are handled

    /**
     * Returns the tasklist loaded from file.
     */
    @SuppressWarnings("unchecked")
    public Store load() throws DuchessException {
        try {
            FileInputStream fileStream = new FileInputStream(this.fileName);
            Store store = getObjectMapper().readValue(fileStream, Store.class);
            fileStream.close();
            return store;
        } catch (IOException | ClassCastException e) {
            throw new DuchessException("Unable to read file, continuing with empty list.");
        }
    }

    /**
     * Saves the given tasklist to file.
     *
     * @param store the store to save
     * @throws DuchessException an error if unable to write to file
     */
    public void save(Store store) throws DuchessException {
        try {
            FileOutputStream fileStream = new FileOutputStream(this.fileName);
            getObjectMapper().writeValue(fileStream, store);
            fileStream.close();
        } catch (IOException e) {
            throw new DuchessException("An unexpected error occurred when writing to the file. " + e);
        }
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper()
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
                .disable(MapperFeature.AUTO_DETECT_CREATORS,
                        MapperFeature.AUTO_DETECT_FIELDS,
                        MapperFeature.AUTO_DETECT_GETTERS,
                        MapperFeature.AUTO_DETECT_IS_GETTERS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Stack<String> getUndoStack() {
        return undoStack;
    }

    public void setUndoStack(Stack<String> undoStack) {
        this.undoStack = undoStack;
    }

    public void stackLoad() {
        if (this.undoStack.size() > 0);
            String prevStoreJSON = getUndoStack().peek();
            getUndoStack().pop();
            ObjectMapper mapper = new ObjectMapper();

            try {
                // Deserializing
                Store newStore = mapper.readValue(prevStoreJSON, Store.class);
                store.setTaskList(newStore.getTaskList());
                store.setModuleList(newStore.getModuleList());
            } catch(JsonParseException e) {
                throw new DuchessException("JSON parsing issue");
            } catch(IOException e) {
                throw new DuchessException("Stack input error");
            }
        }

    }
}

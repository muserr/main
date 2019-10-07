package duchess.storage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import duchess.exceptions.DuchessException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Storage {
    private String fileName;
    private Stack<String> undoStack;
    boolean isPrevUndo;

    public Storage(String fileName) {
        this.fileName = fileName;
        undoStack = new Stack<>();
        isPrevUndo = false;
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
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Store getLastSnapshot() throws DuchessException {
        if (undoStack.size() == 0) {
            throw new DuchessException("There's nothing to undo");
        }

        String jsonVal = undoStack.peek();
        undoStack.pop();

        try {
            Store store = getObjectMapper().readValue(jsonVal, Store.class);
            return store;
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new DuchessException("JSON parse was unsuccessful.");
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new DuchessException("Mapping was unsuccessful.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DuchessException("Check storage input issues.");
        }
    }

    // Saving Store as JSON.
    public void addToUndoStackPush(Store store, String userInput) throws DuchessException {
        List<String> words = Arrays.asList(userInput.split(" "));
        String keyword = words.get(0);

        try {
            String jsonVal = getObjectMapper().writeValueAsString(store);
            undoStack.push(jsonVal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new DuchessException("Undo stack push was unsuccessful.");
        }

        // if(!keyword.equalsIgnoreCase("undo")) {
        //     isPrevUndo = false;
        // } else {
        //     isPrevUndo = true;
        // }
    }

    public Stack<String> getUndoStack() {
        return this.undoStack;
    }
}

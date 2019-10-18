package duchess.parser.states;

import duchess.exceptions.DuchessException;
import duchess.logic.commands.Command;

import java.util.Map;

public interface ParserState {
    public abstract Command parse(String input) throws DuchessException;

    public abstract Command continueParsing(Map<String, String> parameters) throws DuchessException;
}
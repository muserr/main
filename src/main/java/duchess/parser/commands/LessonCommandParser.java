package duchess.parser.commands;

import duchess.exceptions.DuchessException;
import duchess.logic.commands.Command;
import duchess.logic.commands.DeleteModuleCommand;
import duchess.logic.commands.DeleteTaskCommand;
import duchess.parser.Parser;
import duchess.parser.Util;

import java.time.LocalDateTime;
import java.util.Map;

public class LessonCommandParser {
    public static Command parse(Map<String, String> parameters) throws DuchessException {
        try {
            String moduleCode = parameters.get("add");
            String type = parameters.get("type");
            LocalDateTime startTimeString = Util.parseDateTime(parameters.get("time"));
            LocalDateTime endTimeString = Util.parseDateTime(parameters.get("to"));

            if (type.equals(Parser.TASK_KEYWORD)) {
                return new DeleteTaskCommand(number);
            } else if (type.equals(Parser.MODULE_KEYWORD)) {
                return new DeleteModuleCommand(number);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DuchessException(Parser.DELETE_USAGE);
        }
    }
}

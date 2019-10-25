package duchess.parser.commands;

import duchess.exceptions.DuchessException;
import duchess.logic.commands.AddLessonCommand;
import duchess.logic.commands.Command;
import duchess.logic.commands.DeleteModuleCommand;
import duchess.logic.commands.DeleteTaskCommand;
import duchess.parser.Parser;
import duchess.parser.Util;

import java.time.LocalDateTime;
import java.util.Map;

public class LessonCommandParser {
    public static Command parse(Map<String, String> parameters) throws DuchessException {
        String description = parameters.get("type");
        String moduleCode = parameters.get("add");
        LocalDateTime startTimeString = Util.parseDateTime(parameters.get("time"));
        LocalDateTime endTimeString = Util.parseDateTime(parameters.get("to"));

        return new AddLessonCommand(description, startTimeString, endTimeString, moduleCode);
    }
}

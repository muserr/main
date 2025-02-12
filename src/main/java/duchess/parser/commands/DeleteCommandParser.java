package duchess.parser.commands;

import duchess.exceptions.DuchessException;
import duchess.logic.commands.Command;
import duchess.logic.commands.DeleteLessonCommand;
import duchess.logic.commands.DeleteGradeCommand;
import duchess.logic.commands.DeleteModuleCommand;
import duchess.logic.commands.DeleteTaskCommand;
import duchess.parser.Parser;

import java.util.Map;

/**
 * Parses commands related to deletion.
 */
public class DeleteCommandParser {
    /**
     * Returns a command to delete a module based on user input.
     *
     * @param parameters processed user input
     * @return the command to execute
     * @throws DuchessException if the user input is invalid
     */
    public static Command parse(Map<String, String> parameters) throws DuchessException {
        try {
            String type = parameters.get("general");

            if (type.equals(Parser.TASK_KEYWORD)) {
                int number = Integer.parseInt(parameters.get("no"));
                return new DeleteTaskCommand(number);
            } else if (type.equals(Parser.MODULE_KEYWORD)) {
                int number = Integer.parseInt(parameters.get("no"));
                return new DeleteModuleCommand(number);
            } else if (type.equals(Parser.LESSON_KEYWORD)) {
                String lessonType = parameters.get("type");
                String moduleCode = parameters.get("code");
                return new DeleteLessonCommand(lessonType, moduleCode);
            } else if (type.equals(Parser.GRADE_KEYWORD)) {
                int number = Integer.parseInt(parameters.get("no"));
                String moduleCode = parameters.get(Parser.MODULE_KEYWORD);
                if (moduleCode == null) {
                    throw new IllegalArgumentException();
                }
                return new DeleteGradeCommand(moduleCode, number);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
            throw new DuchessException(Parser.DELETE_USAGE);
        }
    }
}

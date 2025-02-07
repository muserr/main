package duchess.parser.commands;

import duchess.exceptions.DuchessException;
import duchess.logic.commands.Command;
import duchess.logic.commands.ListGradesCommand;
import duchess.logic.commands.ListModulesCommand;
import duchess.logic.commands.ListTasksCommand;
import duchess.parser.Parser;

import java.util.Map;
import java.util.Optional;

/**
 * Parses command to list user's resources.
 */
public class ListCommandParser {
    /**
     * Returns a command to list user's resources.
     *
     * @param parameters the processed user input
     * @return the command to execute
     * @throws DuchessException if the input is invalid
     */
    public static Command parse(Map<String, String> parameters) throws DuchessException {
        return Optional
                .ofNullable(parameters.get("general"))
                .map(keyword -> {
                    if (keyword.equalsIgnoreCase(Parser.TASKS_KEYWORD)) {
                        return new ListTasksCommand();
                    } else if (keyword.equalsIgnoreCase(Parser.MODULES_KEYWORD)) {
                        return new ListModulesCommand();
                    } else if (keyword.equalsIgnoreCase(Parser.GRADES_KEYWORD)) {
                        String moduleCode = parameters.get(Parser.MODULE_KEYWORD);
                        if (moduleCode == null) {
                            return null;
                        }
                        return new ListGradesCommand(moduleCode);
                    } else {
                        return null;
                    }
                }).orElseThrow(() -> new DuchessException(Parser.LIST_USAGE));
    }
}

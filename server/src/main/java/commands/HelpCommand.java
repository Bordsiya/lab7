package commands;

import data.User;
import exceptions.WrongAmountOfElements;
import utils.CommandManager;
import utils.ResponseBuilder;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Command to display help for available commands
 * @author NastyaBordun
 * @version 1.1
 */

public class HelpCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;

    private CommandManager commandManager;
    /**
     * Constructor for the command
     * @param commandBase base for commands
     */
    public HelpCommand(CommandBase commandBase, CommandManager commandManager){
        super("help", "вывести справку по доступным командам");
        this.commandBase = commandBase;
        this.commandManager = commandManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#help()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            commandBase.help();
            if(str.length() != 0 || arg != null){
                throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
            }
            String info = "";
            for(Map.Entry<String, ICommand> command : commandManager.getCommands().entrySet()){
                info += command.getKey() + " - " + command.getValue().getDescription() + "\n";
            }
            ResponseBuilder.append(info);
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}

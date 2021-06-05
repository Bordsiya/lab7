package commands;

import data.User;
import exceptions.NullLastSaveException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;

import java.time.format.DateTimeFormatter;

/**
 * Printing command for collection information
 * @author NastyaBordun
 * @version 1.1
 */

public class InfoCommand extends AbstractCommand{
    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;
    /**
     * Manager for collection {@link CollectionManager}
     */
    private CollectionManager collectionManager;

    /**
     * Constructor for the command
     * @param commandBase base for commands
     * @param collectionManager collection manager
     */
    public InfoCommand(CommandBase commandBase, CollectionManager collectionManager){
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#info()
     * @see CollectionManager#collectionSize()
     * @see CollectionManager#getLastInit()
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            commandBase.info();
            if(str.length() != 0 || arg != null){
                throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
            }

            ResponseBuilder.appendln("Размер коллекции: " + collectionManager.collectionSize());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ResponseBuilder.appendln("Дата инициализации: " + collectionManager.getLastInit().format(formatter));
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }

    }

}

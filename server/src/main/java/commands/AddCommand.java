package commands;

import data.SpaceMarine;
import data.SpaceMarineRaw;
import data.User;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;

/**
 * The command for adding a new element to the collection
 * @author NastyaBordun
 * @version 1.1
 */

public class AddCommand extends AbstractCommand {

    /**
     * Base for all commands {@link CommandBase}
     */
    private CommandBase commandBase;

    /**
     * Manager for collection {@link CollectionManager}
     */
    private CollectionManager collectionManager;

    private DatabaseCollectionManager databaseCollectionManager;

    /**
     * Constructor for the command
     * @param commandBase base for commands
     * @param collectionManager collection manager
     */
    public AddCommand(CommandBase commandBase, CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("add", "добавить новый элемент в коллекцию");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#add()
     * @see CollectionManager#addElement(SpaceMarine)
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            commandBase.add();
            if(str.length() != 0 || arg == null){
                throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
            }

            SpaceMarineRaw newSpaceMarineRaw = (SpaceMarineRaw)arg;
            collectionManager.addElement(databaseCollectionManager.insertSpaceShip(newSpaceMarineRaw, user));

            ResponseBuilder.appendln("Элемент добавлен в коллекцию");
            return true;
        }
        catch (WrongAmountOfElements e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (ClassCastException e){
            ResponseBuilder.appendError("Переданный клиентом объект некорректен");
            return false;
        } catch (SQLException e) {
            ResponseBuilder.appendError("Ошибка при работе с БД");
            return false;
        }
    }

}

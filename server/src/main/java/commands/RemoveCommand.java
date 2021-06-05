package commands;

import data.SpaceMarine;
import data.User;
import exceptions.ObjectDoesNotExistException;
import exceptions.PermissionDeniedException;
import exceptions.WrongAmountOfElements;
import utils.CollectionManager;
import utils.ResponseBuilder;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;

/**
 * The Removal Command for collection elements with type {@link SpaceMarine} by ID
 * @author NastyaBordun
 * @version 1.1
 */

public class RemoveCommand extends AbstractCommand{
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
    public RemoveCommand(CommandBase commandBase, CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_by_id", "удалить элемент из коллекции по его id");
        this.commandBase = commandBase;
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Command execution
     * @param str command argument
     * @return command result
     * @see CommandBase#remove()
     * @see CollectionManager#removeElementById(Integer)
     */
    @Override
    public boolean execute(String str, Object arg, User user) {
        try{
            commandBase.remove();
            String [] commandArr = str.trim().split(" ");
            if(str.length() == 0 || commandArr.length != 1 || arg!=null){
                throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
            }

            Integer id = Integer.parseInt(commandArr[0]);
            SpaceMarine removeSpaceShip = collectionManager.searchById(id);
            if(removeSpaceShip == null) throw new ObjectDoesNotExistException("Объекта не существует");
            if(!removeSpaceShip.getCreator().equals(user)) throw new PermissionDeniedException("Ошибка доступа к объектам, недостаточные права");
            databaseCollectionManager.deleteSpaceShipById(id);
            collectionManager.removeElementById(id);
            ResponseBuilder.appendln("Объект удален");
            return true;
        }
        catch (NumberFormatException e){
            ResponseBuilder.appendError("Некорректный ID");
            return false;
        }
        catch (ObjectDoesNotExistException | WrongAmountOfElements | PermissionDeniedException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        } catch (SQLException e) {
            ResponseBuilder.appendError("Ошибка при работе с БД");
            return false;
        }
    }

}

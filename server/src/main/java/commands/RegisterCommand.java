package commands;

import data.User;
import exceptions.UserIsAlreadyRegisteredException;
import exceptions.WrongAmountOfElements;
import utils.ResponseBuilder;
import utils.database.DatabaseUserManager;
import java.sql.SQLException;

/**
 * Register command
 * @author NastyaBordun
 * @version 1.1
 */

public class RegisterCommand extends AbstractCommand{

    private CommandBase commandBase;

    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(CommandBase commandBase, DatabaseUserManager databaseUserManager){
        super("register", "регистрация");
        this.commandBase = commandBase;
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String str, Object arg, User user) {
        try {
            commandBase.register();
            if(str.length() != 0 || arg != null) throw new WrongAmountOfElements("Неправильное количество аргументов для команды");
            if(databaseUserManager.insertUserToDatabase(user)) ResponseBuilder.appendln("Пользователь " + user.getUsername() + " зарегистрирован");
            else throw new UserIsAlreadyRegisteredException("Пользователь " + user.getUsername() + " уже зарегистрирован");
            return true;
        }
        catch (WrongAmountOfElements | UserIsAlreadyRegisteredException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (SQLException e){
            ResponseBuilder.appendError("Ошибка при работе с БД");
            return false;
        }
    }

}

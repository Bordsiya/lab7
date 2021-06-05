package commands;

import data.User;
import exceptions.UserNotFoundException;
import exceptions.WrongAmountOfElements;
import utils.ResponseBuilder;
import utils.database.DatabaseUserManager;
import java.sql.SQLException;

/**
 * Login command
 * @author NastyaBordun
 * @version 1.1
 */

public class LoginCommand extends AbstractCommand{

    private CommandBase commandBase;

    private DatabaseUserManager databaseUserManager;

    public LoginCommand(CommandBase commandBase, DatabaseUserManager databaseUserManager){
        super("login", "вход");
        this.commandBase = commandBase;
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public boolean execute(String str, Object arg, User user) {
        try {
            commandBase.login();
            if (str.length() != 0 || arg != null)
                throw new WrongAmountOfElements("Неправильное количество аргументов для операции");
            if(databaseUserManager.checkUserByUsernameAndPassword(user)){
                ResponseBuilder.appendln("Пользователь " + user.getUsername() + " авторизован");
            }
            else throw new UserNotFoundException("Пользователя с таким логином и паролем нет в базе");
            return true;
        }
        catch (WrongAmountOfElements | UserNotFoundException e){
            ResponseBuilder.appendError(e.getMessage());
            return false;
        }
        catch (SQLException e){
            ResponseBuilder.appendError("Ошибка при работе с БД");
            return false;
        }
    }

}

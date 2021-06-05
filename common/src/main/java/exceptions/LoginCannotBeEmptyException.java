package exceptions;

public class LoginCannotBeEmptyException extends Exception{
    public LoginCannotBeEmptyException(String message){
        super(message);
    }
}

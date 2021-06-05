package utils;

import exceptions.LoginCannotBeEmptyException;
import exceptions.PasswordCannotBeEmptyException;

import java.io.BufferedReader;
import java.io.IOException;

public class AskAuthorizationManager {

    private BufferedReader bf;

    public AskAuthorizationManager(BufferedReader bufferedReader){
        this.bf = bufferedReader;
    }

    public String askLogin(){
        String login;
        while(true){
            try{
                Printer.println("Введите логин: ");
                login = bf.readLine().trim().replaceAll("\uFFFD", "");
                if(login.isEmpty()) throw new LoginCannotBeEmptyException("Логин не может быть пустым");
                break;
            }
            catch (IOException e){
                Printer.printError("Ошибка ввода");
            }
            catch (LoginCannotBeEmptyException e){
                Printer.printError(e.getMessage());
            }
        }
        return login;
    }

    public String askPassword(){
        String password;
        while(true){
            try{
                Printer.println("Введите пароль: ");
                password = bf.readLine().trim().replaceAll("\uFFFD", "");
                if(password.isEmpty()) throw new PasswordCannotBeEmptyException("Логин не может быть пустым");
                break;
            }
            catch (IOException e){
                Printer.printError("Ошибка ввода");
            }
            catch (PasswordCannotBeEmptyException e){
                Printer.printError(e.getMessage());
            }
        }
        return password;
    }

    public boolean askQuestion(String question){
        String answer;
        while(true){
            try{
                Printer.println(question);
                Printer.println("1 - да; 2 - нет");
                answer = bf.readLine().trim().replaceAll("\uFFFD", "");
                if(answer.equals("1")) return true;
                if(answer.equals("2")) return false;
            }
            catch (IOException e){
                Printer.printError("Ошибка ввода");
            }
        }
    }

}

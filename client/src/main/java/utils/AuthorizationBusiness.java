package utils;

import answers.Request;
import data.User;

import java.io.BufferedReader;

public class AuthorizationBusiness {

    private final String loginCommand = "login";
    private final String registerCommand = "register";

    private BufferedReader bf;

    private AskAuthorizationManager askAuthorizationManager;

    public AuthorizationBusiness(BufferedReader bf){
        this.bf = bf;
        AskAuthorizationManager askAuthorizationManager = new AskAuthorizationManager(bf);
        this.askAuthorizationManager = askAuthorizationManager;
    }

    public Request makeRequest(){
        String command = askAuthorizationManager.askQuestion("У вас уже есть учетная запись?") ? "login" : "register";
        User user = new User(askAuthorizationManager.askLogin(), askAuthorizationManager.askPassword());
        return new Request(command, user);
    }

}

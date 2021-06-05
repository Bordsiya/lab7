package utils;

import answers.Request;
import answers.Response;
import answers.ResponseAnswer;
import data.User;
import exceptions.IncorrectCommandException;

/**
 * Client
 * @author NastyaBordun
 * @version 1.1
 */
public class Client {
    /**
     * Class for receiving responses
     */
    private Receiver receiver;
    /**
     * Class for sending requests
     */
    private Sender sender;
    /**
     * Class for making requests
     */
    private Business business;

    private AuthorizationBusiness authorizationBusiness;

    private User user;

    /**
     * Constructor for class
     * @param receiver {@link Receiver}
     * @param sender {@link Sender}
     * @param business {@link Business}
     */
    public Client(Receiver receiver, Sender sender, Business business, AuthorizationBusiness authorizationBusiness){
        this.receiver = receiver;
        this.sender = sender;
        this.business = business;
        this.authorizationBusiness = authorizationBusiness;
    }

    /**
     * Interaction with Server
     * @param command command from Console
     */
    public void handle(String command){
        try {
            Request request = business.makeRequest(command, this.user);
            if(request != null){
                if(sender.send(request, ClientLauncher.PORT)){
                    Response response = receiver.getResponse();
                    if(response != null){
                        Printer.print(response.getResponseBody());
                    }
                }
            }

        } catch (IncorrectCommandException e) {
            Printer.printError(e.getMessage());
        }
    }

    public void login(){
        while(true){
            Request request = authorizationBusiness.makeRequest();
            if(request != null){
                if(sender.send(request, ClientLauncher.PORT)){
                    Response response = receiver.getResponse();
                    if(response != null && response.getResponseAnswer().equals(ResponseAnswer.OK)){
                        Printer.println("Вы авторизованы. Можете приступать к работе.");
                        this.user = request.getUser();
                        break;
                    }
                    else{
                        Printer.println("Вы не авторизованы. Попробуйте еще.");
                    }
                }
            }
        }
    }


}

package utils;

import answers.Request;
import answers.Response;
import answers.ResponseAnswer;
import data.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for analyzing request and creating response
 * @author NastyaBordun
 * @version 1.1
 */
public class MessageAnalyzator implements Runnable{
    private CommandManager commandManager;
    private Request request;
    private Sender sender;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    public MessageAnalyzator(Request request, CommandManager commandManager, Sender sender){
        this.request = request;
        this.commandManager = commandManager;
        this.sender = sender;
    }

    /**
     * Creating response
     * @return response
     */
    public void run(){
        User user = new User(request.getUser().getUsername(), PasswordHasher.hashPassword(request.getUser().getPassword()));
        ResponseAnswer responseAnswer = analyze(request.getCommandName(), request.getCommandStringArg(), request.getCommandObjectArg(), user);
        Response response = new Response(responseAnswer, ResponseBuilder.getStringBuilder());
        ResponseBuilder.clear();
        fixedThreadPool.submit( () ->{
            sender.sendResponse(response);
        });
    }

    private synchronized ResponseAnswer analyze(String commandName, String commandStringArg, Object commandObjectArg, User user){
        if(commandManager.commandExist(commandName)){
            if(!commandManager.executeCommand(commandName, commandStringArg, commandObjectArg, user)) return ResponseAnswer.ERROR;
            else return ResponseAnswer.OK;
        }
        else{
            ResponseBuilder.appendError("Команда " + commandName + " не входит в список доступных");
            return ResponseAnswer.ERROR;
        }

    }
}

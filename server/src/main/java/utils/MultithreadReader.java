package utils;

import answers.Request;
import java.nio.channels.DatagramChannel;
/**
 * The reader thread
 * @author NastyaBordun
 * @version 1.1
 */

public class MultithreadReader implements Runnable{

    private ConParam conParam;
    private DatagramChannel chan;
    private Receiver receiver;
    private CommandManager commandManager;

    public MultithreadReader(ConParam conParam, DatagramChannel chan, Receiver receiver, CommandManager commandManager){
        this.conParam = conParam;
        this.receiver = receiver;
        this.commandManager = commandManager;
        this.chan = chan;
    }

    @Override
    public void run() {
        Request request = receiver.getRequest(conParam);
        Sender sender = new Sender(conParam.sa, chan);
        Thread handleThr = new Thread(new MessageAnalyzator(request, commandManager, sender));
        handleThr.start();
    }
}

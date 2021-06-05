package utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Class for launching Client
 * @author NastyaBordun
 * @version 1.1
 */
public class ClientLauncher {
    /**
     * Port for connection
     */
    public static final int PORT = 12890;

    /**
     * Launching Client
     */
    public void launchClient(){
        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader r = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        AskManager askManager = new AskManager(r);
        AuthorizationBusiness authorizationBusiness = new AuthorizationBusiness(r);
        Business business = new Business(askManager);
        FileManager fileManager = new FileManager();
        try {
            InetAddress ADDR = InetAddress.getByName("localhost");
            DatagramSocket datagramSocket = new DatagramSocket();
            Receiver receiver = new Receiver(datagramSocket);
            Sender sender = new Sender(datagramSocket, ADDR);
            Client client = new Client(receiver, sender, business, authorizationBusiness);
            Console console = new Console(r, fileManager, client, askManager);
            business.addConsole(console);

            Printer.println("Можете вводить данные");
            client.login();
            console.interactiveMode();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            Printer.println("Ошибка во время создания клиента");
        }

    }

}

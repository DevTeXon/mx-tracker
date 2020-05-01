package system;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebSocket extends JavaPlugin implements CommandExecutor {

    private ServerSocket serverSocket;
    private int port = 26583;
    private List<String> keys = new ArrayList<>();
    private static WebSocket webSocket;
    private List<ClientThread> clientThreads;

    @Override
    public void onEnable() {
        webSocket = this;
        try {
            getCommand("register").setExecutor(this);
            serverSocket = new ServerSocket(port);
            clientThreads.add(new ClientThread(serverSocket));
            clientThreads.get(0).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while loading system.WebSocket");
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid = UUID.randomUUID();
        String newKey = uuid.toString().replace("-", "");
        this.keys.add(newKey);
        sender.sendMessage("Key: " + newKey + " was added.");
        return true;
    }

    public List<String> getKeys() {
        return keys;
    }

    public static WebSocket getWebSocket() {
        return webSocket;
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    @Override
    public void onDisable() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


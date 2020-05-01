package system;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientThread extends Thread {

    private boolean active;
    private ServerSocket serverSocket;

    public ClientThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;;
        this.active = false;
        super.start();
        this.active = true;
        WebSocket.getWebSocket().getClientThreads().remove(0);

    }

    private void loop() {
        while (active) {
            try {
                new Client(this.serverSocket.accept()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void interrupt() {
        this.active = false;
        super.interrupt();
    }

    @Override
    public void run() {
        super.run();
        this.active = true;
        this.loop();
    }

    public class Client extends Thread {
        private Socket socket;

        public Client(Socket socket) {
            this.socket = socket;
        }

        private void write() {
            try (PrintWriter writer = new PrintWriter(this.socket.getOutputStream())) {
                if(WebSocket.getWebSocket().getKeys().isEmpty()) {
                    writer.println("Nothing Found");
                }else{
                    writer.println(Arrays.toString(WebSocket.getWebSocket().getKeys().toArray()).replace("[", "").replace("]", "").replace(" ", ""));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            this.write();
        }
    }
}

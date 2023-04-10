import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    // private static final String SERVER_ADDRESS = "mcsmuscle-32634.portmap.io";
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1111;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    protected PointStep point;

    public Client() {
        start();
    }

    private void start() {
        try {
            System.out.println("Connecting to server...");
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Listen any changes from server
            Thread thread = new Thread(new ServerListener());
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    point = (PointStep) inputStream.readObject();
                    System.out.println("Received data from server");
                    applyNewUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void applyNewUpdate() {
    }

    public void sendNewUpdate(PointStep point) {
        try {
            outputStream.writeObject(point);
            outputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

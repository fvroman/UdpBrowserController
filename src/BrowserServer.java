import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.Date;

public class BrowserServer extends Thread{
    protected DatagramSocket socket = null;
    protected boolean running = true;

    public BrowserServer() throws IOException {
        this("BrowserServer");
    }

    public BrowserServer(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
    }

    public void run() {

        while (running) {
            System.out.println("Running..");
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String request = new String(packet.getData(), 0, packet.getLength());

                System.out.println(request);
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(request));
                }
                buf = "OK".getBytes();

                InetAddress address = packet.getAddress();
                System.out.println(packet.getAddress());
                int port = packet.getPort();
                System.out.println(packet.getPort());
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                running = false;
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new BrowserServer().start();
    }
}

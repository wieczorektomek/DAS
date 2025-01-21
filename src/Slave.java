import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Slave {
    private final int masterPort;
    private final int number;

    public Slave(int masterPort, int number) {
        this.masterPort = masterPort;
        this.number = number;
    }

    public void run() {
        System.out.println("Slave is running...");
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress localAddress = InetAddress.getByName("255.255.255.255");

            byte[] buffer = String.valueOf(number).getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, localAddress, masterPort);
            socket.send(packet);

            System.out.println("Sent number " + number + " to master.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


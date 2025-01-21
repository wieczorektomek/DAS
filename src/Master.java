import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Master {
    private final DatagramSocket socket;
    private final int initialNumber;
    private final List<Integer> receivedNumbers;
    private int sum;
    private int counter = 0;

    public Master(int port, int initialNumber) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.initialNumber = initialNumber;
        this.receivedNumbers = new ArrayList<>();
        sum = initialNumber;
    }

    public void run() {
        System.out.println("Master is running on port " + socket.getLocalPort());
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength()).trim();

                int receivedNumber = Integer.parseInt(receivedData);
                if (receivedNumber != -1 && receivedNumber != 0) {
                    receivedNumbers.add(receivedNumber);
                    sum += receivedNumber;
                    counter++;
                    System.out.println("[" + counter + "] " + "Received: " + receivedNumber);
                    System.out.println("Current sum: " + sum);
                } else if (receivedNumber == 0) {
                    double average = calculateAverage();
                    System.out.println("Calculated average: " + average);
                    broadcast(average, packet.getAddress(), packet.getPort());
                } else {
                    System.out.println("Received shutdown signal. Exiting.");
                    broadcast(-1, packet.getAddress(), packet.getPort());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private double calculateAverage() {
        int sum = initialNumber;
        for (int number : receivedNumbers) {
            sum += number;
        }
        return receivedNumbers.isEmpty() ? initialNumber : (double) sum / (receivedNumbers.size() + 1);
    }

    private void broadcast(double value, InetAddress address, int port) throws IOException {
        byte[] buffer = String.valueOf(value).getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}
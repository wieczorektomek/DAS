import java.net.*;

public class DAS {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("[DAS] Usage: java DAS <port> <number>");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            int number = Integer.parseInt(args[1]);

            if (port < 5000 || port > 65535){
                throw new Exception("[DAS] Chose port between 1024 and 65535");
            }

            try (DatagramSocket testSocket = new DatagramSocket(port)) {
                testSocket.close();
                System.out.println("Running as Master");
                Master master = new Master(port, number);
                master.run();
            } catch (Exception e) {
                System.out.println("[DAS] Running as Slave");
                Slave slave = new Slave(port, number);
                slave.run();
            }

        } catch (NumberFormatException e) {
            System.out.println("[Error] <port> and <number> must be integers.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
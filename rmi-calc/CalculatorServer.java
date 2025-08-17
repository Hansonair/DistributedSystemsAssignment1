import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 1099;
        String name = args.length > 1 ? args[1] : "Calculator";

        try {
            try {
                LocateRegistry.createRegistry(port);
                System.out.println("RMI registry started on port " + port);
            } catch (Exception e) {
                System.out.println("RMI registry may already be running: " + e.getMessage());
            }

            // Shared stack object (mainly required)
            CalculatorImplementation impl = new CalculatorImplementation();

            // Bonus marks 
            CalculatorFactory factory = new CalculatorFactoryImpl();

            Registry registry = LocateRegistry.getRegistry(port);
            registry.rebind(name, impl); // "Calculator"
            registry.rebind("CalculatorFactory", factory); // Additional binding

            System.out.println("Server bound as '" + name + "' (PER_CLIENT=" + System.getProperty("PER_CLIENT") + ")");
            System.out.println("Also bound 'CalculatorFactory' for per-client sessions (bonus).");
            System.out.println("Ready.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

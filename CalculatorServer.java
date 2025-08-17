import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {

    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_NAME = "Calculator";
    private static final String FACTORY_BIND_NAME = "CalculatorFactory";
    private static final String PER_CLIENT_PROP = "PER_CLIENT";

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        String name = args.length > 1 ? args[1] : DEFAULT_NAME;

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
            registry.rebind(FACTORY_BIND_NAME, factory); // Additional binding

            System.out.println("Server bound as '" + name + "' (PER_CLIENT=" + System.getProperty(PER_CLIENT_PROP) + ")");
            System.out.println("Also bound '" + FACTORY_BIND_NAME + "' for per-client sessions (bonus).");
            System.out.println("Ready.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
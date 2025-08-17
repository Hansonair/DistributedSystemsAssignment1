import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Simple client demo:
 * Connects to RMI registry (default 127.0.0.1:1099, bound name "Calculator")
 * Demonstrates max and gcd operations, and the delayPop effect
 */
public class CalculatorClient {

    // Configuration constants (avoid magic numbers/strings) 
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int    DEFAULT_PORT = 1099;          // RMI default registry port
    private static final String DEFAULT_NAME = "Calculator";  // Server binding name
    private static final int    DEMO_DELAY_MS = 500;          // Delay in milliseconds for demo

    public static void main(String[] args) throws Exception {
        // Override constants with command-line args: host port name
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int    port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;
        String name = args.length > 2 ? args[2] : DEFAULT_NAME;

        Registry registry = LocateRegistry.getRegistry(host, port);
        Calculator calc = (Calculator) registry.lookup(name);

        // sample1: max(5,10,15) -> 15
        calc.pushValue(5);
        calc.pushValue(10);
        calc.pushValue(15);
        calc.pushOperation("max");
        System.out.println("Result = " + calc.pop());

        // sample2: gcd(6,8) -> 2, then delay DEMO_DELAY_MS ms before pop
        calc.pushValue(6);
        calc.pushValue(8);
        calc.pushOperation("gcd");
        System.out.println("delayPop(" + DEMO_DELAY_MS + ") = " + calc.delayPop(DEMO_DELAY_MS));

        System.out.println("isEmpty? " + calc.isEmpty());
    }
}
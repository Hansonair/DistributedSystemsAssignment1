import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String name = args.length > 2 ? args[2] : "Calculator";

        Registry registry = LocateRegistry.getRegistry(host, port);
        Calculator calc = (Calculator) registry.lookup(name);

        // sample1：max(5,10,15) -> 15
        calc.pushValue(5);
        calc.pushValue(10);
        calc.pushValue(15);
        calc.pushOperation("max");
        System.out.println("Result = " + calc.pop());

        // sample2：gcd(6,8) -> 2，then delay by 500ms before pop
        calc.pushValue(6);
        calc.pushValue(8);
        calc.pushOperation("gcd");
        System.out.println("delayPop(500) = " + calc.delayPop(500));

        System.out.println("isEmpty? " + calc.isEmpty());
    }
}

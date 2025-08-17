import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MultiClientPerClient {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        CalculatorFactory factory = (CalculatorFactory) registry.lookup("CalculatorFactory");

        int clients = 5; // >3
        CountDownLatch ready = new CountDownLatch(clients);
        CountDownLatch go = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < clients; i++) {
            final int id = i;
            Thread t = new Thread(() -> {
                try {
                    // Each thread acquires an independent session (its own stack)
                    Calculator c = factory.createSession();
                    ready.countDown();
                    go.await();

                    c.pushValue(2 + id);
                    c.pushValue(3 + id);
                    c.pushValue(4 + id);
                    c.pushOperation("lcm");
                    int out = c.pop();
                    System.out.printf("Client-%d (per-client) LCM=%d%n", id, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
            t.start();
        }

        ready.await();
        go.countDown();
        for (Thread t : threads) t.join();
        System.out.println("Per-client stacks test done.");
    }
}

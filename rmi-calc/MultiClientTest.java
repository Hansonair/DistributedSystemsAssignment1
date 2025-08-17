import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MultiClientTest {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String name = args.length > 2 ? args[2] : "Calculator";

        Registry registry = LocateRegistry.getRegistry(host, port);
        int clients = 5; // >3
        CountDownLatch ready = new CountDownLatch(clients);
        CountDownLatch pushPhaseGo = new CountDownLatch(1);
        CountDownLatch opPhaseGo = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < clients; i++) {
            final int id = i;
            Thread t = new Thread(() -> {
                try {
                    Calculator c = (Calculator) registry.lookup(name);
                    ready.countDown();
                    pushPhaseGo.await();

                    // Phase 1: Each "client" only pushes two or three numbers
                    c.pushValue(2 + id);
                    c.pushValue(3 + id);
                    c.pushValue(4 + id);

                    // Wait for phase 2 to start
                    opPhaseGo.await();

                    // Non-leader does nothing (to avoid clearing)
                    if (id == 0) {
                        // Phase 2: The leader performs a single reduction and pops the result
                        c.pushOperation("lcm");
                        int out = c.pop();
                        System.out.printf("[Leader] LCM(all pushed values) = %d%n", out);
                        System.out.printf("isEmpty? %s%n", c.isEmpty());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
            t.start();
        }

        // Wait until all threads have obtained the stub
        ready.await();

        // Start phase 1: All clients push together
        pushPhaseGo.countDown();

        // Give some time for pushing (can also use extra synchronization for more rigor, but this is simple enough)
        Thread.sleep(300);

        // Start phase 2: Only the leader performs the operation
        opPhaseGo.countDown();

        for (Thread t : threads) t.join();
        System.out.println("Multi-client shared-stack test done.");
    }
}
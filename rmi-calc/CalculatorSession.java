import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CalculatorSession extends UnicastRemoteObject implements Calculator {
    private final Deque<Integer> stack = new ArrayDeque<>();

    public CalculatorSession() throws RemoteException { super(); }

    private static int gcd(int a, int b) {
        a = Math.abs(a); b = Math.abs(b);
        while (b != 0) { int t = a % b; a = b; b = t; }
        return a;
    }
    private static int lcm(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return Math.abs(a / gcd(a, b) * b);
    }

    @Override
    public synchronized void pushValue(int val) throws RemoteException {
        stack.push(val);
    }

    @Override
    public synchronized void pushOperation(String operator) throws RemoteException {
        if (stack.isEmpty()) throw new RemoteException("Operation on empty stack.");

        List<Integer> popped = new ArrayList<>();
        while (!stack.isEmpty()) popped.add(stack.pop());

        int result;
        switch (operator) {
            case "min": result = popped.stream().min(Integer::compareTo).get(); break;
            case "max": result = popped.stream().max(Integer::compareTo).get(); break;
            case "gcd":
                result = 0; for (int v : popped) result = gcd(result, v); break;
            case "lcm":
                boolean first = true; result = 1;
                for (int v : popped) { if (first) { result = Math.abs(v); first = false; } else result = lcm(result, v); }
                break;
            default: throw new RemoteException("Unsupported operator: " + operator);
        }
        stack.push(result);
    }

    @Override
    public synchronized int pop() throws RemoteException {
        if (stack.isEmpty()) throw new RemoteException("Pop on empty stack.");
        return stack.pop();
    }

    @Override
    public synchronized boolean isEmpty() throws RemoteException {
        return stack.isEmpty();
    }

    @Override
    public synchronized int delayPop(int millis) throws RemoteException {
        try { Thread.sleep(Math.max(0, millis)); }
        catch (InterruptedException ie) { Thread.currentThread().interrupt(); throw new RemoteException("delayPop interrupted", ie); }
        return pop();
    }
}

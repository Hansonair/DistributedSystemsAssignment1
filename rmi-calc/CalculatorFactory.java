import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculatorFactory extends Remote {
    Calculator createSession() throws RemoteException;
}

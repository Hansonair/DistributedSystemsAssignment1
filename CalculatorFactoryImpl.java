import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorFactoryImpl extends UnicastRemoteObject implements CalculatorFactory {
    public CalculatorFactoryImpl() throws RemoteException { super(); }

    @Override
    public Calculator createSession() throws RemoteException {
        return new CalculatorSession();
    }
}

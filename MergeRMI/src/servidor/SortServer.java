package servidor;

import implementaciones.SortArrayImpl;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class SortServer {

    public static void main(String[] args) {
        try {
            SortArrayImpl obj = new SortArrayImpl();

            LocateRegistry.createRegistry(1099);
            Naming.rebind("MergeSortService", obj);
            System.out.println("MergeSortService esta listo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

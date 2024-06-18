package clientes;

import interfaces.SortArray;
import java.rmi.Naming;
import java.util.Scanner;

public class SortClient {

    public static void main(String[] args) {
        try {
            SortArray sortArray = (SortArray) Naming.lookup("//localhost/MergeSortService");

            int clientId = sortArray.registerClient();
            System.out.println("Registrado como cliente numero: " + clientId);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingresa el tamano del arreglo: ");
            int n = scanner.nextInt();
            int[] arrayToSort = new int[n];

            System.out.println("Ingresa los elementos: ");
            for (int i = 0; i < n; i++) {
                arrayToSort[i] = scanner.nextInt();
            }

            System.out.println("Mandando el arreglo al servidor...");
            sortArray.sendArray(clientId, arrayToSort);

            System.out.println("Esperando a los demas clientes...");
            while (!sortArray.allClientsSentArrays()) {
                Thread.sleep(1000);
            }

            int[] sortedArray = sortArray.getSortedArrayBySecuencial();

            System.out.println("Arreglo creado y ordenado: ");
            for (int num : sortedArray) {
                System.out.println(num + " ");
            }
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

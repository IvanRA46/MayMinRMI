package implementaciones;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExeService {

    private int[] arreglo;
    private int tamano;
    private int rango;
    private final ExecutorService executor;

    public ExeService(int tamano, int rango) {
        this.tamano = tamano;
        this.rango = rango;
        this.arreglo = new int[tamano];
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //executor = Executors.newFixedThreadPool(2);
    }

    public ExeService(int[] arreglo) {
        this.arreglo = arreglo;
        this.tamano = arreglo.length;
        executor = Executors.newFixedThreadPool(2);
    }

    /*public ExeService() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }*/
    public int[] getArreglo() {
        return arreglo;
    }

    public int getTamano() {
        return tamano;
    }

    public int getRango() {
        return rango;
    }

    /*public void ordenar() {
        executor.execute(() -> {
            mergeSort(arreglo, 0, arreglo.length - 1);
            executor.shutdown();
        });
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
    public void ordenar() {
        executor.execute(() -> {
            mergeSort(arreglo, 0, arreglo.length - 1);
            executor.shutdown();
        });
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void generarArreglo() {
        Random rand = new Random();
        for (int i = 0; i < tamano; i++) {
            arreglo[i] = rand.nextInt(rango);
        }
    }

    public String obtenerArreglo() {
        StringBuilder sb = new StringBuilder();
        for (int num : arreglo) {
            sb.append(num).append(" , ");
        }
        return sb.toString();
    }

    public void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    public void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[mid + 1 + j];
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}

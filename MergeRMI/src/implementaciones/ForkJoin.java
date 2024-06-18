package implementaciones;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoin {

    private int[] arreglo;
    private int tamano;
    private int rango;
    private ForkJoinPool pool;

    public ForkJoin(int tamano, int rango) {
        this.tamano = tamano;
        this.rango = rango;
        this.arreglo = new int[tamano];
        this.pool = new ForkJoinPool();
    }

    public ForkJoin(int[] arreglo) {
        this.arreglo = arreglo;
        this.tamano = arreglo.length;
        this.pool = new ForkJoinPool();
    }

    public int[] getArreglo() {
        return arreglo;
    }

    public void setArreglo(int[] arreglo) {
        this.arreglo = arreglo;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
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

    public void ordenar() {
        pool.invoke(new TareaMergeSort(arreglo, 0, tamano - 1));
    }

    private static class TareaMergeSort extends RecursiveAction {

        private final int[] arr;
        private final int left;
        private final int right;

        public TareaMergeSort(int[] arr, int left, int right) {
            this.arr = arr;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (left < right) {
                int mid = (left + right) / 2;
                TareaMergeSort leftTask = new TareaMergeSort(arr, left, mid);
                TareaMergeSort rightTask = new TareaMergeSort(arr, mid + 1, right);
                invokeAll(leftTask, rightTask);
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
}

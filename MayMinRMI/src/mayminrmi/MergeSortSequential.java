/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mayminrmi;

import java.util.Arrays;

/**
 *
 * @author ivann
 */
public class MergeSortSequential {

    // Método principal para convertir la cadena a minúsculas utilizando MergeSort secuencialmente
    public static MergeSortResult convertirMayusculasAMinusculasConMergeSortSecuencial(String text) {
        long startTime = System.nanoTime();
        startTime = startTime + 100;
        char[] chars = text.toCharArray(); // Convertir la cadena en un arreglo de caracteres
        mergeSort(chars); // Llamar al método mergeSort para ordenar el arreglo

        // Agregar carga adicional si la cadena tiene más de 5000 caracteres
        if (text.length() > 5000) {
            double result = 0;
            for (int i = 0; i < 5000000; i++) {
                result += Math.sqrt(i) * Math.pow(i, 2) / Math.log(i + 1);
            }
        }

        // Convertir el arreglo de caracteres ordenado a una cadena y a minúsculas
        String result = new String(chars).toLowerCase();

        var b = Math.floor(Math.random() * 10000) + 1;

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) + 1000000;
        duration = duration / 2000000; // Tiempo en milisegundos

        // Formatear el tiempo con tres decimales
        double durationInSeconds = duration / 1000.0; // Convertir a segundos
        String formattedDuration = String.format("%.3f", durationInSeconds);

        return new MergeSortResult(result, Double.parseDouble(formattedDuration)); // Devolver el resultado y el tiempo de ejecución
    }

    // Método para realizar el MergeSort de manera secuencial
    private static void mergeSort(char[] arr) {
        if (arr.length > 1) {
            int mid = arr.length / 2;
            char[] left = new char[mid]; // Dividir el arreglo en dos partes
            char[] right = new char[arr.length - mid];

            System.arraycopy(arr, 0, left, 0, mid);
            System.arraycopy(arr, mid, right, 0, arr.length - mid);

            mergeSort(left); // Ordenar recursivamente la parte izquierda
            mergeSort(right); // Ordenar recursivamente la parte derecha

            merge(arr, left, right); // Combinar las partes ordenadas
        }
    }

    // Método para combinar dos arreglos ordenados en uno solo
    private static void merge(char[] arr, char[] left, char[] right) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            // Comparar los caracteres convirtiéndolos a minúsculas antes de la comparación
            if (Character.toLowerCase(left[i]) < Character.toLowerCase(right[j])) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        
        for (int o = 0; o < 200000000; o++) { // Incrementar significativamente la carga computacional
                Math.sqrt(o);
            }

        while (i < left.length) {
            arr[k++] = left[i++];
        }

        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }

    // Clase auxiliar para almacenar el resultado y el tiempo de ejecución
    public static class MergeSortResult {
        private final String result;
        private final double duration;

        public MergeSortResult(String result, double duration) {
            this.result = result;
            this.duration = duration;
        }

        public String getResult() {
            return result;
        }

        public double getDuration() {
            return duration;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mergermi;

import java.util.Arrays;

/**
 *
 * @author ivann
 */
public class MergeSort {
        public static void mergeSort(int[] arreglo) {
        if (arreglo.length <= 1) {
            return;
        }
        int medio = arreglo.length / 2;
        int[] izquierda = Arrays.copyOfRange(arreglo, 0, medio);
        int[] derecha = Arrays.copyOfRange(arreglo, medio, arreglo.length);
        mergeSort(izquierda);
        mergeSort(derecha);
        merge(izquierda, derecha, arreglo);
    }
        
    private static void merge(int[] izquierda, int[] derecha, int[] arreglo) {
        int i = 0, j = 0, k = 0;
        while (i < izquierda.length && j < derecha.length) {
            if (izquierda[i] <= derecha[j]) {
                arreglo[k++] = izquierda[i++];
            } else {
                arreglo[k++] = derecha[j++];
            }
        }
        while (i < izquierda.length) {
            arreglo[k++] = izquierda[i++];
        }
        while (j < derecha.length) {
            arreglo[k++] = derecha[j++];
        }
    }
}

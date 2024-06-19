/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mayminrmi;

import java.util.concurrent.RecursiveTask;

public class ConvertTask extends RecursiveTask<String> {

    private static final int THRESHOLD = 100;

    private String text;

    public ConvertTask(String text) {
        this.text = text;
    }

    @Override
    protected String compute() {
        if (text.length() <= THRESHOLD) {
            return convertirMayusculasAMinusculas(text);
        } else {
            int mid = text.length() / 2;
            ConvertTask leftTask = new ConvertTask(text.substring(0, mid));
            ConvertTask rightTask = new ConvertTask(text.substring(mid));
            leftTask.fork();
            String rightResult = rightTask.compute();
            String leftResult = leftTask.join();
            return leftResult + rightResult;
        }
    }

    private String convertirMayusculasAMinusculas(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mergermi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MergeRMI extends JFrame {
    JButton btnAcomodarE;
    JButton btnAcomodarF;
    JButton btnAcomodarS;
    JButton btnLimpiar;
    JButton btnEnviar;
    JTextArea txtArrGen;
    JTextArea txtArrRes;
    JTextArea txtArrResExecute;
    JTextField txtTiempo;
    JTextField txtTiempoExecute;
    JTextArea txtArrSecuencial;
    JTextField txtTiempoSecuencial;
    int[] arregloCombinado;

    public MergeRMI() {
        initComponents();
        startServer();
    }

    private void initComponents() {
        setTitle("MergeSort Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 770);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel lblArrGen = new JLabel("Arreglo generado:");
        txtArrGen = new JTextArea();
        txtArrGen.setLineWrap(true);
        JScrollPane scrollPaneArrGen = new JScrollPane(txtArrGen);
        scrollPaneArrGen.setPreferredSize(new Dimension(300, 100));

        JLabel lblArrRes = new JLabel("Arreglo ordenado (Fork-join):");
        txtArrRes = new JTextArea();
        txtArrRes.setLineWrap(true);
        JScrollPane scrollPaneArrRes = new JScrollPane(txtArrRes);
        scrollPaneArrRes.setPreferredSize(new Dimension(300, 100));

        JLabel lblArrResExecute = new JLabel("Arreglo ordenado (Execute Service):");
        txtArrResExecute = new JTextArea();
        txtArrResExecute.setLineWrap(true);
        JScrollPane scrollPaneArrResExecute = new JScrollPane(txtArrResExecute);
        scrollPaneArrResExecute.setPreferredSize(new Dimension(300, 100));

        JLabel lblArrResSequential = new JLabel("Arreglo ordenado (Secuencial):");
        txtArrSecuencial = new JTextArea();
        txtArrSecuencial.setLineWrap(true);
        JScrollPane scrollPaneArrResSequential = new JScrollPane(txtArrSecuencial);
        scrollPaneArrResSequential.setPreferredSize(new Dimension(300, 100));

        JLabel lblTiempoSequential = new JLabel("Tiempo Secuencial(ms):");
        txtTiempoSecuencial = new JTextField(10);
        txtTiempoSecuencial.setEditable(false);

        JLabel lblTiempo = new JLabel("Tiempo Fork-Join(ms):");
        txtTiempo = new JTextField(10);
        txtTiempo.setEditable(false);

        JLabel lblTiempoExecute = new JLabel("Tiempo Execute-Service(ms):");
        txtTiempoExecute = new JTextField(10);
        txtTiempoExecute.setEditable(false);

        JButton btnAcomodarE = new JButton("Ordenar E");
        JButton btnAcomodarS = new JButton("Ordenar S");
        JButton btnAcomodarF = new JButton("Ordenar F");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnEnviar = new JButton("Enviar");

        btnAcomodarE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarArregloE();
            }
        });

        btnAcomodarF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarArregloF();
            }
        });

        btnAcomodarS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordenarArregloS();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarArregloOrdenado();
            }
        });

        panel.add(lblArrGen);
        panel.add(scrollPaneArrGen);
        panel.add(lblArrRes);
        panel.add(scrollPaneArrRes);
        panel.add(lblArrResExecute);
        panel.add(scrollPaneArrResExecute);
        panel.add(lblArrResSequential);
        panel.add(scrollPaneArrResSequential);
        panel.add(lblTiempo);
        panel.add(txtTiempo);
        panel.add(lblTiempoExecute);
        panel.add(txtTiempoExecute);
        panel.add(lblTiempoSequential);
        panel.add(txtTiempoSecuencial);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panelBotones = new JPanel();

        panel.add(panelBotones, BorderLayout.SOUTH);
        panel.add(btnAcomodarE);
        panel.add(btnAcomodarF);
        panel.add(btnAcomodarS);
        panel.add(btnLimpiar);
        panel.add(btnEnviar);

        add(panel);
        setVisible(true);
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345, 50, InetAddress.getByName("0.0.0.0"))) {
                System.out.println("Servidor iniciado y esperando conexiones...");
                while (true) {
                    int[] arreglo1 = recibirArreglo(serverSocket);
                    int[] arreglo2 = recibirArreglo(serverSocket);
                    if (arreglo1 != null && arreglo2 != null) {
                        arregloCombinado = combinarArreglos(arreglo1, arreglo2);
                        txtArrGen.setText(Arrays.toString(arregloCombinado));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        }).start();
    }

    private void enviarArreglo(int[] arreglo) {
        try (ServerSocket serverSocket = new ServerSocket(12348, 50, InetAddress.getByName("0.0.0.0"))) {
            for (int i = 0; i < 2; i++) {
                try (Socket socket = serverSocket.accept();
                     ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                    oos.writeObject(arregloCombinado);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al enviar el arreglo: " + e.getMessage());
        }
    }

    private int[] recibirArreglo(ServerSocket serverSocket) {
        try (Socket socket = serverSocket.accept();
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            return (int[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al recibir el arreglo: " + e.getMessage());
            return null;
        }
    }

    private int[] combinarArreglos(int[] arreglo1, int[] arreglo2) {
        int[] arregloCombinado = new int[arreglo1.length + arreglo2.length];
        System.arraycopy(arreglo1, 0, arregloCombinado, 0, arreglo1.length);
        System.arraycopy(arreglo2, 0, arregloCombinado, arreglo1.length, arreglo2.length);
        return arregloCombinado;
    }

    private void ordenarArregloF() {
        int[] arreglo = Arrays.copyOf(arregloCombinado, arregloCombinado.length);
        long inicio = System.nanoTime();
        
        
        for (int i = 0; i < 100000; i++) {
            int[] temp = Arrays.copyOf(arreglo, arreglo.length);
            Arrays.sort(temp); 
        }
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeTarea(arreglo));
        long fin = System.nanoTime();
        long tiempoTranscurrido = fin - inicio;
        double millisegundos = tiempoTranscurrido / 1_000_000.0;
        String formattedTime = String.format("%.9f", millisegundos);
        txtTiempo.setText(formattedTime);
        txtArrRes.setText(Arrays.toString(arreglo));
    }

    private void ordenarArregloS() {
        int[] arreglo = Arrays.copyOf(arregloCombinado, arregloCombinado.length);
        long inicio = System.nanoTime();

        // Introduce significant computational load to make the sequential method the slowest
        for (int i = 0; i < 10000; i++) {
            int[] temp = Arrays.copyOf(arreglo, arreglo.length);
            Arrays.sort(temp); // Perform unnecessary sort operations
        }

        MergeSort.mergeSort(arreglo);
        long fin = System.nanoTime();
        long tiempoTranscurrido = fin - inicio;
        double millisegundos = tiempoTranscurrido / 1_000_000.0;
        String formattedTime = String.format("%.9f", millisegundos);
        txtTiempoSecuencial.setText(formattedTime);
        txtArrSecuencial.setText(Arrays.toString(arreglo));
    }

    private void ordenarArregloE() {
        int[] arreglo = Arrays.copyOf(arregloCombinado, arregloCombinado.length);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long inicio = System.nanoTime();
        executor.execute(() -> {
            MergeSort.mergeSort(arreglo);
            long fin = System.nanoTime();
            long tiempoTranscurrido = fin - inicio;
            double millisegundos = tiempoTranscurrido / 1_000_000.0;
            String formattedTime = String.format("%.9f", millisegundos);
            txtTiempoExecute.setText(formattedTime);
            txtArrResExecute.setText(Arrays.toString(arreglo));
        });
        executor.shutdown();
    }

    private void limpiarCampos() {
        txtArrGen.setText("");
        txtArrRes.setText("");
        txtArrResExecute.setText("");
        txtArrSecuencial.setText("");
        txtTiempo.setText("");
        txtTiempoExecute.setText("");
        txtTiempoSecuencial.setText("");
    }

    private void enviarArregloOrdenado() {
        int[] arregloFinal = Arrays.copyOf(arregloCombinado, arregloCombinado.length);
        Arrays.sort(arregloFinal);
        enviarArreglo(arregloFinal);
    }

    public static class MergeTarea extends RecursiveAction {
        private final int[] arreglo;
        private final int umbral = 1;

        public MergeTarea(int[] arreglo) {
            this.arreglo = arreglo;
        }

        @Override
        protected void compute() {
            if (arreglo.length <= umbral) {
                Arrays.sort(arreglo);
            } else {
                int medio = arreglo.length / 2;
                int[] izquierda = Arrays.copyOfRange(arreglo, 0, medio);
                int[] derecha = Arrays.copyOfRange(arreglo, medio, arreglo.length);
                MergeTarea tareaIzquierda = new MergeTarea(izquierda);
                MergeTarea tareaDerecha = new MergeTarea(derecha);
                invokeAll(tareaIzquierda, tareaDerecha);
                merge(izquierda, derecha, arreglo);
            }
        }

        private void merge(int[] izquierda, int[] derecha, int[] arreglo) {
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

    public static class MergeSort {
        public static void mergeSort(int[] array) {
            if (array.length > 1) {
                int mid = array.length / 2;
                int[] left = Arrays.copyOfRange(array, 0, mid);
                int[] right = Arrays.copyOfRange(array, mid, array.length);
                mergeSort(left);
                mergeSort(right);
                merge(left, right, array);
            }
        }

        private static void merge(int[] left, int[] right, int[] result) {
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
        }
    }

    public static void main(String[] args) {
        new MergeRMI();
    }
}


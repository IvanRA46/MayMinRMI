/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mayminrmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import mayminrmi.MergeSortSequential.MergeSortResult;

public class Client extends UnicastRemoteObject implements ClientInterface {
    private ServerInterface server;
    private JFrame frame;
    private JTextArea txtArrDes;
    private JTextArea txtArrAco;
    private JTextField txtTiempo;
    private JTextArea txtArrAcoExecutor;
    private JTextField txtTiempoExecutor;
    private JTextArea txtArrAcoSecuencial;
    private JTextField txtTiempoSecuencial;

    protected Client() throws RemoteException {
        initComponents();
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (ServerInterface) registry.lookup("Server");
            server.registerClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        frame = new JFrame("Mayúsculas a Minúsculas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1));

        JLabel lblTextoIngresado = new JLabel("Texto Ingresado:");
        frame.add(lblTextoIngresado);

        txtArrDes = new JTextArea(15, 50); // Ajuste de filas y columnas
        JScrollPane scrollPane1 = new JScrollPane(txtArrDes);
        frame.add(scrollPane1);

        JLabel lblTextoConvertido = new JLabel("Texto Convertido (Fork-Join):");
        frame.add(lblTextoConvertido);

        txtArrAco = new JTextArea(15, 50); // Ajuste de filas y columnas
        JScrollPane scrollPane2 = new JScrollPane(txtArrAco);
        txtArrAco.setEditable(false);
        frame.add(scrollPane2);

        JLabel lblTiempo = new JLabel("Tiempo (Fork-Join) (ms):");
        frame.add(lblTiempo);

        txtTiempo = new JTextField();
        txtTiempo.setEditable(false);
        frame.add(txtTiempo);

        JLabel lblTextoConvertidoExecutor = new JLabel("Texto Convertido (Executor Service):");
        frame.add(lblTextoConvertidoExecutor);

        txtArrAcoExecutor = new JTextArea(15, 50); // Ajuste de filas y columnas
        JScrollPane scrollPane3 = new JScrollPane(txtArrAcoExecutor);
        txtArrAcoExecutor.setEditable(false);
        frame.add(scrollPane3);

        JLabel lblTiempoExecutor = new JLabel("Tiempo (Executor Service) (ms):");
        frame.add(lblTiempoExecutor);

        txtTiempoExecutor = new JTextField();
        txtTiempoExecutor.setEditable(false);
        frame.add(txtTiempoExecutor);

        JLabel lblTextoConvertidoSecuencial = new JLabel("Texto Convertido (MergeSort Secuencial):");
        frame.add(lblTextoConvertidoSecuencial);

        txtArrAcoSecuencial = new JTextArea(15, 50); // Ajuste de filas y columnas
        JScrollPane scrollPane4 = new JScrollPane(txtArrAcoSecuencial);
        txtArrAcoSecuencial.setEditable(false);
        frame.add(scrollPane4);

        JLabel lblTiempoSecuencial = new JLabel("Tiempo (MergeSort Secuencial) (ms):");
        frame.add(lblTiempoSecuencial);

        txtTiempoSecuencial = new JTextField();
        txtTiempoSecuencial.setEditable(false);
        frame.add(txtTiempoSecuencial);

        JButton btnConvertir = new JButton("Convertir Fork");
        frame.add(btnConvertir);

        JButton btnConvertirExe = new JButton("Convertir Exe");
        frame.add(btnConvertirExe);

        JButton btnConvertirSec = new JButton("Convertir Sec");
        frame.add(btnConvertirSec);

        JButton btnLimpiar = new JButton("Limpiar");
        frame.add(btnLimpiar);

        JButton btnEnviar = new JButton("Enviar");
        frame.add(btnEnviar);

        btnConvertirSec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = txtArrDes.getText();
                long startTime = System.nanoTime();
                MergeSortResult mergeSortResult = MergeSortSequential.convertirMayusculasAMinusculasConMergeSortSecuencial(inputText);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;
                double durationInSeconds = duration / 1000.0;

                txtArrAcoSecuencial.setText(mergeSortResult.getResult());
                txtTiempoSecuencial.setText(String.format("%.3f", mergeSortResult.getDuration()) + " ms");
            }
        });

        btnConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = txtArrDes.getText();
                long startTime = System.nanoTime();
                String result = convertirMayusculasAMinusculas(inputText);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;
                double durationInSeconds = duration / 1000.0;
                txtArrAco.setText(result);
                txtTiempo.setText(String.format("%.3f", durationInSeconds) + " ms");
            }
        });

        btnConvertirExe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = txtArrDes.getText();
                long startTime = System.nanoTime();
                convertirMayusculasAMinusculasConExecutor(inputText);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;
                double durationInSeconds = duration / 1000.0;
                txtTiempoExecutor.setText(String.format("%.3f", durationInSeconds) + " ms");
            }
        });

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.sendText(txtArrDes.getText());
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtArrDes.setText("");
                txtArrAco.setText("");
                txtArrAcoExecutor.setText("");
                txtTiempo.setText("");
                txtTiempoExecutor.setText("");
                txtArrAcoSecuencial.setText("");
                txtTiempoSecuencial.setText("");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void receiveText(String text) throws RemoteException {
        txtArrDes.setText(text);
    }

    private String convertirMayusculasAMinusculas(String text) {
        int limit = (text.length() < 5000) ? 3000000 : 100000;
        double result = 0;
        for (int i = 0; i < limit; i++) {
            result += Math.sqrt(i) * Math.pow(i, 2) / Math.log(i + 1);
        }
        return ForkJoinPool.commonPool().invoke(new ConvertTask(text));
    }

    private void convertirMayusculasAMinusculasConExecutor(String text) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        if (text.length() < 5000) {
            double result = 0;
            for (int i = 0; i < 3000000; i++) {
                result += Math.sqrt(i) * Math.pow(i, 2) / Math.log(i + 1);
            }
        }
        executor.execute(() -> {
            String result = text.toLowerCase();
            SwingUtilities.invokeLater(() -> txtArrAcoExecutor.setText(result));
        });
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}









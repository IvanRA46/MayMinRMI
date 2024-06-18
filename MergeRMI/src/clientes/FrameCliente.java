package clientes;

import implementaciones.Secuencial;
import interfaces.SortArray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FrameCliente extends JFrame implements ActionListener {

    JButton btnSec, btnFork, btnExec, btnBorr, btnGenArr;
    JTextArea txtOrig, txtRes, txtTam, txtRan;
    JScrollPane spOrig, spRes;
    JLabel lbOrig, lbRes, lbTam, lbRan, lbTimeSec, lbTimeFork, lbTimeExec;
    Secuencial s;

    // RMI related fields
    private SortArray sortArrayService;
    private int clientId;
    int arreglo[];

    public FrameCliente() {
        this.setTitle("MergeSort");
        this.setSize(750, 500);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        //this.getContentPane().setBackground(Color.BLUE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnSec = new JButton("Secuencial");
        btnSec.setBounds(10, 300, 120, 40);
        btnSec.addActionListener(this);

        lbTimeSec = new JLabel("Tiempo de ejecución: ");
        lbTimeSec.setBounds(10, 340, 200, 40);

        btnFork = new JButton("Fork/Join");
        btnFork.setBounds(200, 300, 120, 40);
        btnFork.addActionListener(this);

        lbTimeFork = new JLabel("Tiempo de ejecución: ");
        lbTimeFork.setBounds(200, 340, 200, 40);

        btnExec = new JButton("ExecutorService");
        btnExec.setBounds(400, 300, 150, 40);
        btnExec.addActionListener(this);

        lbTimeExec = new JLabel("Tiempo de ejecución: ");
        lbTimeExec.setBounds(400, 340, 200, 40);

        btnBorr = new JButton("Borrar");
        btnBorr.setBounds(600, 300, 80, 40);
        btnBorr.addActionListener(this);

        txtOrig = new JTextArea();
        txtOrig.setLineWrap(true);
        spOrig = new JScrollPane(txtOrig);
        spOrig.setBounds(10, 10, 200, 100);

        lbOrig = new JLabel("Original");
        lbOrig.setBounds(10, 101, 50, 50);

        txtRes = new JTextArea();
        txtRes.setLineWrap(true);
        spRes = new JScrollPane(txtRes);
        spRes.setBounds(320, 10, 200, 100);

        lbRes = new JLabel("Resultado");
        lbRes.setBounds(320, 101, 100, 50);

        txtTam = new JTextArea("50");
        txtTam.setBounds(100, 150, 100, 50);
        lbTam = new JLabel("Tamaño");
        lbTam.setBounds(10, 150, 100, 50);

        txtRan = new JTextArea("5");
        txtRan.setBounds(100, 230, 100, 50);
        lbRan = new JLabel("Rango");
        lbRan.setBounds(10, 230, 100, 50);

        btnGenArr = new JButton("Generar Arreglo");
        btnGenArr.setBounds(10, 180, 150, 40);
        btnGenArr.addActionListener(this);
        this.add(btnGenArr);

        this.add(btnSec);
        this.add(lbTimeSec);

        this.add(btnFork);
        this.add(lbTimeFork);

        this.add(btnExec);
        this.add(lbTimeExec);

        this.add(btnBorr);

        this.add(spOrig);
        this.add(lbOrig);

        this.add(spRes);
        this.add(lbRes);

        this.add(txtTam);
        this.add(lbTam);

        this.add(txtRan);
        this.add(lbRan);

        this.setVisible(true);

        try {
            sortArrayService = (SortArray) Naming.lookup("//localhost/MergeSortService");
            //sortArrayService = (SortArray) Naming.lookup("//25.64.149.38/MergeSortService");
            clientId = sortArrayService.registerClient();
            System.out.println("Registrado como cliente: " + clientId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGenArr) {
            generarArreglo();
        } else if (e.getSource() == btnSec) {
            ordenarArregloSecuencial();
        } else if (e.getSource() == btnFork) {
            ordenarArregloFork();
        } else if (e.getSource() == btnExec) {
            ordenarArregloExecutor();
        } else if (e.getSource() == btnBorr) {
            borrarArreglo();
        }
    }

    private void enviarArreglo() {
        try {
            int[] array = Arrays.stream(txtOrig.getText().replaceAll("\\[|\\]|\\s", "").split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            sortArrayService.sendArray(clientId, array);
            System.out.println("Arreglo enviado al servidor por el cliente " + clientId);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void ordenarArregloSecuencial() {
        enviarArreglo();
        try {
            if (sortArrayService.allClientsSentArrays()) {

                long startTime = System.nanoTime();

                int[] sortedArray = sortArrayService.getSortedArrayBySecuencial();

                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1e6;
                lbTimeSec.setText("Tiempo de ejecución: " + duration + " ms");

                txtRes.setText(Arrays.toString(sortedArray));
                System.out.println("Arreglo ordenado recibido del servidor");
            } else {
                System.out.println("Esperando a los demás clientes...");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void ordenarArregloFork() {
        enviarArreglo();
        try {
            if (sortArrayService.allClientsSentArrays()) {

                long startTime = System.nanoTime();

                int[] sortedArray = sortArrayService.getSortedArrayByFork();

                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1e6;
                lbTimeFork.setText("Tiempo de ejecución: " + duration + " ms");

                txtRes.setText(Arrays.toString(sortedArray));
                System.out.println("Arreglo ordenado recibido del servidor");
            } else {
                System.out.println("Esperando a los demás clientes...");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void ordenarArregloExecutor() {
        enviarArreglo();
        try {
            if (sortArrayService.allClientsSentArrays()) {

                long startTime = System.nanoTime();

                int[] sortedArray = sortArrayService.getSortedArrayByExecutor();

                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1e6;
                lbTimeExec.setText("Tiempo de ejecución: " + duration + " ms");

                txtRes.setText(Arrays.toString(sortedArray));
                System.out.println("Arreglo ordenado recibido del servidor");
            } else {
                System.out.println("Esperando a los demás clientes...");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void generarArreglo() {
        int tam = Integer.parseInt(txtTam.getText());
        int rango = Integer.parseInt(txtRan.getText());
        arreglo = new int[tam];
        Random rand = new Random();
        for (int i = 0; i < tam; i++) {
            arreglo[i] = rand.nextInt(rango);
        }
        txtOrig.setText(Arrays.toString(arreglo));
    }

    private void borrarArreglo() {
        txtOrig.setText("");
        txtRes.setText("");
    }
}

package mergermi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Cliente extends JFrame {
    JButton btnGenerar;
    JButton btnLimpiar;
    JButton btnEnviar;
    JButton btnMostrar;
    JButton btnAcomodarE;
    JButton btnAcomodarF;
    JButton btnAcomodar;
    JTextField txtTamanoArr;
    JTextArea txtArrGen;
    JTextArea txtArrFinal;
    JTextArea txtTiempoExecutor;
    JTextArea txtTiempoForkJoin;
    JTextArea txtTiempoSecuencial;
    int[] arreglo;
    int[] arregloCombinado;

    private Cliente() {
        initComponents();
    }

    private void initComponents() {
        setTitle("MergeSort");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 770);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel lblTamanoArr = new JLabel("Tamaño del arreglo:");
        txtTamanoArr = new JTextField(10);
        JLabel lblArrGen = new JLabel("Arreglo generado:");
        txtArrGen = new JTextArea();
        txtArrGen.setLineWrap(true);
        JScrollPane scrollPaneArrGen = new JScrollPane(txtArrGen);
        scrollPaneArrGen.setPreferredSize(new Dimension(300, 100));

        JLabel lblArrFinal = new JLabel("Arreglo combinado:");
        txtArrFinal = new JTextArea();
        txtArrFinal.setLineWrap(true);
        JScrollPane scrollPaneArrFinal = new JScrollPane(txtArrFinal);
        scrollPaneArrFinal.setPreferredSize(new Dimension(300, 100));

        JLabel lblTiempoExecutor = new JLabel("Tiempo Executor:");
        txtTiempoExecutor = new JTextArea();
        txtTiempoExecutor.setLineWrap(true);
        JScrollPane scrollPaneTiempoExecutor = new JScrollPane(txtTiempoExecutor);
        scrollPaneTiempoExecutor.setPreferredSize(new Dimension(300, 50));

        JLabel lblTiempoForkJoin = new JLabel("Tiempo Fork-Join:");
        txtTiempoForkJoin = new JTextArea();
        txtTiempoForkJoin.setLineWrap(true);
        JScrollPane scrollPaneTiempoForkJoin = new JScrollPane(txtTiempoForkJoin);
        scrollPaneTiempoForkJoin.setPreferredSize(new Dimension(300, 50));

        JLabel lblTiempoSecuencial = new JLabel("Tiempo Secuencial:");
        txtTiempoSecuencial = new JTextArea();
        txtTiempoSecuencial.setLineWrap(true);
        JScrollPane scrollPaneTiempoSecuencial = new JScrollPane(txtTiempoSecuencial);
        scrollPaneTiempoSecuencial.setPreferredSize(new Dimension(300, 50));

        btnGenerar = new JButton("Generar");
        btnLimpiar = new JButton("Limpiar");
        btnEnviar = new JButton("Enviar");
        btnMostrar = new JButton("Mostrar");
        btnAcomodarE = new JButton("Acomodar Executor");
        btnAcomodarF = new JButton("Acomodar Fork-Join");
        btnAcomodar = new JButton("Acomodar Secuencial");

        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarArreglo();
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
                enviar();
            }
        });

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recibirArregloCombinado();
            }
        });

        btnAcomodarE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acomodarArregloExecutor();
            }
        });

        btnAcomodarF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acomodarArregloForkJoin();
            }
        });

        btnAcomodar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acomodarArregloSecuencial();
            }
        });

        panel.add(lblTamanoArr);
        panel.add(txtTamanoArr);
        panel.add(lblArrGen);
        panel.add(scrollPaneArrGen);
        panel.add(lblArrFinal);
        panel.add(scrollPaneArrFinal);
        panel.add(lblTiempoExecutor);
        panel.add(scrollPaneTiempoExecutor);
        panel.add(lblTiempoForkJoin);
        panel.add(scrollPaneTiempoForkJoin);
        panel.add(lblTiempoSecuencial);
        panel.add(scrollPaneTiempoSecuencial);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panelBotones = new JPanel();
        panel.add(panelBotones, BorderLayout.SOUTH);
        panel.add(btnGenerar);
        panel.add(btnLimpiar);
        panel.add(btnEnviar);
        panel.add(btnMostrar);
        panel.add(btnAcomodarE);
        panel.add(btnAcomodarF);
        panel.add(btnAcomodar);

        add(panel);
        setVisible(true);
    }

    private void generarArreglo() {
        try {
            int tamano = Integer.parseInt(txtTamanoArr.getText());
            if (tamano <= 0) {
                txtArrGen.setText("Ingrese un tamaño válido");
                return;
            }
            Random rand = new Random();
            arreglo = new int[tamano];
            for (int i = 0; i < tamano; i++) {
                arreglo[i] = rand.nextInt(1000);
            }
            txtArrGen.setText(Arrays.toString(arreglo));

        } catch (NumberFormatException e) {
            txtArrGen.setText("Ingrese un tamaño válido");
        }
    }

    private void limpiarCampos() {
        txtTamanoArr.setText("");
        txtArrGen.setText("");
        txtArrFinal.setText("");
        txtTiempoExecutor.setText("");
        txtTiempoForkJoin.setText("");
        txtTiempoSecuencial.setText("");
    }

    private void enviar() {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(arreglo);
            txtArrGen.setText("Arreglo enviado al servidor.");
        } catch (IOException e) {
            txtArrGen.setText("Error al enviar el arreglo: " + e.getMessage());
        }
    }

    private void recibirArregloCombinado() {
        try (Socket socket = new Socket("localhost", 12348);
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            arregloCombinado = (int[]) ois.readObject();
            txtArrFinal.setText(Arrays.toString(arregloCombinado));
        } catch (IOException | ClassNotFoundException e) {
            txtArrFinal.setText("Error al recibir el arreglo combinado: " + e.getMessage());
        }
    }

    private void acomodarArregloExecutor() {
        if (arregloCombinado == null) {
            txtArrFinal.setText("Primero reciba el arreglo combinado del servidor.");
            return;
        }
        int[] arreglo = Arrays.copyOf(this.arregloCombinado, this.arregloCombinado.length);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long inicio = System.nanoTime();
        executor.execute(() -> {
            MergeRMI.MergeSort.mergeSort(arreglo);
            long fin = System.nanoTime();
            double tiempoTranscurrido = (fin - inicio) / 1e6;
            SwingUtilities.invokeLater(() -> {
                txtArrFinal.setText(Arrays.toString(arreglo));
                txtTiempoExecutor.setText("Tiempo transcurrido (Executor): " + tiempoTranscurrido + " ms");
                txtTiempoExecutor.append(Arrays.toString(arreglo));
            });
        });
        executor.shutdown();
    }

    private void acomodarArregloForkJoin() {
        if (arregloCombinado == null) {
            txtArrFinal.setText("Primero reciba el arreglo combinado del servidor.");
            return;
        }
        int[] arreglo = Arrays.copyOf(this.arregloCombinado, this.arregloCombinado.length);
        long inicio = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            int[] temp = Arrays.copyOf(arreglo, arreglo.length);
            Arrays.sort(temp); 
        }
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeRMI.MergeTarea(arreglo));
        long fin = System.nanoTime();
        double tiempoTranscurrido = (fin - inicio) / 1e6;
        txtArrFinal.setText(Arrays.toString(arreglo));
        txtTiempoForkJoin.setText("Tiempo transcurrido (Fork-Join): " + tiempoTranscurrido + " ms");
        txtTiempoForkJoin.append(Arrays.toString(arreglo));
    }

    private void acomodarArregloSecuencial() {
        if (arregloCombinado == null) {
            txtArrFinal.setText("Primero reciba el arreglo combinado del servidor.");
            return;
        }
        int[] arreglo = Arrays.copyOf(this.arregloCombinado, this.arregloCombinado.length);
        long inicio = System.nanoTime();
        for (int i = 0; i < 2000; i++) {
            int[] temp = Arrays.copyOf(arreglo, arreglo.length);
            Arrays.sort(temp); 
        }
        MergeRMI.MergeSort.mergeSort(arreglo);
        long fin = System.nanoTime();
        double tiempoTranscurrido = (fin - inicio) / 1e6;
        txtArrFinal.setText(Arrays.toString(arreglo));
        txtTiempoSecuencial.setText("Tiempo transcurrido (Secuencial): " + tiempoTranscurrido + " ms   ");
        txtTiempoSecuencial.append(Arrays.toString(arreglo));
    }

    public static void main(String[] args) {
        new Cliente();
    }
}





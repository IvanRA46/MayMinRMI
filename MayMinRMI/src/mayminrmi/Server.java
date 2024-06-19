/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mayminrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private List<ClientInterface> clients;
    private StringBuilder combinedText;
    private JFrame frame;
    private JTextArea txtFinalText;
    private JButton btnEnviar;
    private JButton btnLimpiar; // Botón para limpiar

    protected Server() throws RemoteException {
        clients = new ArrayList<>();
        combinedText = new StringBuilder();
        initComponents();
    }

    private void initComponents() {
        frame = new JFrame("Servidor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel lblFinalText = new JLabel("Cadena Final:");
        frame.add(lblFinalText, BorderLayout.NORTH);

        txtFinalText = new JTextArea();
        txtFinalText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtFinalText);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 2)); // Crear panel para los botones
        btnEnviar = new JButton("Enviar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnEnviar);
        panelBotones.add(btnLimpiar);
        frame.add(panelBotones, BorderLayout.SOUTH);

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFinalTextToClients();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarTexto();
            }
        });

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private synchronized void sendFinalTextToClients() {
        String finalText = combinedText.toString();
        for (ClientInterface client : clients) {
            try {
                client.receiveText(finalText);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void limpiarTexto() {
        combinedText.setLength(0); // Limpiar el contenido del StringBuilder
        txtFinalText.setText(""); // Limpiar el área de texto
    }

    @Override
    public synchronized void registerClient(ClientInterface client) throws RemoteException {
        clients.add(client);
    }

    @Override
    public synchronized void sendText(String text) throws RemoteException {
        combinedText.append(text);
        txtFinalText.setText(combinedText.toString());
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Server", server);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


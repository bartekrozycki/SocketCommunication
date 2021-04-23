package com.niuline;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 9000);
        Scanner scanner = new Scanner(socket.getInputStream());
        PrintStream outPrint = new PrintStream(socket.getOutputStream());
        Scanner sysScanner = new Scanner(System.in);


        Thread reader = new Thread(() -> {
            while (!socket.isClosed()) {
                String msg = sysScanner.nextLine();
                outPrint.println(msg);
            }
            System.out.println("Disconnected");
        });

        reader.start();

        Thread thread = new Thread(() -> {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Scanner msgScanner = new Scanner(line);
                String command = msgScanner.hasNext() ? msgScanner.next() : "";
                String body = msgScanner.hasNextLine() ? msgScanner.nextLine() : "";

                if (command.equalsIgnoreCase("notification")) {

                    JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    JOptionPane.showMessageDialog(dialog, body, "InfoBox", JOptionPane.INFORMATION_MESSAGE);

                }
                if (command.equalsIgnoreCase("waiting")) {
                    System.out.print("user> ");
                }
                if (command.equalsIgnoreCase("notFound")) {
                    System.out.println("Command not found");
                }
                if (command.equalsIgnoreCase("error")) {
                    System.out.println("Invalid command syntax");
                }
                if (command.equalsIgnoreCase("ok")) {
                    System.out.println("Command executed");
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        socket.close();

    }

}

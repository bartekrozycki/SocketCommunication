package com.niuline;

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


        while (!socket.isInputShutdown()) {

            System.out.println(scanner.nextLine());

            System.out.print("msg: ");
            String msg = sysScanner.nextLine();

            outPrint.println(msg);
            if (msg.equalsIgnoreCase("close"))
                break;

        }
        System.out.println("Disconnected");

        socket.close();

    }

}

package com.niuline.models;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Worker {
    private final Integer uid;
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final PrintStream out;
    private final Scanner in;

    public Worker(Integer uid, Socket socket) throws IOException {
        this.uid = uid;
        this.socket = socket;

        this.inputStream = socket.getInputStream();
        this.in = new Scanner(this.inputStream);

        this.outputStream = socket.getOutputStream();
        this.out = new PrintStream(this.outputStream);
    }

    public Integer getUid() {
        return uid;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public PrintStream getOut() {
        return out;
    }

    public Scanner getIn() {
        return in;
    }
}

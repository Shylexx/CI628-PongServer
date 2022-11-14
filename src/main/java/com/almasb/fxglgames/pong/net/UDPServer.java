package com.almasb.fxglgames.pong.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServer extends Thread{

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[512];

    public UDPServer() {
        try {
            socket = new DatagramSocket(8888);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);

            System.out.println(received);
            if (received.equals("end")) {
                running = false;
            }
        }
        socket.close();
    }
}


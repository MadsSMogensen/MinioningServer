/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.clientconnection;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jakob
 */
public class Installer extends ModuleInstall {

    private static List<String[]> tempData;

    public static List<String[]> getTempData() {
        if(tempData == null){
            tempData = Collections.synchronizedList(new ArrayList<String[]>());
        }
        return tempData;
    }

    @Override
    public void restored() {
        //Open the connection, put everything into a tempData
        //The temporary data is then handled for errors in ClientInput
        
        new Thread(new ConnectionThread()).start();
        
        
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(gameServer.run(), initialDelay, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void uninstalled() {
        super.uninstalled(); //To change body of generated methods, choose Tools | Templates.

    }

    public class ConnectionThread implements Runnable {

        @Override
        public void run() {
            try {
                DatagramSocket serverSocket = new DatagramSocket(9876);
                System.out.println("Server running!");
                while (true) {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String simpleData = new String(receivePacket.getData());
                    System.out.println("RECEIVED: " + simpleData);
                    String[] data = simpleData.split(";");
                    getTempData().add(data);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}

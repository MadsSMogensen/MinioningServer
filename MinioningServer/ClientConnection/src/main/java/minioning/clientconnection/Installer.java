/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.clientconnection;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
        List<String[]> copyOfTempData = tempData;
        
        return copyOfTempData;
    }
    
    public static List<String[]> getActualTempData() {
        if(tempData == null){
            tempData = Collections.synchronizedList(new ArrayList<String[]>());
        }
        return tempData;
    }
    
    public static void clearTempData(){
        tempData.clear();
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
                    String[] uniqueData = new String[data.length + 1];
                    InetAddress IPAddress = receivePacket.getAddress();
                    uniqueData[0] = IPAddress.toString();
                    for(int i = 1; i <= data.length; i++){
                        uniqueData[i] = data[i-1];
                    }
                    getActualTempData().add(uniqueData);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

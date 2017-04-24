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
import minioning.common.data.GameData;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jakob
 */
public class Installer extends ModuleInstall {
    
    private static DatagramSocket serverSocket;
    private static List<String[]> tempData;

    public synchronized static List<String[]> getTempData() {
        List<String[]> copyOfTempData = Collections.synchronizedList(new ArrayList<String[]>());;
        for(String[] data : getActualTempData()){
            copyOfTempData.add(data);
        }
        return copyOfTempData;
    }
    
    public static DatagramSocket getServerSocket(){
        if(serverSocket == null){
            try {
                serverSocket = new DatagramSocket(GameData.getPort());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return serverSocket;
    }

    public synchronized static List<String[]> getActualTempData() {
        if (tempData == null) {
            tempData = Collections.synchronizedList(new ArrayList<String[]>());
        }
        return tempData;
    }

    public static void clearTempData() {
        getActualTempData().clear();
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
                DatagramSocket serverSocket = getServerSocket();
                System.out.println("Server running!");
                while (true) {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String simpleData = new String(receivePacket.getData());
                    System.out.println("RECEIVED: " + simpleData);
                    String[] data = simpleData.split(";");
                    String[] uniqueData = new String[data.length + 2];
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    uniqueData[0] = IPAddress.toString().replace("/", ""); //removes the unwanted "/"
                    uniqueData[1] = Integer.toString(port);
                    for (int i = 1; i <= data.length; i++) {
                        uniqueData[i + 1] = data[i - 1].trim();
                    }
                    getActualTempData().add(uniqueData);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

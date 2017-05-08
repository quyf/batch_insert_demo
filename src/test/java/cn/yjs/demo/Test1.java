package cn.yjs.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

public class Test1 {

	public static void main(String[] args) {
		InetAddress netAddress = getInetAddress(); 
		System.out.println( netAddress );
        System.out.println("host ip:" + getHostIp(netAddress));  
        System.out.println("host name:" + getHostName(netAddress));  
        Properties properties = System.getProperties();  
        Set<String> set = properties.stringPropertyNames(); //获取java虚拟机和系统的信息。  
        for(String name : set){  
            System.out.println(name + ":" + properties.getProperty(name));  
        }  
	}
	
	 public static InetAddress getInetAddress(){  
		  
	        try{  
	            return InetAddress.getLocalHost();  
	        }catch(UnknownHostException e){  
	            System.out.println("unknown host!");  
	        }  
	        return null;  
	  
	    }  
	  
	    public static String getHostIp(InetAddress netAddress){  
	        if(null == netAddress){  
	            return null;  
	        }  
	        String ip = netAddress.getHostAddress(); //get the ip address  
	        return ip;  
	    }  
	  
	    public static String getHostName(InetAddress netAddress){  
	        if(null == netAddress){  
	            return null;  
	        }  
	        String name = netAddress.getHostName(); //get the host address  
	        return name;  
	    }  
}

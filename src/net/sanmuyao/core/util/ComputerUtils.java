package net.sanmuyao.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 取本机机器信息
 * @author 杜庆明
 * 2014年12月17日
 */
public class ComputerUtils {

   /**
    * mac地址
    */
   public static String getMAC() {
		String host = getIP();
		String mac = "";
		StringBuffer sb = new StringBuffer();
		try {
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(host));
			byte[] macs = ni.getHardwareAddress();
			for (int i = 0; i < macs.length; i++) {
				mac = Integer.toHexString(macs[i] & 0xFF);
				if (mac.length() == 1) {
					mac = '0' + mac;
				}
				sb.append(mac + "-");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		mac = sb.toString();
		mac = mac.substring(0, mac.length() - 1);
		return mac;
   }
   
   /**
    * 取计算机名称
    * @return
    */
   public static String getHostName() {
	   String hostName = "";
	   Collection<InetAddress> colInetAddress =getAllHostAddress();	   
	   for (InetAddress address : colInetAddress) {
			if (address.isSiteLocalAddress()) {
				hostName = address.getHostName().toString();
				break;
			}
	   }
       return hostName;
   }
   
   /**
    * 取本机ip地址
    * 获取本机IP，排除 127.0.0.1
    * @return
    */
   public static String getIP(){
	   String ip = "";
	   Collection<InetAddress> colInetAddress =getAllHostAddress();	   
	   for (InetAddress address : colInetAddress) {
			if (address.isSiteLocalAddress()) {
				ip = address.getHostAddress();
				break;
			}
	   }
       return ip;
   }
   
   /**
    * 取所有本机ip地址
    * @return
    */
   private static Collection<InetAddress> getAllHostAddress() {
       try {
           Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();   
           Collection<InetAddress> addresses = new ArrayList<InetAddress>();
           while (networkInterfaces.hasMoreElements()) {   
               NetworkInterface networkInterface = networkInterfaces.nextElement();   
               Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();   
               while (inetAddresses.hasMoreElements()) {   
                   InetAddress inetAddress = inetAddresses.nextElement();   
                   addresses.add(inetAddress);   
               }
           }
           return addresses;   
       } catch (SocketException e) {   
           throw new RuntimeException(e.getMessage(), e);   
       }   
   }   
   
   /**
    * 取本机操作系统版本
    * @return
    */
   public static String getOSVersion() {
	   String version = "";
       try {
    	   Properties props=System.getProperties();  
    	   version = props.getProperty("os.version");
       } catch (Exception e) {
           e.printStackTrace();  
       }
       return version;
   }
   /**
    * 取本机操作系统名称
    * @return
    */
   public static String getOSName() {
	   String name = "";
       try{  
    	   Properties props=System.getProperties();  
    	   name = props.getProperty("os.name");
       } catch (Exception e){
           e.printStackTrace();  
       }
       return name;
   }
   
   //java环境  
   public static void all(){  
       Properties props=System.getProperties();  
       System.out.println("Java的运行环境版本："+props.getProperty("java.version"));  
       System.out.println("Java的运行环境供应商："+props.getProperty("java.vendor"));  
       System.out.println("Java供应商的URL："+props.getProperty("java.vendor.url"));  
       System.out.println("Java的安装路径："+props.getProperty("java.home"));  
       System.out.println("Java的虚拟机规范版本："+props.getProperty("java.vm.specification.version"));  
       System.out.println("Java的虚拟机规范供应商："+props.getProperty("java.vm.specification.vendor"));  
       System.out.println("Java的虚拟机规范名称："+props.getProperty("java.vm.specification.name"));  
       System.out.println("Java的虚拟机实现版本："+props.getProperty("java.vm.version"));  
       System.out.println("Java的虚拟机实现供应商："+props.getProperty("java.vm.vendor"));  
       System.out.println("Java的虚拟机实现名称："+props.getProperty("java.vm.name"));  
       System.out.println("Java运行时环境规范版本："+props.getProperty("java.specification.version"));  
       System.out.println("Java运行时环境规范供应商："+props.getProperty("java.specification.vender"));  
       System.out.println("Java运行时环境规范名称："+props.getProperty("java.specification.name"));  
       System.out.println("Java的类格式版本号："+props.getProperty("java.class.version"));  
       System.out.println("Java的类路径："+props.getProperty("java.class.path"));  
       System.out.println("加载库时搜索的路径列表："+props.getProperty("java.library.path"));  
       System.out.println("默认的临时文件路径："+props.getProperty("java.io.tmpdir"));  
       System.out.println("一个或多个扩展目录的路径："+props.getProperty("java.ext.dirs"));  
       System.out.println("操作系统的名称："+props.getProperty("os.name"));  
       System.out.println("操作系统的构架："+props.getProperty("os.arch"));  
       System.out.println("操作系统的版本："+props.getProperty("os.version"));  
       System.out.println("文件分隔符："+props.getProperty("file.separator"));//在 unix 系统中是＂／＂ System.out.println("路径分隔符："+props.getProperty("path.separator"));//在 unix 系统中是＂:＂ System.out.println("行分隔符："+props.getProperty("line.separator"));//在 unix 系统中是＂/n＂ System.out.println("用户的账户名称："+props.getProperty("user.name"));  
       System.out.println("用户的主目录："+props.getProperty("user.home"));  
       System.out.println("用户的当前工作目录："+props.getProperty("user.dir"));  
   }
   
   public static void main(String[] args) {  
	   System.out.println(getHostName());
   }  
}

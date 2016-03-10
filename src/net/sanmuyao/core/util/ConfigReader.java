package net.sanmuyao.core.util;

import java.net.URL;
import java.rmi.RemoteException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 取系统配置信息
 * @author 杜庆明
 *
 */ 
public class ConfigReader
{ 
	private static ConfigReader instance = new ConfigReader();
	public static Configuration config;
 
	/**
	 * 构造配置类
	 */
	private ConfigReader() {
		URL url = ConfigReader.class.getResource("ConfigReader.class");	
		String path = url.getFile();
		int beginIndex = 0;
		int endIndex = path.length() - "classes/com/sinocall/commons/ConfigReader.class".length();
		path = path.substring(beginIndex, endIndex)+ "sys.properties";
		path = path.replace("%20", " ");
		System.out.println("#### 系统配置文件(sys.properties)路径：" + path);		
		try {
			config = new PropertiesConfiguration(path);
		} catch (NestableRuntimeException e) {
			System.out.println("读系统配置文件失败..." + e.getLocalizedMessage());
		} catch (ConfigurationException e) {
			System.out.println("读系统配置文件失败..." + e.getLocalizedMessage());
		}
		
	}

	/**
	 * 取配置属性信息
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return instance.config.getString(key);
	}

	public static void main(String args[]) throws RemoteException, NestableRuntimeException, ConfigurationException {
		
		System.out.println(ConfigReader.get("server.path"));
	}
}

package net.sanmuyao.core.util;

import java.util.Random;
import java.util.UUID;

public class Id {

	/**
	 * 获得UniqId
	 * 
	 */
	public static String getId() {
		String id = UUID.randomUUID().toString();
		id = id.replaceAll("-", "");
		return id;
	}
	
	/**
	 * 获取10以内的随机数
	 * @return
	 */
	public static int getRandomNo() {
		Random r = new Random();
		return r.nextInt(9);
	}
	
}

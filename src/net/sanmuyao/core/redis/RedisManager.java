package net.sanmuyao.core.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * reids工具类
 */
public class RedisManager {
	
	/**
	 * 缓存生存时间
	 * 默认12个小时
	 */
	private static final int expire = 60*60*12;
	
	public static Jedis getJedis() {
		try {
			return RedisContext.getResource();
		} catch(Exception e) {
			System.out.println("========== RedisManager ERROR ==========");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	// 回收连接
	public static void returnJedis(Jedis jedis) {
		RedisContext.jedisPool.returnResource(jedis);
	}
	//回收发生异常后的连接
	public static void returnBrokenJedis(Jedis jedis) {
		if (jedis != null) {
			RedisContext.jedisPool.returnBrokenResource(jedis);
		}
	}
	
	//获取事务jedis
	public static Transaction getTransJedis() {
		Jedis jedis = getJedis();
		return jedis.multi();
	}
	
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	private static byte[] serialize(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	private static Object unserialize(byte[] bytes) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param redisKey
	 * @param value
	 */
	public static void put(RedisKey redisKey, String value) {
		try{
			put(redisKey.getCode(), value, expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	public static void put(String redisKey, String value){
		try{
			put(redisKey, value, expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public static void put(String redisKey, String value, int expire){
		Jedis jedis = null;
		try{
			jedis = getJedis();
			jedis.set(redisKey, value);
			jedis.expire(redisKey, expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	public static void putObject(RedisKey redisKey, Object obj) {
		try {
			putObject(redisKey.getCode(), obj, expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
		}
	}
	
	public static void putObject(String key, Object obj) {
		try {
			putObject(key, obj, expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public static void putObject(String key, Object obj, int expire) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.set(key.getBytes(), serialize(obj));
			jedis.expire(key.getBytes(), expire);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	/**
	 * 将指定redisKey.getCode()的值增加指定量
	 * @param redisKey.getCode()
	 * @param integer
	 */
	public static void incrBy(RedisKey redisKey, int integer) {
		Jedis jedis = getJedis();
		try {
			jedis.incrBy(redisKey.getCode(), integer);			
		} finally {
			if(jedis!=null) returnJedis(jedis);
		}
	}

	public static String get(RedisKey redisKey){
		try {
			return get(redisKey.getCode());
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			return null;
		}
	}
	
	public static String get(String redisKey){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String value = jedis.get(redisKey);
			return value;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	public static Object getObject(RedisKey redisKey) {
			return getObject(redisKey.getCode());
	}
	
	public static Object getObject(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			byte[] person = jedis.get(key.getBytes());
			if(person == null) return null;
			Object object = unserialize(person);
			return object;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	
	/**
	 * 从左端加入
	 * @param redisKey.getCode() list的redisKey.getCode()
	 * @param value 新加的value
	 */
	public static void lpushList(RedisKey redisKey, String value) {
		lpushList(redisKey.getCode(), value);
	}
	
	/**
	 * 从左端加入
	 * @param redisKey.getCode() list的redisKey.getCode()
	 * @param value 新加的value
	 */
	public static void lpushList(String redisKey, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.lpush(redisKey, value);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	
	public static long lengthList(RedisKey redisKey) {
		return lengthList(redisKey.getCode());
	}
	
	public static long lengthList(String redisKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			long value = jedis.llen(redisKey);  
			return value;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return 0;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}


	
	public static List<String> lrange(String redisKey,  long start, long end) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			List<String> list = jedis.lrange(redisKey, start, end);
			return list;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	/**
	 * 取list
	 * @param redisKey
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> lrange(RedisKey redisKey,  long start, long end) {
		return lrange(redisKey.getCode(),  start, end);
	}
	
	public static List<String> lrange(String redisKey) {
		int start = 0;
		long end = RedisManager.lengthList(redisKey);
		return lrange(redisKey,  start, end);
	}
	
	/**
	 * 从右端减出
	 * @param redisKey.getCode()
	 * @return
	 */
	public static String rpopList(RedisKey redisKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String value = jedis.rpop(redisKey.getCode());
			return value;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	/**
	 * 指定index取出
	 * @param redisKey.getCode()
	 * @return
	 */
	public static String lindexList(RedisKey redisKey, long index) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String value = jedis.lindex(redisKey.getCode(), index);
			return value;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	/**
	 * 指定index赋值
	 * @param redisKey.getCode()
	 * @param index
	 * @param value
	 */
	public static void lsetList(RedisKey redisKey, long index, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.lset(redisKey.getCode(), index, value);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	/**
	 * 删除指定index对应的值
	 * @param redisKey.getCode()
	 * @param index
	 */
	public static void ldelList(RedisKey redisKey, long index) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.lset(redisKey.getCode(), index, "__deleted__");
			jedis.lrem(redisKey.getCode(), 0, "__deleted__");
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	public static void remove(RedisKey redisKey){
		remove(redisKey.getCode());
	}
	
	/**
	 * 删除缓存数据
	 * @param redisKey
	 */
	public static void remove(String redisKey){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(redisKey);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	
	public static void removeObject(String redisKey){
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.del(redisKey.getBytes());
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}
	/**
	 * List是否为空
	 * @param redisKey.getCode()
	 * @return
	 */
	public static boolean isListNull(RedisKey redisKey) {
		Jedis jedis = getJedis();
		try{
			if (jedis.exists(redisKey.getCode())) {
				return false;
			}
			long listSize = jedis.llen(redisKey.getCode());
			if (listSize > 0) {
				return false;
			}
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
		return true;
	}

	/**
	 * 从右端加入
	 * @param redisKey.getCode()
	 * @param value
	 */
	public static void rpushList(RedisKey redisKey, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.rpush(redisKey.getCode(), value);
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}


	/**
	 * 根据redisKey.getCode()取数据长度
	 * @param redisKey.getCode()
	 * @return
	 */
	public static long llen(RedisKey redisKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			long l = jedis.llen(redisKey.getCode());
			return l;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return 0;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}

	public static Set<String> smembers(RedisKey redisKey) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Set<String> set = jedis.smembers(redisKey.getCode());
			return set;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}

	/**
	 * 清空所有key
	 */
	public String flushAll() {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String stata = jedis.flushAll();
			return stata;
		} catch (Exception e){
			System.err.println("##########" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		} finally {
			if(jedis!=null) returnJedis(jedis);
	    }
	}


    
}

/**
 * Project Name:kk_core
 * File Name:mongodbHelper.java
 * Package Name:net.sanmuyao.core.db
 * Date:2015年4月8日下午7:00:39
 * Copyright (c) 2015, QuickCRM All Rights Reserved.
 *
*/

package net.sanmuyao.core.db;

import java.util.List;
import java.util.Map;

import net.sanmuyao.core.util.DataMap;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


/**
 * ClassName:mongodbHelper <br/>
 * Function: TODO<br/>
 * Reason:	 TODO<br/>
 * Date:     2015年4月8日 下午7:00:39 <br/>
 * @author  duqingming@126.com
 * @version  v1.0
 * @see 	 
 */
public class MongodbHelper {

	/**
	 * Mongodb操作类
	 */
	protected MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/** 
     * 判断集合是否存在 
     * @param collectionName 
     * @return 
     */  
    public boolean collectionExists(String collectionName) {  
        return mongoTemplate.collectionExists(collectionName);
    }  

	/**
	 * 取表的记录条数
	 * @param collectionName 表名
	 * @return
	 */
    public long getTotalCount(String collectionName) {
		return mongoTemplate.getCollection(collectionName).getCount();
	}
	/**
	 *  取表的记录条数
	 * @param collectionName 表名
	 * @param queryMap 条件
	 * @return
	 * @throws Exception
	 */
    public Long getListCount(String collectionName, QueryMap queryMap) throws Exception {
		Query query = new Query();
		for(String key : queryMap.getQueryMap().keySet()) {
			query.addCriteria(Criteria.where(key).is(queryMap.getQueryMap().get(key)));
	   	 }
		long count = mongoTemplate.count(query, collectionName);
		return count;
	}

	/**
	 * 取列表数据，支持过滤条件
	 * @param collectionName
	 * @param sort
	 * @param dir
	 * @return
	 * @throws Exception
	 */
    public List<DataMap> getList(String collectionName, QueryMap queryMap) throws Exception {
		Query query = new Query();
		for(String key : queryMap.getQueryMap().keySet()) {
	 		query.addCriteria(Criteria.where(key).is(queryMap.getQueryMap().get(key)));
	   	 }
		List<DataMap> list = mongoTemplate.find(query, DataMap.class, collectionName);
		return list;
	}

	/**
	 * 取列表数据，支持排序
	 * @param collectionName 表名
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @return
	 * @throws Exception
	 */
    public List<DataMap> getList(String collectionName, String sidx, String sord) throws Exception {
		throw new Exception("方法未实现");
	}

	/**
	 *  取列表数据，支持条件查询，排序
	 * @param collectionName 要取的数据的表名
	 * @param searchList：取数据的过滤条件。
	 * 一个Map一个条件，最外层Map的key值表示要检索的字段，对应的子Map为条，其中包括操作符 “opt” 和值 “value”，opt 默认值为“=”，value 默认值为null
	 *
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @return
	 * @throws Exception
	 */
    public List<Map<String, Object>> getList(String collectionName, QueryMap queryMap, String sidx, String sord) throws Exception {
		throw new Exception("方法未实现");
	}

	/**
	 * 取列表数据，支持查询，排序，分页
	 * @param collectionName 要查询的表名
	 * @param columns 要查询的列
	 * @param searchList 查询条件
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @param start 取数据的起始索引
	 * @param limit 取数据条数
	 * @return
	 * @throws Exception
	 */
    public List<Map<String, Object>> getList(String collectionName, String columns, QueryMap queryMap, String sidx, String sord, long start, long limit) throws Exception {
		throw new Exception("方法未实现");
	}

	/**
	 * 取列表数据，支持查询，排序，分页
	 * @param collectionName 要查询的表名
	 * @param searchList 查询条件
	 * @param sort 排序字段
	 * @param dir 排序方式 desc asc
	 * @param start 取数据的起始索引
	 * @param limit 取数据条数
	 * @return
	 * @throws Exception
	 */
    public List<Map<String, Object>> getList(String collectionName, QueryMap queryMap, String sidx, String sord, long start, long limit) throws Exception {
		throw new Exception("方法未实现");
	}

	/**
	 * 添加一条记录，记录的字段根据dataMap中的key取，因此dataMap的key必需和表中的字段保持一致
	 * @param collectionName
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
    public void add(String collectionName, Map<String, Object> dataMap) throws Exception {
		mongoTemplate.insert(dataMap, collectionName);
	}



	/**
	 * 更新记录信息
	 * @param entityMap
	 * @throws Exception
	 */
    public long update(String collectionName, String keyFieldName, DataMap dataMap, Object keyFieldValue) throws Exception {
		Criteria criteria = Criteria.where(keyFieldName).in(keyFieldValue);
		DataMap dm = null;
        if(criteria == null){
             Query query = new Query(criteria);
             dm = mongoTemplate.findOne(query, DataMap.class);
             if(dm != null) {
            	 Update update = new Update();
            	 for(String key : dataMap.keySet()) {
            		 update.addToSet(key, dataMap.get(key));
            	 }
            	 mongoTemplate.updateFirst(query, update, collectionName);
             }
        }
        return 1;
    }

	/**
	 * 根据主健获取数据
	 * @param collectionName
	 * @param keyFieldName
	 * @param id
	 * @return
	 */
    public Map<String, Object> getMap(String collectionName, String keyFieldName, Object keyFieldValue) {
		Criteria criteria = Criteria.where(keyFieldName).in(keyFieldValue);
		DataMap dataMap = null;
        if(criteria == null){
             Query query = new Query(criteria);
             dataMap = mongoTemplate.findOne(query, DataMap.class);
        }
        return dataMap;
	}


	/**
	 * 删除记录
	 * @param collectionName 要删除的记录所有的表
	 * @param keyFieldName 要删除的记录的主键
	 * @param keyFieldValue 要删除的记录的主键id值
	 * @return
	 * @throws Exception
	 */
    public long delete(String collectionName, String keyFieldName, Object keyFieldValue) throws Exception {
		Criteria criteria = Criteria.where(keyFieldName).is(keyFieldValue);
        Query query = new Query();
        query.addCriteria(criteria);
        DataMap dataMap = mongoTemplate.findOne(query, DataMap.class);
        if(dataMap != null) {
            mongoTemplate.remove(dataMap, collectionName);
            return 1;
        }
        return 0;
	}

	/**
	 * 清空表，删除表中所有数据
	 * 执行非常危险
	 * @param collectionName 表名
	 * @return
	 */
    public long clearData(String collectionName) {
		List<DataMap> list = null;
		list = mongoTemplate.findAll(DataMap.class, collectionName);
		long count = 0;
        if(list != null) {
           for (DataMap map : list) {
        	   mongoTemplate.remove(map);
        	   count++;
           }
        }
        return count;
	}

}


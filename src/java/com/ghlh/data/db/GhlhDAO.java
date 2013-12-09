package com.ghlh.data.db;

import java.util.List;

import org.apache.log4j.Logger;

import com.common.db.DBAgentOO;

public class GhlhDAO {
	private static Logger logger = Logger.getLogger(GhlhDAO.class);

	public static void create(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.insert(object);
		} catch (Exception ex) {
			logger.error("insert " + object + " throw", ex);
		}
	}

	public static void edit(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.update(object);
		} catch (Exception ex) {
			logger.error("update " + object + " throw", ex);
		}
	}

	public static Object get(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		Object result = null;
		try {
			result = dbAgentOO.select(object);
		} catch (Exception ex) {
			logger.error("get " + object + " throw", ex);
		}
		return result;
	}

	public static void remove(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.delete(object);
		} catch (Exception ex) {
			logger.error("get " + object + " throw", ex);
		}
	}

	public static List list(String sql, String className) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectData(sql, className);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

	public static List list(String sql, String className, int startPos, int size) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectData(sql, className, startPos, size);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

}

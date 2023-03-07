package cn.alvasw.plugin;

import cn.alvasw.commons.PageUtil;
import cn.alvasw.entity.Page;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
@Intercepts(
		@Signature(
				type = StatementHandler.class,
				method = "prepare",
				args = {Connection.class, Integer.class}
		)
)
public class PagePlugin implements Interceptor {

	private static final String SELECT = "select";
	private static final String LIMIT  = " limit ";
	private static final String END    = ";";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

		// 判断是否需要进行分页操作
		Page page;
		if ((page = PageUtil.getPage()) == null) {
			return invocation.proceed();
		}

		String sql = statementHandler.getBoundSql().getSql().toLowerCase(Locale.ROOT);

		// is select?
		if (!sql.startsWith(SELECT)) {
			return invocation.proceed();
		}

		// is limited?
		if (sql.lastIndexOf(LIMIT) != -1) {
			return invocation.proceed();
		}

		// 准备分页 SQL
		int count;
		page.setPageMax((count = count(sql, invocation)) % page.getPageSize() == 0 ?
				count / page.getPageSize() :
				count / page.getPageSize() + 1);

		if (sql.endsWith(END)) {
			sql = sql.substring(0, sql.length() - 1);
		}

		sql += LIMIT + ((page.getPageNum() - 1) * page.getPageSize() + "," + page.getPageSize());
		SystemMetaObject.forObject(statementHandler.getBoundSql()).setValue("sql", sql);
		return invocation.proceed();
	}

	private int count(String sql, Invocation invocation) {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		Connection       connection       = (Connection) invocation.getArgs()[0];

		int    count    = 0;
		String countSql = "select count(*) as total" + sql.substring(sql.indexOf(" from "));

		PreparedStatement ps = null;
		ResultSet         resultSet;
		try {
			ps = connection.prepareStatement(countSql);
			// 使用 statementHandler 自身参数设置来设置参数
			statementHandler.parameterize(ps);
			resultSet = ps.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}

		return count;
	}


}

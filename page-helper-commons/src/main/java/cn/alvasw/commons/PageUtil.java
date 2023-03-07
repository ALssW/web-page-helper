package cn.alvasw.commons;

import cn.alvasw.entity.Page;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
public class PageUtil {

	private static final ThreadLocal<Page> LOCAL_PAGE = new ThreadLocal<>();

	public static void setPage(int pageNum, int pageSize) {
		LOCAL_PAGE.set(new Page(pageNum, pageSize));
	}

	public static Page getPage() {
		return LOCAL_PAGE.get();
	}

	public static void clear() {
		LOCAL_PAGE.remove();
	}

	public static Page getAndRemove() {
		Page page = getPage();
		clear();
		return page;
	}

}

package cn.alvasw.plugin.mybatis.entity;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
public class PageResult {

	private Page page;

	private String msg;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

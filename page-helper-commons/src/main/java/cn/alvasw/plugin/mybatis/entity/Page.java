package cn.alvasw.plugin.mybatis.entity;

/**
 * @author ALsW
 * @version 1.0.0
 * @date 2023-03-07
 */
public class Page {

	/**
	 * 当前页数
	 */
	private Integer pageNum;
	/**
	 * 每一页的数据条数
	 */
	private Integer pageSize;
	/**
	 * 总共的数据条数
	 */
	private Integer pageMax;

	public Page() {
	}

	public Page(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageMax() {
		return pageMax;
	}

	public void setPageMax(Integer pageMax) {
		this.pageMax = pageMax;
	}
}

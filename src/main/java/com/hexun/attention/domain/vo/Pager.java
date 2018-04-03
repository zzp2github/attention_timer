package com.hexun.attention.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 描述：分页信息
 *
 * @author zhangyang@staff.hexun.com
 * @date 2017年8月9日
 * @version v1.0
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Pager {

	/** 当前页 */
	@ApiModelProperty(notes = "当前页")
	@JSONField(name = "current_page")
	private Integer currentPage = 1;
	
	/** 每页记录数 */
	@ApiModelProperty(notes = "每页记录数")
	@JSONField(name = "page_size")
	private Integer pageSize = 10;
	
	/** 总记录数 */
	@ApiModelProperty(notes = "总记录数")
	@JSONField(name = "total_rows")
	private Integer totalRows;
	
	/** 总页数 */
	@ApiModelProperty(notes = "总页数")
	@JSONField(name = "total_pages")
	private Integer totalPages;
	
	public Pager() {
		
	}
	
	public Pager(Integer currentPage, Integer pageSize, Integer totalRows) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getTotalPages() {
		if (getTotalRows() != null) {
			if (getTotalRows() % getPageSize() == 0) {
				totalPages = getTotalRows() / getPageSize();
			} else {
				totalPages = getTotalRows() / getPageSize() + 1;
			}
		}
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
}

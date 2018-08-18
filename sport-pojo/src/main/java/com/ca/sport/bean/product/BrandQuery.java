package com.ca.sport.bean.product;

public class BrandQuery {
    /**
     * 品牌ID
     */
    private Long id;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片URL
     */
    private String imgUrl;
    /**
     * 排序  越大越靠前
     */
    private Integer sort;

    /**
     * 是否可用   0 不可用 1 可用
     */
    private Integer isDisplay;




    private Integer pageNo = 1;

    private Integer pageSize = 10;

    /**
     * 开始行
     */
    private Integer startRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.startRow = (pageNo - 1) * pageSize;
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.startRow = (pageNo - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        return startRow;
    }

}



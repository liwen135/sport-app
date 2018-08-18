package com.ca.sport.bean;


import com.ca.sport.bean.product.Sku;

import java.io.Serializable;

/**
 * 购物项
 *
 * @author lx
 */
public class BuyerItem implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //1:SKUID   Sku对象里面有个自己的ID
    private Sku sku;

    //2：Boolean 是否有货;
    private Boolean isHave = true;

    //3:数量
    private Integer amount = 1;

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Boolean getIsHave() {
        return isHave;
    }

    public void setIsHave(Boolean isHave) {
        this.isHave = isHave;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BuyerItem [sku=" + sku + ", isHave=" + isHave + ", amount=" + amount + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)  // 比较地址
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass()) //class  cn.itcast.core.bean.BuyerItem
            return false;
        BuyerItem other = (BuyerItem) obj;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.getId().equals(other.sku.getId()))
            return false;
        return true;
    }


}

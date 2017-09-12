package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-08.
 */

public class ProductOrderInfoEntity implements Serializable {

    private String PoCode; //生产单
    private String PatternNo; //款号
    private String Customer; //顾客
    private String Color; //颜色
    private String Size; //尺码
    private String Amount; //数量

    public String getPoCode() {
        return PoCode;
    }

    public void setPoCode(String poCode) {
        PoCode = poCode;
    }

    public String getPatternNo() {
        return PatternNo;
    }

    public void setPatternNo(String patternNo) {
        PatternNo = patternNo;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}

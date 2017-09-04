package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-31.
 */

public class SiteEntity implements Serializable {
    private String siteNum; //站点
    private String processNumber; //工序编号
    private String processName; //工序名称
    private String standardHour; //标准工时
    private String processPrice; //工序单价
    private String todayPlanNumber; //今日计划数
    private String todayCompanyNumber; //今日完成数
    private String todayPlanSalary; //今日计划工资
    private String todayGotSalary; //今日已拿工资

    private String videoUrl;
    private int bookUrl;

    public String getSiteNum() {
        return siteNum;
    }

    public void setSiteNum(String siteNum) {
        this.siteNum = siteNum;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String rocessNumber) {
        this.processNumber = rocessNumber;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStandardHour() {
        return standardHour;
    }

    public void setStandardHour(String standardHour) {
        this.standardHour = standardHour;
    }

    public String getProcessPrice() {
        return processPrice;
    }

    public void setProcessPrice(String processPrice) {
        this.processPrice = processPrice;
    }

    public String getTodayPlanNumber() {
        return todayPlanNumber;
    }

    public void setTodayPlanNumber(String todayPlanNumber) {
        this.todayPlanNumber = todayPlanNumber;
    }

    public String getTodayCompanyNumber() {
        return todayCompanyNumber;
    }

    public void setTodayCompanyNumber(String todayCompanyNumber) {
        this.todayCompanyNumber = todayCompanyNumber;
    }

    public String getTodayPlanSalary() {
        return todayPlanSalary;
    }

    public void setTodayPlanSalary(String todayPlanSalary) {
        this.todayPlanSalary = todayPlanSalary;
    }

    public String getTodayGotSalary() {
        return todayGotSalary;
    }

    public void setTodayGotSalary(String todayGotSalary) {
        this.todayGotSalary = todayGotSalary;
    }

    public int getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(int bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

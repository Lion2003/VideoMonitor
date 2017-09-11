package videomonitor.videomonitor.entity;

/**
 * 缝纫机信息实体类
 * Created by Administrator on 2017-09-08.
 */

public class SewingInfoEntity {

    private String Code; //设备编号
    private String ModelNo; //型号
    private String ThisBootTime; //本次开机时间
    private String SumBootTime; //累计开机时间
    private String PinNum; //本次开机针数
    private String SumPinNum; //累计开机针数
    private String CutLineNum; //本次切线数
    private String SumCutLineNum; //累计切线数
    private String PresserNum; //本次压脚抬起数
    private String SumPresserNum; //累计压脚抬起数
    private String Speed; //当前转速

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getModelNo() {
        return ModelNo;
    }

    public void setModelNo(String modelNo) {
        ModelNo = modelNo;
    }

    public String getThisBootTime() {
        return ThisBootTime;
    }

    public void setThisBootTime(String thisBootTime) {
        ThisBootTime = thisBootTime;
    }

    public String getSumBootTime() {
        return SumBootTime;
    }

    public void setSumBootTime(String sumBootTime) {
        SumBootTime = sumBootTime;
    }

    public String getPinNum() {
        return PinNum;
    }

    public void setPinNum(String pinNum) {
        PinNum = pinNum;
    }

    public String getSumPinNum() {
        return SumPinNum;
    }

    public void setSumPinNum(String sumPinNum) {
        SumPinNum = sumPinNum;
    }

    public String getCutLineNum() {
        return CutLineNum;
    }

    public void setCutLineNum(String cutLineNum) {
        CutLineNum = cutLineNum;
    }

    public String getSumCutLineNum() {
        return SumCutLineNum;
    }

    public void setSumCutLineNum(String sumCutLineNum) {
        SumCutLineNum = sumCutLineNum;
    }

    public String getPresserNum() {
        return PresserNum;
    }

    public void setPresserNum(String presserNum) {
        PresserNum = presserNum;
    }

    public String getSumPresserNum() {
        return SumPresserNum;
    }

    public void setSumPresserNum(String sumPresserNum) {
        SumPresserNum = sumPresserNum;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }
}

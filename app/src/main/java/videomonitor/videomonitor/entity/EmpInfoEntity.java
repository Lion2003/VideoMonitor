package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-13.
 */
//     <EmpInfo>
//        <CardNo>4990476</CardNo>
//        <EmpCode>BL00001</EmpCode>
//        <LineCode>03</LineCode>
//        <LineName>小组</LineName>
//        <Name>张桂山</Name>
//     </EmpInfo>
public class EmpInfoEntity implements Serializable {
    private String CardNo;
    private String EmpCode;
    private String LineCode;
    private String LineName;
    private String Name;

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getLineCode() {
        return LineCode;
    }

    public void setLineCode(String lineCode) {
        LineCode = lineCode;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }
}

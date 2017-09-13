package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-13.
 */

public class EmpInfoEntity implements Serializable {
    private String EmpCode;
    private String Name;
    private String Sex;

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

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }
}

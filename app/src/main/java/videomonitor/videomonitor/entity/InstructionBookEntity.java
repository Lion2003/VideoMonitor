package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-05.
 */

public class InstructionBookEntity implements Serializable {
    private String technologyId;  //工艺编号
    private String technologyName;  //工艺名称
    private int src;

    public String getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(String technologyId) {
        this.technologyId = technologyId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }
}

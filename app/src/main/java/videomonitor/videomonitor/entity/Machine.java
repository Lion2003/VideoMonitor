package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-22.
 */

public class Machine implements Serializable {
    private int type;
    private String deviceNo;

    public Machine(int type, String deviceNo) {
        this.type = type;
        this.deviceNo = deviceNo;
    }
}

package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-22.
 */

public class LockState implements Serializable {
    private int type;
    private int isLock;
    private String deviceNo;

    public LockState(int type, int isLock, String deviceNo) {
        this.type = type;
        this.isLock = isLock;
        this.deviceNo = deviceNo;
    }
}

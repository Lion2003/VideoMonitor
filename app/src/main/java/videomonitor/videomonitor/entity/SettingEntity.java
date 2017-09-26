package videomonitor.videomonitor.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-09-26.
 */

public class SettingEntity implements Serializable {

    private String productorderId, edColor, edSize;
    private int type;
    private String frjId;
    private String siteId;
    private String url, fushanUrl;
    private String time;

    public String getProductorderId() {
        return productorderId;
    }

    public void setProductorderId(String productorderId) {
        this.productorderId = productorderId;
    }

    public String getEdColor() {
        return edColor;
    }

    public void setEdColor(String edColor) {
        this.edColor = edColor;
    }

    public String getEdSize() {
        return edSize;
    }

    public void setEdSize(String edSize) {
        this.edSize = edSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrjId() {
        return frjId;
    }

    public void setFrjId(String frjId) {
        this.frjId = frjId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFushanUrl() {
        return fushanUrl;
    }

    public void setFushanUrl(String fushanUrl) {
        this.fushanUrl = fushanUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

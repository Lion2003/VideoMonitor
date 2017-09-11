package videomonitor.videomonitor.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-09-08.
 */

public class SiteInfoEntity implements Serializable {

    /**
     * StationCode : 13
     * ProcessData : [{"ProcessCode":"70045","ProcessName":"暗合门襟腰头挂钩垫布连翻门襟腰头","StandardHour":45,"UnitPrice":0.35,"PlanNumber":850,"FinishNumber":650,"PlanWages":297.5,"TakenWages":227.5},{"ProcessCode":"39047","ProcessName":"暗合门襟腰头挂钩垫布连翻门襟腰头2","StandardHour":45,"UnitPrice":0.35,"PlanNumber":550,"FinishNumber":450,"PlanWages":297.5,"TakenWages":227.5},{"ProcessCode":"50345","ProcessName":"暗合门襟腰头挂钩垫布连翻门襟腰头3","StandardHour":45,"UnitPrice":0.35,"PlanNumber":800,"FinishNumber":600,"PlanWages":297.5,"TakenWages":227.5}]
     */

    private String StationCode;
    private List<ProcessDataBean> ProcessData;

    public String getStationCode() {
        return StationCode;
    }

    public void setStationCode(String StationCode) {
        this.StationCode = StationCode;
    }

    public List<ProcessDataBean> getProcessData() {
        return ProcessData;
    }

    public void setProcessData(List<ProcessDataBean> ProcessData) {
        this.ProcessData = ProcessData;
    }

    public static class ProcessDataBean implements Serializable {
        /**
         * ProcessCode : 70045
         * ProcessName : 暗合门襟腰头挂钩垫布连翻门襟腰头
         * StandardHour : 45
         * UnitPrice : 0.35
         * PlanNumber : 850
         * FinishNumber : 650
         * PlanWages : 297.5
         * TakenWages : 227.5
         */

        private String ProcessCode;
        private String ProcessName;
        private String StandardHour;
        private String UnitPrice;
        private String PlanNumber;
        private String FinishNumber;
        private String PlanWages;
        private String TakenWages;
        private int bookUrl;
        private String videoUrl;

        public String getProcessCode() {
            return ProcessCode;
        }

        public void setProcessCode(String ProcessCode) {
            this.ProcessCode = ProcessCode;
        }

        public String getProcessName() {
            return ProcessName;
        }

        public void setProcessName(String ProcessName) {
            this.ProcessName = ProcessName;
        }

        public String getStandardHour() {
            return StandardHour;
        }

        public void setStandardHour(String StandardHour) {
            this.StandardHour = StandardHour;
        }

        public String getUnitPrice() {
            return UnitPrice;
        }

        public void setUnitPrice(String UnitPrice) {
            this.UnitPrice = UnitPrice;
        }

        public String getPlanNumber() {
            return PlanNumber;
        }

        public void setPlanNumber(String PlanNumber) {
            this.PlanNumber = PlanNumber;
        }

        public String getFinishNumber() {
            return FinishNumber;
        }

        public void setFinishNumber(String FinishNumber) {
            this.FinishNumber = FinishNumber;
        }

        public String getPlanWages() {
            return PlanWages;
        }

        public void setPlanWages(String PlanWages) {
            this.PlanWages = PlanWages;
        }

        public String getTakenWages() {
            return TakenWages;
        }

        public void setTakenWages(String TakenWages) {
            this.TakenWages = TakenWages;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getBookUrl() {
            return bookUrl;
        }

        public void setBookUrl(int bookUrl) {
            this.bookUrl = bookUrl;
        }
    }
}

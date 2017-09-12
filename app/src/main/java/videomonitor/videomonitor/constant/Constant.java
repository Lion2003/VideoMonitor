package videomonitor.videomonitor.constant;

/**
 * Created by Administrator on 2017-09-08.
 */

public class Constant {
    public static String baseUrl = "http://112.124.63.137:8016";
    public static String productOrderInfoUrl = baseUrl + "/api/po/"; //生产单信息
    public static String sweingInfoUrl = baseUrl + "/api/Sartorius/"; //缝纫机信息
    public static String siteInfoUrl = baseUrl + "/api/Station/"; //站点信息


    public static String productOrderCache = "productOrderCache"; //获取生产单信息缓存
    public static String sewingInfoCache = "sewingInfoCache"; //缝纫机信息缓存
    public static String siteInfoCache = "siteInfoCache"; //站点信息缓存

}

package videomonitor.videomonitor;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import videomonitor.videomonitor.entity.ImageInfo;

/**
 * Created by Administrator on 2017-09-12.
 */

public class ImageUtils {

    /**
     * 获取指定路径中的视频文件
     * @param list 装扫描出来的视频文件实体类
     * @param file 指定的文件
     */
    public static List<ImageInfo> getImageFile(final List<ImageInfo> list, File file) {// 获得视频文件
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // sdCard找到视频名称
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);//获取文件后缀名
                    if (name.equalsIgnoreCase(".png")  //忽略大小写
                            || name.equalsIgnoreCase(".jpg")
                            || name.equalsIgnoreCase(".bmp")) {
                        ImageInfo vi = new ImageInfo();
                        vi.displayName = file.getName();//文件名
                        vi.filePath = file.getAbsolutePath();//文件路径
                        list.add(vi);
                        Log.i("tag","文件名:"+vi.displayName+",路径:"+vi.filePath);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getImageFile(list, file);
                }
                return false;
            }
        });
        return list;
    }

    public static String checkContainImage(List<ImageInfo> list, String id) {
        String path = "";
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).displayName.contains(id)) {
//                if(list.get(i).displayName.split(".")[0].equals(id)) {
                    path = list.get(i).filePath;
                    break;
//                }
            } else {
                path = "";
            }
        }
        return path;
    }

}

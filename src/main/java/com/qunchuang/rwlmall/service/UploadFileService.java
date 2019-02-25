package com.qunchuang.rwlmall.service;

import com.qiniu.util.StringMap;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    /**
     * 上传图片
     *
     * @param image
     * @return
     */
    String uploadImage(MultipartFile image);

    /**
     * 七牛云上传图片
     *
     * @param image
     * @return
     */
    String qiNiuUploadImage(MultipartFile image);

    /**
     * 删除文件
     *
     * @param path
     */
    void deleteFile(String path);

    /**
     * 生成上传token
     *
     * @return 生成的上传token
     */
    String uploadToken();

}

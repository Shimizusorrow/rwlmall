package com.qunchuang.rwlmall.service.impl;


import com.qiniu.util.StringMap;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;
import com.qunchuang.rwlmall.service.UploadFileService;
import com.qunchuang.rwlmall.utils.FileUtil;
import com.qunchuang.rwlmall.utils.KeyUtil;
import com.qunchuang.rwlmall.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Value("${imagepath}")
    String imagepath;

    @Value("${imageurl}")
    String returnurl;

    @Value("$qiniuimageurl")
    String qiniuimageurl;


    @Override
    public String uploadImage(MultipartFile image) {
        String fileName = KeyUtil.genUniqueKey() + image.getOriginalFilename();

        try {
            FileUtil.uploadFile(image.getBytes(), imagepath, fileName);
        } catch (Exception e) {
            throw new RwlMallException(ResultExceptionEnum.FILE_UPLOAD_FAILURE);
        }

        /*不返回前缀*/
        //return returnurl + fileName;
        return fileName;
    }

    @Override
    public String qiNiuUploadImage(MultipartFile image) {
        String fileName = KeyUtil.genUniqueKey() + image.getOriginalFilename();
        try {
            QiNiuUtil.uploadFile(image.getBytes(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*不返回前缀*/
        //return qiniuimageurl+fileName;
        return fileName;
    }

    @Override
    public String uploadToken() {
        return QiNiuUtil.uploadToken();
    }

    @Override
    public void deleteFile(String path) {
        File file = new File(path);
        if (!file.delete()){
            throw new RwlMallException(ResultExceptionEnum.FILE_DELETION_FAILURE);
        }

    }

}

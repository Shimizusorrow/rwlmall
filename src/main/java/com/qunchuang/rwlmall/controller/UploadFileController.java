package com.qunchuang.rwlmall.controller;

import com.qunchuang.rwlmall.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RequestMapping("/file")
public class UploadFileController {

    @Value("${imagepath}")
    String imagepath;

    @Autowired
    private UploadFileService uploadFileService;

    /*上传图片*/
    @PostMapping("/uploadimage")
    public Object uploadImage(MultipartFile file) {
        return uploadFileService.qiNiuUploadImage(file);
    }


    /*删除图片*/
    @PostMapping("/deleteimage")
    public Object deleteImage(@RequestParam("imagename") String imageName) {
        uploadFileService.deleteFile(imagepath+imageName);
        return null;
    }

    /*返回上传凭证*/
    @RequestMapping("/uploadtoken")
    public Object uploadToken(){
        return uploadFileService.uploadToken();
    }
}

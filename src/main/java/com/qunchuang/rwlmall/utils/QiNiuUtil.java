package com.qunchuang.rwlmall.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.exception.RwlMallException;

import java.io.ByteArrayInputStream;

/**
 * @author Curtain
 * @date 2018/5/18 14:27
 */
public class QiNiuUtil {

    private final static String ACCESS_KEY = "b6IcBIRJwW-RvOK6qggyNnbPZCN_ePOnaTvSV7m-";
    private final static String SECRET_KEY = "UHijfMiiMerLlAXma0KNJ9vCQJOF2OVnmDEW2P6_";
    private final static String BUCKET = "rwlmall";

    public static String uploadFile(byte[] file, String filename) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        byte[] uploadBytes = file;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        String upToken = uploadToken();
        try {
            Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            return putRet.key;
        } catch (QiniuException ex) {
            throw new RwlMallException(ResultExceptionEnum.FILE_UPLOAD_FAILURE);
        }

    }

    public static String uploadToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(BUCKET);
        return upToken;
    }

}

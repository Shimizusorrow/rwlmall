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
 * 可以修改为使用服务器上传的方式  //2019-2-25
 * @author Curtain
 * @date 2018/5/18 14:27
 */
public class QiNiuUtil {

    private final static String ACCESS_KEY = "b6IcBIRJwW-RvOK6qggyNnbPZCN_ePOnaTvSV7m-";
    private final static String SECRET_KEY = "UHijfMiiMerLlAXma0KNJ9vCQJOF2OVnmDEW2P6_";
    private final static String BUCKET = "rwlmall";

    public static String uploadFile(byte[] file, String filename) {

        //代码已删除
        return null;
    }

    public static String uploadToken() {
        //代码已删除
        return null;
    }

}

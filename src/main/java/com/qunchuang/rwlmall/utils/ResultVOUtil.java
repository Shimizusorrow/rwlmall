package com.qunchuang.rwlmall.utils;


import com.qunchuang.rwlmall.enums.ResultExceptionEnum;
import com.qunchuang.rwlmall.vo.ResultVO;

import java.util.Enumeration;

public class ResultVOUtil {


    public static ResultVO success(Object object) {
        ResultVO resultVO =new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
       return success(null);
    }


    public static ResultVO error(String msg){
        ResultVO resultVO =new ResultVO();
        resultVO.setCode(1);
        resultVO.setMsg(msg);
        return resultVO;
    }

    public static ResultVO error(ResultExceptionEnum resultExceptionEnum){
        ResultVO resultVO =new ResultVO();
        resultVO.setCode(resultExceptionEnum.getCode());
        resultVO.setMsg(resultExceptionEnum.getMessage());
        return resultVO;
    }


}

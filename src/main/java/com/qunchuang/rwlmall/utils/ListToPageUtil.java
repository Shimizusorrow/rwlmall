package com.qunchuang.rwlmall.utils;

import com.qunchuang.rwlmall.bean.MyPage;
import com.qunchuang.rwlmall.bean.RoleVO;

import java.util.ArrayList;
import java.util.List;

/**
 * List转化为Page
 *
 * @author Curtain
 * @date 2018/7/6 9:30
 */
public class ListToPageUtil {

    public static <T> MyPage convert(List<T> list, Integer page, Integer size) {
        MyPage myPage = new MyPage();
        myPage.setTotalSize(list.size());
        myPage.setCurrentPage(page);
        myPage.setTotalPage((int) Math.ceil(Double.valueOf(list.size()) / Double.valueOf(size + 1)));

        Integer startIndex = (page - 1) * size;

        Integer endIndex = startIndex + size;

        List<T> result = new ArrayList<>();

        if (startIndex >= list.size()) {
            myPage.setObject(null);
            return myPage;
        }

        if (endIndex > list.size()) {
            endIndex = list.size();
        }

        for (int i = startIndex; i < endIndex; i++) {
            result.add(list.get(i));
        }

        myPage.setObject(result);

        return myPage;
    }
}

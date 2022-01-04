package com.lry.util;

/**
 * @author 刘汝杨
 */
public class PageSupport {
    public int maxPagesNums(int total, int pageSize) {
        //初始值设置为1，如果查询到的用户数为0，则默认为1页，否则到后面执行分页查询sql语句的时候，会出现在负条数之后进行查询（BUG）
        int result = 1;
        if (total > 0 && pageSize > 0) {
            if (total % pageSize == 0) {
                result = total / pageSize;
            } else {
                result = total / pageSize + 1;
            }
        }
        return result;
    }
}

package com.sinjee.filter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 创建时间 2020 - 01 -19
 * 防止XSS攻击 宽松
 * @author kweitan
 */
public class XssOtherHttpServletRequestWrapper extends HttpServletRequestWrapper {
    HttpServletRequest request;

    public XssOtherHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        String value = request.getParameter(name);
        System.out.println("name:" + name + "," + value);
        if (!StringUtils.isEmpty(value)) {
            // 转换Html
            value = StringEscapeUtils.escapeHtml4(value);
        }
        return value;
    }
}

package com.sinjee.filter;

import com.sinjee.common.GsonUtil;
import com.sinjee.common.ResultVOUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 创建时间 2020 - 01 -19
 * XSS过滤器 使用严格的过滤器
 * @author kweitan
 */
public class XssFilter implements Filter {
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = "GET";
        String param = "";
        XssHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = this.getBodyString(xssRequest.getReader());
            if(StringUtils.isNotBlank(param)){
                if(xssRequest.checkXSSAndSql(param)){
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(GsonUtil.getInstance().toStr(ResultVOUtil.error(101,"您所访问的页面请求中有违反安全规则元素存在，拒绝访问!")));
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write(GsonUtil.getInstance().toStr(ResultVOUtil.error(102,"您所访问的页面请求中有违反安全规则元素存在，拒绝访问!")));
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    // 获取request请求body中参数
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;

    }
}

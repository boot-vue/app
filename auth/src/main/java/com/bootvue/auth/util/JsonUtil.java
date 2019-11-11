package com.bootvue.auth.util;

import com.alibaba.fastjson.JSON;
import com.bootvue.common.result.Result;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * response 响应
 */
public class JsonUtil {

    public static void write(HttpServletResponse response, Result data) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharsetUtil.UTF_8.toString());

        response.getWriter().write(JSON.toJSONString(data));
    }
}

package com.bootvue.auth.util;

import com.bootvue.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.netty.util.CharsetUtil;
import org.apache.http.entity.ContentType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * response 响应
 */
public class ResponseUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public static void write(HttpServletResponse response, Result data) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setCharacterEncoding(CharsetUtil.UTF_8.toString());
        PrintWriter writer = response.getWriter();
        writer.write(MAPPER.writeValueAsString(data));
        writer.flush();
    }
}

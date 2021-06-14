package com.twitch.twitchgame.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitch.twitchgame.entity.Item;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ServletUtil {
    // <T> is the generics.
    public static <T> void writeData(HttpServletResponse response,
                                     T data)
            throws IOException {
        response.setContentType("application/json; charset = UTF-8");
        response.getWriter().print (
                new ObjectMapper().writeValueAsString(data)
        );
    }

    // Help encrypt user password before saving to the database.
    // this is to use salt to add userId + password. then md5.
    public static String encryptPassword(String userId, String password)
        throws IOException {
        return DigestUtils.md5Hex(
                userId + DigestUtils.md5Hex(password)
        ).toLowerCase();
    }

    public static <T> T readRequestBody(Class<T> cl, HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(request.getReader(), cl);
        } catch (JsonParseException | JsonMappingException e) {
            return null;
        }
    }
}

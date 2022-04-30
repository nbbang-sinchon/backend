package nbbang.com.nbbang.global.support.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static nbbang.com.nbbang.global.support.controller.ControllerUtilMessage.*;

/**
 * An Utility for controller Mock tests
 * Designed to support DefaultResponse class
 *
 * Example:
 *     Map data = expectMapData(
 */

public class ControllerTestUtil {

    public Map expectMapData(MvcResult res) throws Exception {
        try {
            DefaultResponse resp = expectDefaultResponseObject(res);
            return extractMapData(resp);
        } catch (Exception e) {
            throw new IllegalArgumentException(ILLEGAL_RESPONSE);
        }
    }

    public Map expectMapData(DefaultResponse res) throws Exception {
        return extractMapData(res);
    }

    public <T> Map expectMapData(ResponseEntity<T> responseEntity) throws Exception {
        DefaultResponse res = (DefaultResponse) responseEntity.getBody();
        return extractMapData(res);
    }

    protected Map extractMapData(DefaultResponse res) {
        try {
            Map data = (Map) res.getData();
            return data;
        } catch (Exception e) {
            throw new IllegalArgumentException(ILLEGAL_DEFAULT_RESPONSE);
        }
    }

    public DefaultResponse expectDefaultResponseObject(MvcResult res) throws Exception {
        return extractDefaultResponse(res);
    }

    public DefaultResponse expectDefaultResponseObject(ResponseEntity responseEntity) {
        DefaultResponse res = (DefaultResponse) responseEntity.getBody();
        return res;
    }

    protected DefaultResponse extractDefaultResponse(MvcResult res) throws Exception {
        if (res == null) {
            return null;
        }
        String json = res.getResponse().getContentAsString();
        try {
            return new ObjectMapper().readValue(json, DefaultResponse.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(EXPECT_OK);
        }
    }

    public ErrorResponse expectErrorResponseObject(MvcResult res) throws Exception {
        return extractErrorResponse(res);
    }

    protected ErrorResponse extractErrorResponse(MvcResult res) throws Exception {
        if (res == null) {
            return null;
        }
        String json = res.getResponse().getContentAsString();
        try {
            return new ObjectMapper().readValue(json, ErrorResponse.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(EXPECT_ERROR);
        }
    }

    public String jsonStringify(Object data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(data);
        return jsonString;
    }

    public <T> T convert(Object fromValue, Class<T> toValueType) {
        return new ObjectMapper().registerModule(new JavaTimeModule()).convertValue(fromValue, toValueType);
    }

    public Object getObject(DefaultResponse res, Class classInfo) throws JsonProcessingException {
        String json = jsonStringify(res.getData());
        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(json, classInfo);
        return object;
    }

}

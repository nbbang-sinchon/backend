package nbbang.com.nbbang.domain.party.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * An Utility for controller Mock tests
 */


public class ControllerTestUtil {

    @Autowired private MockMvc mockMvc;

    private String EXPECT_OK = "Expected OK, but wasn't";
    private String EXPECT_ERROR = "Expected Error, but wasn't";

    public Map expectMapData(MvcResult res) throws Exception {
        DefaultResponse resp = expectDefaultResponseObject(res);
        Map data = (Map) resp.getData();
        return data;
    }

    public Map expectMapData(DefaultResponse res) throws Exception {
        Map data = (Map) res.getData();
        return data;
    }

    public Map expectMapData(RequestBuilder requestBuilder) throws Exception {
        DefaultResponse res = expectDefaultResponseObject(requestBuilder);
        Map data = (Map) res.getData();
        return data;
    }

    public <T> T convert(Object fromValue, Class<T> toValueType) {
        return new ObjectMapper().registerModule(new JavaTimeModule()).convertValue(fromValue, toValueType);
    }

    public DefaultResponse expectDefaultResponseObject(RequestBuilder requestBuilder) throws Exception {
        MvcResult res = this.mockMvc.perform(requestBuilder).andReturn();
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


    public DefaultResponse expectDefaultResponseObject(MvcResult res) throws Exception {
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

    public DefaultResponse expectDefaultResponseWithDto(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object data) throws Exception {
        MvcResult res = mockMvc.perform(mockHttpServletRequestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringify(data)))
                .andReturn();
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


    public ErrorResponse expectErrorResponseObject(RequestBuilder requestBuilder) throws Exception {
        MvcResult res = this.mockMvc.perform(requestBuilder).andReturn();

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

    public ErrorResponse expectErrorResponseObject(MvcResult res) throws Exception {
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

    public ErrorResponse expectErrorResponseWithDto(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object data) throws Exception {
        MvcResult res = mockMvc.perform(mockHttpServletRequestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringify(data)))
                .andReturn();
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

}

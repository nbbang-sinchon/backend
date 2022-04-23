package nbbang.com.nbbang.global.support.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static nbbang.com.nbbang.global.support.controller.ControllerUtilMessage.EXPECT_ERROR;
import static nbbang.com.nbbang.global.support.controller.ControllerUtilMessage.EXPECT_OK;

public class ControllerMockTestUtil extends ControllerTestUtil {
    @Autowired private MockMvc mockMvc;

    public DefaultResponse expectDefaultResponseObject(RequestBuilder requestBuilder) throws Exception {
        MvcResult res = this.mockMvc.perform(requestBuilder).andReturn();
        return extractDefaultResponse(res);
    }

    public DefaultResponse expectDefaultResponseObject(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object data) throws Exception {
        MvcResult res = mockMvc.perform(mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringify(data)))
                .andReturn();
        return extractDefaultResponse(res);
    }

    public ErrorResponse expectErrorResponseObject(RequestBuilder requestBuilder) throws Exception {
        MvcResult res = this.mockMvc.perform(requestBuilder).andReturn();
        return extractErrorResponse(res);
    }

    public ErrorResponse expectErrorResponseObject(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, Object data) throws Exception {
        MvcResult res = mockMvc.perform(mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringify(data)))
                .andReturn();
        return extractErrorResponse(res);
    }

}

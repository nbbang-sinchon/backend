package nbbang.com.nbbang.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.UNAUTHORIZED_ERROR;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized user tried to access secured resource");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        ObjectMapper om = new ObjectMapper();
        writer.print(om.writeValueAsString(ErrorResponse.res(StatusCode.UNAUTHORIZED, UNAUTHORIZED_ERROR, null)));
    }
}


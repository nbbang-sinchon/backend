package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandShakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            log.info("URI: {}", request.getURI()); // http://localhost:8094/ws-stomp/611/afkxvbnq/websocket?token=nbbang //
            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
            String queryString = servletRequest.getQueryString();
            Map<String,String> queryMap = getQueryMap(servletRequest.getQueryString());
            String token = queryMap.get("token");
            if(!token.equals("nbbang")){ // **************** verify token value *******************//
                return false;
            }
        }
        return true;
    }

    private Map getQueryMap(String queryString){
        String[] params = queryString.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

}



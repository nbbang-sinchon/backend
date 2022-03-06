package nbbang.com.nbbang.global.security;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CookieUtils {
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addResponseCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .secure(true)
                .sameSite("none")
                .maxAge(3600000)
                .domain("127.0.0.1")
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void addResponseCookie(HttpServletResponse response, String name, String value, boolean httpOnly, boolean secure, int maxAge, String sameSite, String domain, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .secure(secure)
                .httpOnly(httpOnly)
                .sameSite(sameSite)
                .maxAge(maxAge)
                .domain(domain)
                .path(path)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
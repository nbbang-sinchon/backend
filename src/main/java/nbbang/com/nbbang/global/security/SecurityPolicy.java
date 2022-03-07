package nbbang.com.nbbang.global.security;

public class SecurityPolicy {
    public static int TOKEN_EXPIRE_TIME = 3600000;
    public static String TOKEN_COOKIE_KEY = "access_token";
    public static String DEFAULT_REDIRECT_URI = "http://127.0.0.1:3000";
    //public static String FRONTEND_DOMAIN = "127.0.0.1";
    public static String TOKEN_SECRET_KEY = "eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0NjEwMjQ2MywiaWF0IjoxNjQ2MTAyNDYzfQ._l4MpJZlb32_h-aSTyF77P8Kn-PL25Hio89YK_ntpMba_z9YfDi-aJdGDSYNnOYLC9okXyxB_YvYkmZLjqhrVQ";



    /*
    Local settings
     */
    //public static String FRONTEND_DOMAIN = "localhost";
    //public static String DEFAULT_REDIRECT_URI = "http://localhost:3000";
}

package nbbang.com.nbbang.global.socket;

import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisServer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    //@Value("${spring.redis.port}")
    private int redisPort = 6379;
    private String redisHost = "localhost";

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis(){
        if(redisServer!=null){
            redisServer.stop();
        }
    }
}

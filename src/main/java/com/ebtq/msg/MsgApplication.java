package com.ebtq.msg;

import com.ebtq.msg.netty.ChatServer;
import io.netty.channel.ChannelFuture;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yingb
 * @since 2021/4/10
 */
@SpringBootApplication
public class MsgApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MsgApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        final ChannelFuture future = new ChatServer().start(9999);
        future.channel().closeFuture().sync();
    }
}

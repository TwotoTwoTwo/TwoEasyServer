package cn.wsjiu.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"cn.wsjiu.server", "cn.wsjiu.IM"})
public class App extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }

}

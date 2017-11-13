package com.lese.spring.social;

import com.lese.spring.social.qq.connect.QQConnectionFactory;
import com.lese.spring.social.wechat.connect.WeChatConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 * spring social配置
 * Created by ganguixiang on 2017/11/10.
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
        return connectController;
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // 添加qq连接工厂到spring social中
        connectionFactoryConfigurer.addConnectionFactory(new QQConnectionFactory(environment.getProperty("qq.clientId"), environment.getProperty("qq.clientSecret")));
        connectionFactoryConfigurer.addConnectionFactory(new WeChatConnectionFactory(environment.getProperty("wechat.clientId"), environment.getProperty("wechat.clientSecret")));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // 用户连接仓库，这里可以保存到内存中InMemoryUsersConnectionRepository，也可以保存到数据库中JdbcUsersConnectionRepository
        return new InMemoryUsersConnectionRepository(connectionFactoryLocator);
    }
}

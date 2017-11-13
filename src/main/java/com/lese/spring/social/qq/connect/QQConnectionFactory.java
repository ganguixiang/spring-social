package com.lese.spring.social.qq.connect;

import com.lese.spring.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * qq连接工厂
 * Created by ganguixiang on 2017/11/10.
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String clientId, String clientSecret) {
        super("qq", new QQServiceProvider(clientId, clientSecret), new QQAdapter());
    }
}

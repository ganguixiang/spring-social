package com.lese.spring.social.qq.connect;

import com.lese.spring.social.qq.api.QQ;
import com.lese.spring.social.qq.api.impl.QQTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * qq服务提供者
 * Created by ganguixiang on 2017/11/10.
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    public QQServiceProvider(String clientId, String clientSecret) {
        // 因为qq认证返回的结果与spring social期望的结果不太一致，所以需要自定义OAuth2Template
        super(new QQOAuth2Template(clientId, clientSecret, "https://graph.qq.com/oauth2.0/authorize", "https://graph.qq.com/oauth2.0/token"));
        this.appId = clientId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQTemplate(accessToken, appId);
    }

}

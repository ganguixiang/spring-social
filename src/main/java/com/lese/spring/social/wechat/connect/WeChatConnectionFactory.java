package com.lese.spring.social.wechat.connect;

import com.lese.spring.social.wechat.api.WeChat;
import com.lese.spring.social.wechat.api.WeChatAccessGrant;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信登陆连接工厂
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatConnectionFactory extends OAuth2ConnectionFactory<WeChat> {

    public WeChatConnectionFactory(String clientId, String clientSecret) {
        super("wechat", new WeChatServiceProvider(clientId, clientSecret), new WeChatAdapter());
    }

    /**
     * 微信授权后，accessToken和openId一起返回
     * @param accessGrant
     * @return
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeChatAccessGrant) {
            return ((WeChatAccessGrant) accessGrant).getOpenId();
        }
        return super.extractProviderUserId(accessGrant);
    }

    /**
     * 重写创建连接方法，目的是为了将openId传入adapter中
     * @param accessGrant
     * @return
     */
    @Override
    public Connection<WeChat> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeChat>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    private ApiAdapter<WeChat> getApiAdapter(String providerId) {
        return new WeChatAdapter(providerId);
    }

    private OAuth2ServiceProvider<WeChat> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeChat>) getServiceProvider();
    }
}

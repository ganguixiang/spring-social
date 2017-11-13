package com.lese.spring.social.qq.connect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by ganguixiang on 2017/11/10.
 */
public class QQOAuth2Template extends OAuth2Template {

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 在restTemplate上添加解析text/html类型的转换器StringHttpMessageConverter
     * @return
     */
    @Override
    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();
        /**
         * 解决这个异常
         * Exception while handling OAuth2 callback (Could not extract response: no suitable HttpMessageConverter found for response type [interface java.util.Map] and content type [text/html]).
         * Redirecting to qq connection status page.
         */
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    /**
     * 解析qq授权后的响应结果成AccessGrant
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        // access_token=93E024AE0F5A96CE687B459A3348FD0B&expires_in=7776000&refresh_token=A01CCFBA9C3ECE8B265B748920073248
        String response = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        String[] strings = StringUtils.splitByWholeSeparator(response, "&");
        String accessToken = StringUtils.substringAfterLast(strings[0], "=");
        String expiresIn = StringUtils.substringAfterLast(strings[1], "=");
        String refreshToken = StringUtils.substringAfterLast(strings[2], "=");
        return new AccessGrant(accessToken, null, refreshToken, Long.parseLong(expiresIn));
    }
}

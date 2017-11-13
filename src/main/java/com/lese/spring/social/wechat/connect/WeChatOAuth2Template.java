package com.lese.spring.social.wechat.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lese.spring.social.wechat.api.WeChatAccessGrant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 微信认证模板
 * Created by ganguixiang on 2017/11/11.
 */
public class WeChatOAuth2Template extends OAuth2Template {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatOAuth2Template.class);

    private String appId;

    private String secret;

    private String accessTokenUrl;

    private String authorizeUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    /**
     * 微信授权的请求与标准的OAuth2授权请求有些许不同，需要自定义
     * @param clientId
     * @param clientSecret
     * @param authorizeUrl
     * @param accessTokenUrl
     */
    public WeChatOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.appId = clientId;
        this.secret = clientSecret;
        // 参考：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN
        // https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        String clientInfo = "?appid=" + formEncode(clientId);
        this.authorizeUrl = authorizeUrl + clientInfo;
        this.accessTokenUrl = accessTokenUrl;
    }

    /**
     * 加入微信认证需要的一些参数
     * @param parameters
     * @return
     */
    public String buildAuthorizeUrl(OAuth2Parameters parameters) {
        // 登陆成功回调地址
        parameters.set("redirect_uri", "http://www.pinzhi365.com/login/wechat");
        // 添加scope参数
        parameters.add("scope", "snsapi_login");
        return buildAuthUrl(authorizeUrl, GrantType.AUTHORIZATION_CODE, parameters);
    }

    private String buildAuthUrl(String baseAuthUrl, GrantType grantType, OAuth2Parameters parameters) {
        StringBuilder authUrl = new StringBuilder(baseAuthUrl);
        if (grantType == GrantType.AUTHORIZATION_CODE) {
            authUrl.append('&').append("response_type").append('=').append("code");
        } else if (grantType == GrantType.IMPLICIT_GRANT) {
            authUrl.append('&').append("response_type").append('=').append("token");
        }
        for (Iterator<Map.Entry<String, List<String>>> additionalParams = parameters.entrySet().iterator(); additionalParams.hasNext();) {
            Map.Entry<String, List<String>> param = additionalParams.next();
            String name = formEncode(param.getKey());
            for (Iterator<String> values = param.getValue().iterator(); values.hasNext();) {
                authUrl.append('&').append(name);
                String value = values.next();
                if (StringUtils.hasLength(value)) {
                    authUrl.append('=').append(formEncode(value));
                }
            }
        }
        return authUrl.toString();
    }

    private String formEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            // should not happen, UTF-8 is always supported
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 加入text/plain解析器
     * @return
     */
    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = super.getRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        return restTemplate;
    }

    /**
     * 请求accessToken
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * @param authorizationCode
     * @param redirectUri
     * @param additionalParameters
     * @return
     */
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("appid", this.appId);
        params.set("secret", this.secret);

        params.set("code", authorizationCode);
        params.set("redirect_uri", redirectUri);
        params.set("grant_type", "authorization_code");
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }
        return postForAccessGrant(accessTokenUrl, params);
    }

    /**
     * 请求accessToken
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        return extractAccessGrant(getRestTemplate().postForObject(accessTokenUrl, parameters, String.class));
    }

    /**
     * 解析微信授权后返回的结果到accessGrant中
     * {
         "access_token":"yX2dx6n04Qg5VN2cPpNXXM5lqduzpaKjldR53KsLUGtzWPI4K9AK71ugoy-fV7_VW2y8oSFK9YBardrdx117xg",
         "expires_in":7200,
         "refresh_token":"k4KftIzXLgpvylX1czLeVypM0PWUq9zBNY2eTbZSdja4iBjJZuzFQxk6n_0j3OvUEg7zL3Ew6bI-zH4yYNcedw",
         "openid":"od4PTw__0zRxmQk4a_H52J2UhyUY",
         "scope":"snsapi_login",
         "unionid":"oEg8VuAmETZFAV4IMcZDv83xkFQk"
         }
     * @param result
     * @return
     */
    private AccessGrant extractAccessGrant(String result) {
        LOGGER.info(result);
        Map<String, Object> resultMap = null;
        try {
            resultMap = new ObjectMapper().readValue(result, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String accessToken = (String) resultMap.get("access_token");
        Long expiresIn = ((Integer) resultMap.get("expires_in")).longValue();
        String refreshToken = (String) resultMap.get("refresh_token");
        String openId = (String) resultMap.get("openid");
        String scope = (String) resultMap.get("scope");
        String unionId = (String) resultMap.get("unionid");

        WeChatAccessGrant weChatAccessGrant = new WeChatAccessGrant(accessToken, scope, refreshToken, expiresIn);
        weChatAccessGrant.setOpenId(openId);
        weChatAccessGrant.setUnionId(unionId);
        return weChatAccessGrant;
    }

    /**
     * 重写微信刷新token方法
     * @param refreshToken
     * @param additionalParameters
     * @return
     */
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.set("appid", this.appId);
        params.set("grant_type", "refresh_token");
        params.set("refresh_token", refreshToken);
        if (additionalParameters != null) {
            params.putAll(additionalParameters);
        }
        return postForAccessGrant(REFRESH_TOKEN_URL, params);
    }
}

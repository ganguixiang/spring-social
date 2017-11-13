package com.lese.spring.social.wechat.api;

import org.springframework.social.ApiBinding;

/**
 * Created by ganguixiang on 2017/11/11.
 */
public interface WeChat extends ApiBinding {

    UserOperations userOperations();
}

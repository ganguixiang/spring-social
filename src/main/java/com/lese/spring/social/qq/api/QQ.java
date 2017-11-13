package com.lese.spring.social.qq.api;

import org.springframework.social.ApiBinding;

/**
 * QQ api
 * Created by ganguixiang on 2017/11/10.
 */
public interface QQ extends ApiBinding {

    UserOperations userOperations();
}

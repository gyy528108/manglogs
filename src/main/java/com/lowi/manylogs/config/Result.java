package com.lowi.manylogs.config;

import lombok.Data;

/**
 * Result.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/4/2 16:22
 */
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;
}

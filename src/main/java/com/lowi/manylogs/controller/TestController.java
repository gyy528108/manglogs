package com.lowi.manylogs.controller;

import com.lowi.manylogs.config.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * TestController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/4/2 16:20
 */
@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping("/bigDataMethod")
    public Result bigDataMethod() {
        String[] strs = new String[]{"1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3"};
        List<String> strings = Arrays.asList(strs);
        Result result = new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(strings);
        return result;
    }

    @RequestMapping("/smallDataMethod")
    public Result smallDataMethod() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("success");
        return result;
    }
}

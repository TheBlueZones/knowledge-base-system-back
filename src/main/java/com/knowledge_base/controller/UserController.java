package com.knowledge_base.controller;


import com.alibaba.fastjson.JSONObject;
import com.knowledge_base.req.UserLoginReq;
import com.knowledge_base.req.UserQueryReq;
import com.knowledge_base.req.UserResetPasswordReq;
import com.knowledge_base.req.UserSaveReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.resp.UserLoginResp;
import com.knowledge_base.resp.UserQueryResp;
import com.knowledge_base.service.DocService;
import com.knowledge_base.service.UserService;
import com.knowledge_base.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;
    @Resource
    private DocService docService;

    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;/*依赖里面的类*/

    @GetMapping("/list")
    public CommonResp list( @Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save( @Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        /*@RequestBody就对应这jsion方式的提交*/
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    /*@PathVariable用于{id}和id映射*/
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = userService.delete(id);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }


    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        //        生成单点登录的token，放入redis
        Long token = snowFlake.nextId();/*雪花算法生成一个token*/
        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        //JSONObject.toJSONString(userLoginResp) 转换成json格式字符串
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
        /*opsForValue().set执行set这个操作，set往里面赋值，以token为key，userLoginResp为值，3600 * 24时效*/
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable Long token) {
        LOG.info("token：{}，从redis中删除", token);
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);//删除redis中的token
        LOG.info("删除redis中的token：{}", token);
        return resp;
    }



}
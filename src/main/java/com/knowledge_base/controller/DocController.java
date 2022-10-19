package com.knowledge_base.controller;


import com.knowledge_base.req.DocQueryReq;
import com.knowledge_base.req.DocSaveReq;
import com.knowledge_base.resp.DocQueryResp;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/list")
    public CommonResp list( @Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/all")
    public CommonResp all( ) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save( @Valid @RequestBody DocSaveReq req) {
        /*@RequestBody就对应这jsion方式的提交*/
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    /*@PathVariable用于{id}和id映射*/
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        CommonResp resp = docService.delete(list);

        return resp;
    }
}
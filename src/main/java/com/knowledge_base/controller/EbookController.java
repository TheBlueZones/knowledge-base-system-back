package com.knowledge_base.controller;


import com.knowledge_base.req.EbookQueryReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.EbookQueryResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save( @RequestBody EbookQueryReq req) {
        /*@RequestBody就对应这jsion方式的提交*/
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    /*@PathVariable用于{id}和id映射*/
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
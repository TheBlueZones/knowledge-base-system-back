package com.knowledge_base.controller;


import com.knowledge_base.req.CategoryQueryReq;
import com.knowledge_base.req.CategorySaveReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.CategoryQueryResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list( @Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save( @Valid @RequestBody CategorySaveReq req) {
        /*@RequestBody就对应这jsion方式的提交*/
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    /*@PathVariable用于{id}和id映射*/
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = categoryService.delete(id);
        return resp;
    }
}
package com.knowledge_base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge_base.model.dao.CategoryMapper;
import com.knowledge_base.model.pojo.Category;
import com.knowledge_base.model.pojo.CategoryExample;
import com.knowledge_base.req.CategoryQueryReq;
import com.knowledge_base.req.CategorySaveReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.CategoryQueryResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.util.CopyUtil;
import com.knowledge_base.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper eategoryMapper;
    @Resource
    private SnowFlake snowFlake;


    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample eategoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = eategoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> eategoryList = eategoryMapper.selectByExample(eategoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(eategoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<CategoryResp> respList = new ArrayList<>();
        // for (Category eategory : eategoryList) {
        //     // CategoryResp eategoryResp = new CategoryResp();
        //     // BeanUtils.copyProperties(eategory, eategoryResp);
        //     // 对象复制
        //     CategoryResp eategoryResp = CopyUtil.copy(eategory, CategoryResp.class);
        //
        //     respList.add(eategoryResp);
        // }

        // 列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(eategoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(@Valid CategorySaveReq req) {
        Category eategory = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
//            eategory.setDocCount(0);
//            eategory.setViewCount(0);
//            eategory.setVoteCount(0);
            eategory.setId(snowFlake.nextId());
            eategoryMapper.insert(eategory);
        } else {
            // 更新
            System.out.println( eategoryMapper.updateByPrimaryKey(eategory));
        }
    }

    public CommonResp delete(Long id) {
        CommonResp resp = new CommonResp();
        int result = eategoryMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            resp.setMessage("删除失败");
            resp.setSuccess(false);
        }else {
            resp.setMessage("删除成功");
        }
        return resp;
    }
}


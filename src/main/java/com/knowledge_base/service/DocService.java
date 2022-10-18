package com.knowledge_base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge_base.model.dao.DocMapper;
import com.knowledge_base.model.pojo.Doc;
import com.knowledge_base.model.pojo.DocExample;
import com.knowledge_base.req.DocQueryReq;
import com.knowledge_base.req.DocSaveReq;
import com.knowledge_base.resp.DocQueryResp;
import com.knowledge_base.resp.CommonResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper eategoryMapper;
    @Resource
    private SnowFlake snowFlake;


    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> eategoryList = eategoryMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(eategoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<DocResp> respList = new ArrayList<>();
        // for (Doc eategory : eategoryList) {
        //     // DocResp eategoryResp = new DocResp();
        //     // BeanUtils.copyProperties(eategory, eategoryResp);
        //     // 对象复制
        //     DocResp eategoryResp = CopyUtil.copy(eategory, DocResp.class);
        //
        //     respList.add(eategoryResp);
        // }

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(eategoryList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> eategoryList = eategoryMapper.selectByExample(docExample);

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(eategoryList, DocQueryResp.class);

        return list;
    }

    public void save(@Valid DocSaveReq req) {
        Doc eategory = CopyUtil.copy(req, Doc.class);
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


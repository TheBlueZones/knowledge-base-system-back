package com.knowledge_base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge_base.model.dao.EbookMapper;
import com.knowledge_base.model.pojo.Ebook;
import com.knowledge_base.model.pojo.EbookExample;
import com.knowledge_base.req.EbookQueryReq;
import com.knowledge_base.req.EbookSaveReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.EbookQueryResp;
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
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;
    @Resource
    private SnowFlake snowFlake;


    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // List<EbookResp> respList = new ArrayList<>();
        // for (Ebook ebook : ebookList) {
        //     // EbookResp ebookResp = new EbookResp();
        //     // BeanUtils.copyProperties(ebook, ebookResp);
        //     // 对象复制
        //     EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
        //
        //     respList.add(ebookResp);
        // }

        // 列表复制
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void save(@Valid EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            // 更新
            System.out.println( ebookMapper.updateByPrimaryKey(ebook));
        }
    }

    public CommonResp delete(Long id) {
        CommonResp resp = new CommonResp();
        int result = ebookMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            resp.setMessage("删除失败");
            resp.setSuccess(false);
        }else {
            resp.setMessage("删除成功");
        }
        return resp;
    }
}


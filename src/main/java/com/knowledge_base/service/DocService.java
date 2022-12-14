package com.knowledge_base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge_base.exception.BusinessException;
import com.knowledge_base.exception.BusinessExceptionCode;
import com.knowledge_base.model.dao.ContentMapper;
import com.knowledge_base.model.dao.DocMapper;
import com.knowledge_base.model.dao.MyDocMapper;
import com.knowledge_base.model.pojo.Content;
import com.knowledge_base.model.pojo.Doc;
import com.knowledge_base.model.pojo.DocExample;
import com.knowledge_base.req.DocQueryReq;
import com.knowledge_base.req.DocSaveReq;
import com.knowledge_base.resp.CommonResp;
import com.knowledge_base.resp.DocQueryResp;
import com.knowledge_base.resp.PageResp;
import com.knowledge_base.util.CopyUtil;
import com.knowledge_base.util.RedisUtil;
import com.knowledge_base.util.RequestContext;
import com.knowledge_base.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;
    @Resource
    private MyDocMapper myDocMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WsService wsService;
//



    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("????????????{}", pageInfo.getTotal());
        LOG.info("????????????{}", pageInfo.getPages());

        // List<DocResp> respList = new ArrayList<>();
        // for (Doc doc : docList) {
        //     // DocResp docResp = new DocResp();
        //     // BeanUtils.copyProperties(doc, docResp);
        //     // ????????????
        //     DocResp docResp = CopyUtil.copy(doc, DocResp.class);
        //
        //     respList.add(docResp);
        // }

        // ????????????
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        /*EbookId ??????????????????????????????*/
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // ????????????
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    @Transactional
    public void save(@Valid DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // ??????
//            doc.setDocCount(0);
//            doc.setViewCount(0);
//            doc.setVoteCount(0);
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // ??????
            System.out.println(docMapper.updateByPrimaryKey(doc));
            int result = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            /*?????????????????????*/
            if (result == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public CommonResp delete(Long id) {
        CommonResp resp = new CommonResp();
        int result = docMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            resp.setMessage("????????????");
            resp.setSuccess(false);
        } else {
            resp.setMessage("????????????");
        }
        return resp;
    }

    public CommonResp delete(List<String> ids) {
        CommonResp resp = new CommonResp();
        DocExample docExample = new DocExample();/*????????????*/
        DocExample.Criteria criteria = docExample.createCriteria();/*??????????????????*/
        criteria.andIdIn(ids);/*??????????????????ids?????????????????????*/
        int result = docMapper.deleteByExample(docExample);
        if (result == 0) {
            resp.setMessage("????????????");
            resp.setSuccess(false);
        } else {
            resp.setMessage("????????????");
        }
        return resp;
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
//        ???????????????+1
        myDocMapper.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();/*????????????getcontent???*/
        }
    }

    public void vote(Long id) {
//        myDocMapper.increaseVoteCount(id);
//        ??????IP+doc.id??????key???24???????????????
        String ip = RequestContext.getRemoteAddr();

        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            myDocMapper.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
//????????????
        Doc doc = docMapper.selectByPrimaryKey(id);
        String log_id = MDC.get("LOG_ID");
        wsService.sendInfo("???" + doc.getName() + "???" + "????????????", log_id);
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC", "???" + doc.getName() + "???" + "????????????");
    }

    public void updateEbookInfo() {
        myDocMapper.updateEbookInfo();
    }

}


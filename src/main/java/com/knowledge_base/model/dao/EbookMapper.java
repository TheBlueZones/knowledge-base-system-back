package com.knowledge_base.model.dao;

import com.knowledge_base.model.pojo.Ebook;
import com.knowledge_base.model.pojo.EbookExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookMapper {
    long countByExample(EbookExample example);

    int deleteByExample(EbookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Ebook record);

    int insertSelective(Ebook record);

    List<Ebook> selectByExample(EbookExample example);

    Ebook selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Ebook record, @Param("example") EbookExample example);

    int updateByExample(@Param("record") Ebook record, @Param("example") EbookExample example);

    int updateByPrimaryKeySelective(Ebook record);

    int updateByPrimaryKey(Ebook record);
}
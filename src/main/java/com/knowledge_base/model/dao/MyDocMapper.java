package com.knowledge_base.model.dao;

import org.apache.ibatis.annotations.Param;

public interface MyDocMapper {
    public void increaseViewCount(@Param("id") Long id);

}

package com.bbs.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

	public List<String> getArticleList();
}

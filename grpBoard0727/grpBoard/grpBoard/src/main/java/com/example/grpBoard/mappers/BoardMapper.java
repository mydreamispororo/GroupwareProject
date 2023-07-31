package com.example.grpBoard.mappers;

import com.example.grpBoard.dto.BoardDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Insert("insert into grp_board values(null, #{grpBoardNotice}, #{grpBoardSubject}, #{grpBoardWriter}, #{grpBoardContent}, #{grpBoardUploadName}, #{grpBoardUploadUrl}, #{grpBoardUploadSize}, #{grpBoardUploadTrans}, 0, now())")
    void setBoardWrite(BoardDto boardDto);

    @Select("SELECT * FROM grp_board")
    List<BoardDto> getBoardList();


}
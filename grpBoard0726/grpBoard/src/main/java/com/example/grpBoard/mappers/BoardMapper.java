package com.example.grpBoard.mappers;

import com.example.grpBoard.dto.BoardDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardMapper {

    @Insert("insert into grp_board values(null, #{grpBoardNotice}, #{grpBoardWriter}, #{grpBoardSubject}, #{grpBoardContent}, #{grpBoardUploadName}, #{grpBoardUploadUrl}, #{grpBoardUploadSize}, #{grpBoardUploadTrans}, 0, #{grpBoardReplyGrp}, 1, 1, now())")
    void setBoardWrite(BoardDto boardDto);

    //ifnull(값, 1)
    @Select("select ifnull(Max(grp_board_reply_grp) + 1, 1) as Maxcnt from kor_board")
    int getMaxCnt();

}
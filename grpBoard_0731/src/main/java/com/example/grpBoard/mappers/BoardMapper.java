package com.example.grpBoard.mappers;

import com.example.grpBoard.dto.BoardDto;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    @Insert("insert into grp_board values(null, #{grpBoardNotice}, #{grpBoardSubject}, #{grpBoardWriter}, #{grpBoardContent}, #{grpBoardUploadName}, #{grpBoardUploadUrl}, #{grpBoardUploadSize}, #{grpBoardUploadTrans}, 0, now())")
    void setBoardWrite(BoardDto boardDto);

    @Select("SELECT * FROM grp_board order by grp_board_id desc")
    List<BoardDto> getBoardList(Map<String, Object> map);

    @Delete("delete from grp_board where grp_board_id = #{grpBoardId}")
    void deleteBoard(int grpBoardId);

    @Select("select * from grp_board where grp_board_id = #{grpBoardId}")
    BoardDto viewBoard(int grpBoardId);

    @Update("update grp_board set grp_board_notice = #{grpBoardNotice}, grp_board_subject = #{grpBoardSubject}, grp_board_writer = #{grpBoardWriter}, grp_board_content = #{grpBoardContent}, grp_board_upload_name = #{grpBoardUploadName}, grp_board_upload_url = #{grpBoardUploadUrl}, grp_board_upload_size = #{grpBoardUploadSize}, grp_board_upload_trans = #{grpBoardUploadTrans}, grp_board_regdate = now() where grp_board_id = #{grpBoardId}")
    void setModify(BoardDto boardDto);

    @Update("update grp_board set grp_board_visit = grp_board_visit + 1 where grp_board_id = #{grpBoardId}")
    void updateVisit(int grpBoardId);



}













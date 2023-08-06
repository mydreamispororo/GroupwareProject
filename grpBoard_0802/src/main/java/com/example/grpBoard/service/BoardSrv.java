package com.example.grpBoard.service;

import com.example.grpBoard.dto.BoardDto;
import com.example.grpBoard.dto.PageDto;
import com.example.grpBoard.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardSrv {

    @Autowired
    private BoardMapper boardMapper;

    public void pageCalc(int page, String grpBoardId) {

        PageDto pageDto = new PageDto();

        int totalCount = boardMapper.getBoardCount(grpBoardId);

        int totalPage = (int)Math.ceil((double) totalCount / pageDto.getPageCount());

        //시작페이지
        int startPage = (int)Math.ceil(((double) page / pageDto.getBlockCount()) - 1 ) * pageDto.getStartPage() + 1;

        //끝페이지
        int endPage = startPage + pageDto.getBlockCount() - 1;

        if(endPage > totalPage) {
            endPage = totalPage;
        }
        pageDto.setTotalPage(totalPage);
        pageDto.setStartPage(startPage);
        pageDto.setEndPage(endPage);
        pageDto.setPage(page);

        System.out.println("서비스 출력 : " + pageDto);



    }

    public List<BoardDto> getBoardList(String searchType, String words) {
        String searchQuery = "";
        if(searchType.equals("grp_board_subject") || searchType.equals("grp_board_writer")) {
            searchQuery = "where " +searchType+" ='"+words+"'";

        } else if (searchType.equals("grp_board_content")) {
            searchQuery = "where grp_board_content like '%" + words +"%'";

        }else {
            searchQuery = "";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("searchQuery", searchQuery);

        return boardMapper.getBoardList(map);
    }


}

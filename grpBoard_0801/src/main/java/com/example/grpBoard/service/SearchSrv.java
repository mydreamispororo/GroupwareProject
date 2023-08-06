package com.example.grpBoard.service;

import com.example.grpBoard.dto.BoardDto;
import com.example.grpBoard.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchSrv {

    @Autowired
    private BoardMapper boardMapper;

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

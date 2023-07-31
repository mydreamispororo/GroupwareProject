package com.example.grpBoard.Controller;

import com.example.grpBoard.dto.BoardDto;
import com.example.grpBoard.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BoardController {
    @Value("${spring.servlet.multipart.location}")
    String UPLOAD_LOCATION;

    @Autowired
    private BoardMapper boardMapper;

    @GetMapping("/board/list")
    public String getList() {
        return "board/list";
    }

    @GetMapping("/board/write")
    public String getWrite() {
        return "board/write";
    }

    @PostMapping("/board/write")
    @ResponseBody
    public String setBoardWrite(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {

        //원본파일 이름, 원본파일 용량, 원본파일 이름 바꾸기(날짜로)
        /*System.out.println("원본 파일명 : " + uploadFile.getOriginalFilename()); //원본파일 이름
        System.out.println("원본 파일용량 : " + uploadFile.getSize() + "bytes"); //원본파일 용량
        String oName = uploadFile.getOriginalFilename();

        String ext = oName.substring(oName.lastIndexOf("."));
        System.out.println("원본파일 확장자 : " + ext);

                        //현재시간을 1 / 1000초 . 확장자
        String tName = System.currentTimeMillis() + ext;
        System.out.println("변환된 파일명 : " + tName);*/

        try {
            if(uploadFile != null) {
                //첨부파일
                boardDto.setGrpBoardUploadName(uploadFile.getOriginalFilename()); //업로드 파일 원본이름
                boardDto.setGrpBoardUploadSize(uploadFile.getSize()); //업로드 파일 사이즈

                String oName = uploadFile.getOriginalFilename(); //업로드 파일 변경된 이름
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                boardDto.setGrpBoardUploadTrans(tName);

            }
            //첨부파일 O, X 실행되어야 하는 구문
//            boardMapper.setBoardWrite(boardDto);
            System.out.println(boardDto);

        }catch (Exception e) {
            e.printStackTrace(); //예외가 생기면 추적하겠다는 뜻
        }
        return "";
    }


}

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
import java.util.Objects;

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
    public Map<String, Object> setBoardWrite(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {

        Map<String, Object> map = new HashMap<>();

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
                //날짜별 폴더 생성
                String saveDir = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

                //폴더 생성 : File 객체 사용
                File f = new File(UPLOAD_LOCATION + "\\" + saveDir);

                //폴더가 없을 때만 만들기
                if(!f.exists()) {
                    f.mkdir();
                }
                String oName = uploadFile.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String tName = System.currentTimeMillis() + ext;

                //파일 저장 : Path(경로, 변환된 파일명) + Files.Write(경로, 파일명.getBytes)
                Path p = Paths.get(String.valueOf(f), tName);
                Files.write(p, uploadFile.getBytes());

                //첨부파일
                boardDto.setGrpBoardUploadName(uploadFile.getOriginalFilename());
                boardDto.setGrpBoardUploadSize(uploadFile.getSize());
                boardDto.setGrpBoardUploadTrans(tName);

            }
            map.put("msg", "success");

            //첨부파일 O, X 실행되어야 하는 구문
            boardMapper.setBoardWrite(boardDto);
            System.out.println(boardDto);

        }catch (Exception e) {
            e.printStackTrace(); //예외가 생기면 추적하겠다는 뜻
        }
        return map;
    }


}

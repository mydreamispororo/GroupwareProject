package com.example.grpBoard.Controller;

import com.example.grpBoard.dto.BoardDto;
import com.example.grpBoard.mappers.BoardMapper;
import com.example.grpBoard.service.SearchSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    private SearchSrv searchSrv;

    @GetMapping("/board/list")
    public String getList(Model model, @RequestParam(value = "searchType", defaultValue = "") String searchType, @RequestParam(value = "words", defaultValue = "") String words) {


        model.addAttribute("board", boardMapper.getBoardList(new HashMap<>()));
        model.addAttribute("grp", searchSrv.getBoardList(searchType, words));

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
                boardDto.setGrpBoardUploadUrl(saveDir);
                boardDto.setGrpBoardUploadSize(uploadFile.getSize());
                boardDto.setGrpBoardUploadTrans(tName);

            }
            map.put("msg", "success");


            //첨부파일 O, X 실행되어야 하는 구문
            boardMapper.setBoardWrite(boardDto);

        }catch (Exception e) {
            e.printStackTrace(); //예외가 생기면 추적하겠다는 뜻
        }
        return map;
    }


    @GetMapping("/board/delete")
    public String deleteBoard(@RequestParam int grpBoardId) {
            boardMapper.deleteBoard(grpBoardId);

        return "redirect:/board/list";
    }

    @GetMapping("/board/view")
    public String viewBoard(@RequestParam int grpBoardId, Model model) {

        if(grpBoardId > 0) {
            boardMapper.updateVisit(grpBoardId);
            model.addAttribute("board", boardMapper.viewBoard(grpBoardId));
        }

        return "board/view";
    }

    @GetMapping("/board/modify")
    public String getModify(@RequestParam int grpBoardId, Model model) {

        model.addAttribute("board", boardMapper.viewBoard(grpBoardId));
        return "board/modify";
    }

    @PostMapping("/board/modify")
    @ResponseBody
    public Map<String, Object> setModify(@ModelAttribute BoardDto boardDto, MultipartFile uploadFile) {

        Map<String, Object> map = new HashMap<>();

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
                boardDto.setGrpBoardUploadUrl(saveDir);
                boardDto.setGrpBoardUploadSize(uploadFile.getSize());
                boardDto.setGrpBoardUploadTrans(tName);

            }else {
                if(boardDto.getGrpBoardUploadName().equals("") ) {
                    boardDto.setGrpBoardUploadName(null);
                    boardDto.setGrpBoardUploadSize(boardDto.getGrpBoardUploadSize());
                    boardDto.setGrpBoardUploadTrans(null);
                }else{
                    boardDto.setGrpBoardUploadName(boardDto.getGrpBoardUploadName());
                    boardDto.setGrpBoardUploadSize(boardDto.getGrpBoardUploadSize());
                    boardDto.setGrpBoardUploadTrans(boardDto.getGrpBoardUploadTrans());
                }

            }

            //첨부파일 O, X 실행되어야 하는 구문
            boardMapper.setModify(boardDto);
            map.put("msg", "success");


        }catch (Exception e) {
            e.printStackTrace(); //예외가 생기면 추적하겠다는 뜻
        }

        return map;

    }

    @GetMapping("/board/download")
    public ResponseEntity<Resource> getDownload(@RequestParam String dir, @RequestParam String filename) throws IOException { //폴더이름, 파일이름

        //다운로드 할 저장 위치
        Path p = Paths.get(UPLOAD_LOCATION + "\\" + dir + "\\" + filename);

        /* header 정보 */
        String contentType = Files.probeContentType(p);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build());

        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(p));


        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }





}

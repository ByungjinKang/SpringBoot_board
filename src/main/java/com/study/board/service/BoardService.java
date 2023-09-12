//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import java.io.File;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public BoardService() {
    }

    public void write(Board board, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String var10000 = String.valueOf(uuid);
        String fileName = var10000 + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
        this.boardRepository.save(board);
    }

    public Page<Board> boardlist(Pageable pageable) {
        return this.boardRepository.findAll(pageable);
    }

    public Board boardView(Integer id) {
        return (Board)this.boardRepository.findById(id).get();
    }

    public void boardDelete(Integer id) {
        this.boardRepository.deleteById(id);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return this.boardRepository.findByTitleContaining(searchKeyword, pageable);
    }
}

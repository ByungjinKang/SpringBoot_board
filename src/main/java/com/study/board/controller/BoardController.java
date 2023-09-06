//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    public BoardController() {
    }

    @GetMapping({"/board/write"})
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping({"/board/writedo"})
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {
        this.boardService.write(board, file);
        model.addAttribute("message", "작성 완료");
        model.addAttribute("searchUrl", "/board/list");
        return "message";
    }

    @GetMapping({"/board/list"})
    public String boardList(Model model, @PageableDefault(page = 0,size = 10,sort = {"id"},direction = Direction.DESC) Pageable pageable, String searchKeyword) {
        Page<Board> list = null;
        if (searchKeyword != null) {
            list = this.boardService.boardSearchList(searchKeyword, pageable);
        } else {
            list = this.boardService.boardlist(pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    @GetMapping({"board/view"})
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", this.boardService.boardView(id));
        return "boardview";
    }

    @GetMapping({"/board/delete"})
    public String boardDelete(Integer id) {
        this.boardService.boardDelete(id);
        return "redirect:/board/list";
    }

    @GetMapping({"/board/modify/{id}"})
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", this.boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping({"/board/update/{id}"})
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {
        Board boardTemp = this.boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        this.boardService.write(boardTemp, file);
        model.addAttribute("message", "수정 완료");
        model.addAttribute("searchUrl", "/board/view?id=" + id);
        return "message";
    }
}

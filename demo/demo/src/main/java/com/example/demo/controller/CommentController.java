package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.declared.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/write")
    public Map<String, Object> writeComment(@RequestBody CommentDto commentDto) {
        Map<String, Object> map = new HashMap<>();
        if (commentService.save(commentDto)) {
            map.put("success", true);
            return map;
        }

        map.put("success", false);
        return map;
    }

}

package com.example.demo.controller;

import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberJoinController {

    private final MemberService memberService;

    @PostMapping("/member/idValidation")
    public Map<String,Object> validateId(@RequestBody Map<String, String> requestData) {
        String id = requestData.get("id");
        Map<String, Object> response = new HashMap<>();
        boolean isValid = memberService.isExistsByUsername(id);
        response.put("isValid", isValid);
        return response;
    }

    @PostMapping("/member/nameValidation")
    public Map<String,Object> validateName(@RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        Map<String, Object> response = new HashMap<>();
        boolean isValid = memberService.isExistsByNickname(name);
        response.put("isValid", isValid);
        return response;
    }

}

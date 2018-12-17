package me.quiz_together.root.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.quiz_together.root.model.supoort.ResultContainer;
import me.quiz_together.root.support.HashIdUtils;
import me.quiz_together.root.support.HashIdUtils.HashIdType;

@RestController
public class TestController {
    @GetMapping("/generateId")
    public ResultContainer<String> generateId(@RequestParam Long id, HashIdType hashIdType) {
        return new ResultContainer<>(HashIdUtils.encryptId(hashIdType, id));
    }

}

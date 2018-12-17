package me.quiz_together.root.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import me.quiz_together.root.support.Constant;

@Validated
@RequestMapping(Constant.API_URI)
public interface ApiController {
}

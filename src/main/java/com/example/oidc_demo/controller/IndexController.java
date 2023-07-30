package com.example.oidc_demo.controller;

import com.example.oidc_demo.common.JsonUtils;
import com.example.oidc_demo.config.security.SecurityMember;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class IndexController {

    @GetMapping
    public String index(@AuthenticationPrincipal Object object,
                        Model model) {
        if (object instanceof SecurityMember member) {
            model.addAttribute("data", JsonUtils.toJson(member.getMember()));
        }
        return "index";
    }
}

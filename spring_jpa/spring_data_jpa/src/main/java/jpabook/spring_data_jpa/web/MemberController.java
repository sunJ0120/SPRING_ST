package jpabook.spring_data_jpa.web;

import jakarta.validation.Valid;
import jpabook.spring_data_jpa.entity.Address;
import jpabook.spring_data_jpa.entity.Member;
import jpabook.spring_data_jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new jpabook.spring_data_jpa.web.MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Valid jpabook.spring_data_jpa.web.MemberForm form, BindingResult result){
        if (result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getCity(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    //추가
    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}

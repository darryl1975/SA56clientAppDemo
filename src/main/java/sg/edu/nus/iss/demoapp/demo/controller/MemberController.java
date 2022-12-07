package sg.edu.nus.iss.demoapp.demo.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.nus.iss.demoapp.demo.formModel.MemberForm;
import sg.edu.nus.iss.demoapp.demo.model.Member;
import sg.edu.nus.iss.demoapp.demo.model.Role;
import sg.edu.nus.iss.demoapp.demo.service.MemberService;
import sg.edu.nus.iss.demoapp.demo.service.RoleService;

@Controller
public class MemberController {

    @Autowired
	private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberSvc;

    @Autowired
    private RoleService roleSvc;

    List<Role> roleList;

    @ModelAttribute
    public void preLoad(Model model) {
        roleList = new ArrayList<>();
        roleList = roleSvc.findAll();
    }

    @GetMapping("/memberlisting")
    public String viewHomePage(Model model) {
        model.addAttribute("allmemlist", memberSvc.findAll());
        return "index";
    }

    @GetMapping("/addnew")
    public String addNewMember(Model model) {
        MemberForm memberForm = new MemberForm();
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("roleList", roleList);
        return "newmember";
    }

    @PostMapping("/save")
    public String saveMember(@Valid @ModelAttribute("memberForm") MemberForm memberForm, BindingResult result) {

        // System.out.println();
        // System.out.print("Create MemberForm: " + memberForm);

        if (result.hasErrors()) {
            return "addnew";
            // return "showFormForUpdate/" + memberForm.getId().toString();
        }

        List<Role> selectedRole = new ArrayList<Role>();
        selectedRole.add(memberForm.getRole());

        Member newMember = new Member();
        newMember.setDeleted(false);
        newMember.setFullName(memberForm.getFullName());
        newMember.setMobilePhone(memberForm.getMobilePhone());
        newMember.setEmail(memberForm.getEmail());

        String passwd= memberForm.getPassword();
		String encodedPassword = passwordEncoder.encode(passwd);
        newMember.setPassword(encodedPassword);
        newMember.setPostalCode(memberForm.getPostalCode());
        newMember.setBirthDay(memberForm.getBirthDay());
        newMember.setBirthMonth(memberForm.getBirthMonth());
        newMember.setRoles(selectedRole);
        newMember.setMyRoles(selectedRole);
        newMember.setCreatedBy("Darryl");
        newMember.setCreatedTime(OffsetDateTime.now());
        memberSvc.create(newMember);

        return "redirect:/memberlisting";
    }

    @GetMapping("/deleteMember/{id}")
    public String deleteThroughId(@PathVariable(value = "id") long id) {
        memberSvc.delete(id);
        return "redirect:/memberlisting";

    }

    @GetMapping("/showFormForUpdate/{id}")
    public String updateForm(@PathVariable(value = "id") long id, Model model) {
        Member retrievedMember = memberSvc.findById(id);

        List<Role> selectedRole = new ArrayList<Role>();
        selectedRole = retrievedMember.getRoles();

        MemberForm memberForm = new MemberForm();
        memberForm.setId(retrievedMember.getId());
        memberForm.setDeleted(false);
        memberForm.setFullName(retrievedMember.getFullName());
        memberForm.setMobilePhone(retrievedMember.getMobilePhone());
        memberForm.setEmail(retrievedMember.getEmail());
        memberForm.setPassword(retrievedMember.getPassword());
        memberForm.setPostalCode(retrievedMember.getPostalCode());
        memberForm.setBirthDay(retrievedMember.getBirthDay());
        memberForm.setBirthMonth(retrievedMember.getBirthMonth());

        if (retrievedMember.getRoles().size() > 0) {
            memberForm.setRole(selectedRole.get(0));
        }

        memberForm.setCreatedBy(retrievedMember.getCreatedBy());
        memberForm.setCreatedTime(retrievedMember.getCreatedTime());
        memberForm.setLastUpdatedBy("Darryl");
        memberForm.setLastUpdatedTime(OffsetDateTime.now());
        model.addAttribute("memberForm", memberForm);
        model.addAttribute("roleList", roleList);
        return "update";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute("memberForm") MemberForm memberForm, BindingResult result) {

        System.out.println();
        System.out.print("UpdateMember function: ");
        System.out.println();
        System.out.print(memberForm);
        memberForm.setCreatedTime(OffsetDateTime.now());
        memberForm.setLastUpdatedTime(OffsetDateTime.now());

        if (result.hasErrors()) {
            return "update";
            // return "showFormForUpdate/" + memberForm.getId().toString();
        }
        
        List<Role> selectedRole = new ArrayList<Role>();
        selectedRole.add(memberForm.getRole());

        Member updMember = new Member();
        updMember.setId(memberForm.getId());
        updMember.setDeleted(false);
        updMember.setFullName(memberForm.getFullName());
        updMember.setMobilePhone(memberForm.getMobilePhone());
        updMember.setEmail(memberForm.getEmail());
        updMember.setPassword(memberForm.getPassword());
        updMember.setPostalCode(memberForm.getPostalCode());
        updMember.setBirthDay(memberForm.getBirthDay());
        updMember.setBirthMonth(memberForm.getBirthMonth());
        updMember.setRoles(selectedRole);
        updMember.setMyRoles(selectedRole);
        updMember.setCreatedBy(memberForm.getCreatedBy());
        updMember.setCreatedTime(memberForm.getCreatedTime());
        updMember.setLastUpdatedBy("Darryl");
        updMember.setLastUpdatedTime(OffsetDateTime.now());
        memberSvc.update(updMember);

        return "redirect:/memberlisting";
    }
}

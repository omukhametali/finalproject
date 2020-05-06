package kz.iitu.csse.group34.controllers;

import kz.iitu.csse.group34.entities.*;
import kz.iitu.csse.group34.repositories.CommentsRepository;
import kz.iitu.csse.group34.repositories.MachinesRepository;
import kz.iitu.csse.group34.repositories.RolesRepository;
import kz.iitu.csse.group34.repositories.UserRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private MachinesRepository machinesRepository;



    @GetMapping(value = "/")
    public String index(ModelMap model){
            List<Machines> items = machinesRepository.findAll();
            List<Machines> copy = new ArrayList<>(items);
            for (Machines m : copy) {
                if (m.getBooker() != null) {
                    items.remove(m);
                }
            }
            model.addAttribute("itemler", items);
        if (getUserData()!= null) {
            if (getUserData().getIsActive() == null)
                return "redirect:/signout";
        }
        return "index";
    }


    @GetMapping("/details{id}")
    public String details(@PathVariable Long id,Model model){
        model.addAttribute("item", machinesRepository.findById(id).orElse(null));
        List<Comments> list = commentsRepository.findByMachines(machinesRepository.findById(id).orElse(null));
        model.addAttribute("comments", list);
        model.addAttribute("user",getUserData());
        return "details";
    }

    @PostMapping("/book{id}")
    public String book(@PathVariable Long id, Model model) {
        Machines m =machinesRepository.findById(id).orElse(null);
        m.setBooker(getUserData());
        machinesRepository.save(m);
        return "redirect:/";
    }

    @PostMapping("/addUser")
    public String addUser(
                            @RequestParam String name,
                            @RequestParam String email, @RequestParam String pass,
                            @RequestParam String surname, @RequestParam String otchestvo,
                            @RequestParam String IIN
                          ){
        var roles = new HashSet<Roles>();
        roles.add(rolesRepository.findById(2L).orElse(null));
        userRepository.save(new Users(0L,email, passwordEncoder.encode(pass), name, surname,otchestvo,IIN, true, roles));
        return "redirect:/users";
    }

    @PostMapping("/updateUser{id}")
    public String updateuser(@PathVariable Long id, @RequestParam(required = false) Boolean active) {
        Users u = userRepository.findById(id).orElse(null);
        u.setIsActive(active);
        userRepository.save(u);
        return "redirect:/user"+id;
    }

    @PostMapping("/addComment{id}")
    public String addcomment(@RequestParam String comment, @PathVariable Long id, Model model) {
        commentsRepository.save(new Comments(0L, getUserData(), machinesRepository.findById(id).orElse(null),comment, new Date()));
//        model.addAttribute("comments", commentsRepository.findAllByNewsPostId(id));
        return "redirect:/details"+id;
    }

    @GetMapping(path = "/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping(path = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        model.addAttribute("user", getUserData());
        return "profile";

    }

    @PostMapping("/addMachine")
    public String addPost( @RequestParam String gosNomer, @RequestParam String marka, @RequestParam String type
            , Model model, @RequestParam String year,@RequestParam String opisanye ){
        machinesRepository.save(new Machines(0L,gosNomer,marka,type,year,opisanye, null,getUserData()));
        return "redirect:/";
    }

    @GetMapping("/user{id}")
    public String getUser(@PathVariable Long id, Model model) {
        Users u = userRepository.findById(id).orElse(null);
        model.addAttribute("item", u);

        return "user";
    }


    @GetMapping(path = "/users")
    public String usersPage(Model model){

        model.addAttribute("user", getUserData());

        List<Users> us = userRepository.findAll();
        List<Users> moders = new ArrayList<>();
        List<Users> users = new ArrayList<>();

        Roles moder=rolesRepository.findById(2L).orElse(null);

        Roles admin=rolesRepository.findById(1L).orElse(null);

        for (Users u : us)
                if(!u.getRoles().contains(admin) && !u.getRoles().contains(moder))
                    moders.add(u);

        for (Users u : us)
                if(!u.getRoles().contains(admin)) {
                    users.add(u);
                }

        model.addAttribute("userList", users);
        model.addAttribute("moders",moders);

        return "users";
    }

    @PostMapping("/register")
    String reg(@RequestParam String user_email, @RequestParam String user_password, @RequestParam String full_name,
               @RequestParam String surname, @RequestParam String otchestvo,
               @RequestParam String IIN, ModelMap map){

        if(userRepository.findByEmail(user_email).orElse(null)==null) {
            Roles r = rolesRepository.findById(2L).orElse(null);
            Set<Roles> roles = new HashSet<>();
            roles.add(r);
            Users u = new Users(0L,user_email,passwordEncoder.encode(user_password),full_name,surname,otchestvo,IIN,true,roles);
            u.setEmail(user_email);
            u.setPassword(passwordEncoder.encode(user_password));
            u.setFullName(full_name);

            u.setRoles(roles);
            userRepository.save(u);
            return "redirect:/login";
        }
        else {
            return "redirect:/register";
        }

    }

    @GetMapping("/register")
    String regi(){
        return "register";
    }

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userRepository.findByEmail(secUser.getUsername()).orElse(null);
        }
        return userData;
    }

}
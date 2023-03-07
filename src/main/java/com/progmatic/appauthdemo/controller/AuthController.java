package com.progmatic.appauthdemo.controller;

import com.progmatic.appauthdemo.model.AppUser;
import com.progmatic.appauthdemo.model.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping("/registration")
    public String saveUser(
            @ModelAttribute("newUser")
            @Validated
            RegistrationForm reg,
            BindingResult result
    ) {
        try {
            if (userDetailsService.loadUserByUsername(reg.getEmail()) != null) {
                result.rejectValue("email", "NotUnique", "Ezzel mar regisztraltak");
            }
        } catch (UsernameNotFoundException e) {
            // it is ok if not found
        }

        if (result.hasErrors()) {
            return "reg-form";
        }

        AppUser nu = new AppUser();
        nu.setEmail(reg.getEmail());
        nu.setFirstName(reg.getFirstName());
        nu.setLastName(reg.getLastName());
        nu.setPassword(passwordEncoder.encode(reg.getPassword()));
        appUserRepository.save(nu);

        return "redirect:/login";
    }

    @GetMapping("/registration")
    public String saveUserForm(Model model) {
        model.addAttribute("newUser", new RegistrationForm());
        return "reg-form";
    }
}

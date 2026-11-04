package com.devut.controller;

import com.devut.dto.LoginResult;
import com.devut.service.AuthService;
import com.devut.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    private final EventService events;
    private final AuthService authService;

    public PageController(EventService events, AuthService authService) {
        this.events = events;
        this.authService = authService;
    }

    private void addEvents(Model model) {
        model.addAttribute("events", events.findAll());
    }

    @GetMapping({"/", "/login"})
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "auth";
    }

    @GetMapping("/register")
    public String register(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = false) String username,
                        @RequestParam(required = false) String password,
                        HttpSession session) {
        LoginResult result = authService.authenticate(username, password);
        if (!result.successful()) {
            return "redirect:/login?error=" + java.net.URLEncoder.encode(result.error(), java.nio.charset.StandardCharsets.UTF_8);
        }

        session.setAttribute("displayName", result.displayName());
        session.setAttribute("isAdmin", result.admin());
        session.setAttribute("userEmail", username == null ? null : username.trim().toLowerCase());
        return result.admin() ? "redirect:/admin" : "redirect:/home";
    }

    @PostMapping("/register")
    public String register(@RequestParam(required = false) String firstName,
                           @RequestParam(required = false) String lastName,
                           @RequestParam(required = false) String username,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String repeatedPassword,
                           HttpSession session) {
        LoginResult result = authService.register(firstName, lastName, username, password, repeatedPassword);
        if (!result.successful()) {
            return "redirect:/register?error=" + java.net.URLEncoder.encode(result.error(), java.nio.charset.StandardCharsets.UTF_8);
        }

        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model) { addEvents(model); return "home"; }

    @GetMapping("/events")
    public String eventList(Model model) { addEvents(model); return "events"; }

    @GetMapping("/event/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        model.addAttribute("event", events.findById(id));
        return "event-detail";
    }

    @GetMapping("/experiencias")
    public String experiences() { return "experiences"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        addEvents(model);
        String email = (String) session.getAttribute("userEmail");
        AuthService.UserProfile profile = authService.getProfile(email);
        if (profile == null) {
            Object name = session.getAttribute("displayName");
            model.addAttribute("displayName", name == null ? "Usuario" : name);
            return "dashboard";
        }
        model.addAttribute("displayName", profile.firstName() + " " + profile.lastName());
        model.addAttribute("firstName", profile.firstName());
        model.addAttribute("lastName", profile.lastName());
        model.addAttribute("userEmail", profile.email());
        return "dashboard";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String email,
                                HttpSession session) {
        String currentEmail = (String) session.getAttribute("userEmail");
        if (authService.updateProfile(currentEmail, firstName, lastName, email)) {
            session.setAttribute("userEmail", email.trim().toLowerCase());
            session.setAttribute("displayName", firstName.trim() + " " + lastName.trim());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String admin(Model model) { addEvents(model); return "admin"; }

    @PostMapping("/admin/events")
    public String addEvent() { return "redirect:/admin?tab=list"; }
}

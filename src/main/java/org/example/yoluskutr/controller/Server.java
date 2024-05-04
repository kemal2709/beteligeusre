package org.example.yoluskutr.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.yoluskutr.entity.User;
import org.example.yoluskutr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@AllArgsConstructor
public class Server {
    @Autowired
    private HttpServletRequest request;
    private final UserService userService;
    @GetMapping("/v1.0/api/write/user")
    public RedirectView writeUser(){
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        User user = new User();
        user.setIpAddress(ipAddress);
        user.setAgent(userAgent);
        userService.saveUser(user);
        return new RedirectView("http://youtube.com");
    }
    @GetMapping("/v1.0/api/download/user/get/{agent}")
    public ResponseEntity<ByteArrayResource> downloadExcel(@PathVariable String agent) {
        userService.controlUser(agent);
        List<User> users = userService.getUsers(); // Veritabanından kullanıcıları alın

        byte[] excelBytes = userService.exportToExcel(users); // Excel dosyasını oluşturun

        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

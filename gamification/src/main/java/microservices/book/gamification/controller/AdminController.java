package microservices.book.gamification.controller;

import microservices.book.gamification.service.AdminService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequestMapping("/gamification/admin")
final class AdminController {

    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/delete-db")
    public ResponseEntity deleteDatabase() {
        adminService.deleteDatabaseContents();
        return ResponseEntity.ok().build();
    }

}

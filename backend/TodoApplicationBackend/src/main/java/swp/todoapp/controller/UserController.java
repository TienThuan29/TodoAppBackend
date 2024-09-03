package swp.todoapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swp.todoapp.dto.info.SingleMessageResponse;
import swp.todoapp.dto.info.UserDTO;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.service.UserService;
import java.io.IOException;


@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/image/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<?> getImage(@PathVariable("id") Long id) {
        byte[] image = userService.getImageById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }


    @PutMapping("/update/info")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UserDTO> update(
            @RequestParam("id") Long id,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email
    ) throws NotFoundException {
        return ResponseEntity.ok(userService.updateInfo(id, fullname, email));
    }


    @PutMapping("/update/image")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<SingleMessageResponse> updateImage(
            @RequestParam("id") Long id, @RequestParam("image") MultipartFile file
    ) throws NotFoundException, IOException {
        SingleMessageResponse response = userService.updateImage(id, file);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<SingleMessageResponse> changePassword(
            HttpServletRequest req, HttpServletResponse reps,
            @RequestParam String newPassword
    ){
        SingleMessageResponse response = userService.updatePassword(req, reps, newPassword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/info")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserDTO> endPointUser(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.getUserInfo(request, response));
    }
}

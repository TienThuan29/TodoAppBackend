package swp.todoapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swp.todoapp.config.ConstantConfiguration;
import swp.todoapp.config.MessageConfiguration;
import swp.todoapp.dto.info.SingleMessageResponse;
import swp.todoapp.dto.info.UserDTO;
import swp.todoapp.exception.def.InvalidTokenException;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.mapper.UserMapper;
import swp.todoapp.model.User;
import swp.todoapp.repository.UserRepository;
import swp.todoapp.utils.ImageUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final MessageConfiguration message;

    private final ConstantConfiguration constant;

    private final PasswordEncoder passwordEncoder;

    public boolean hasAccountByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDTO getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(constant.HTTP_HEADER_AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(constant.HTTP_HEADER_AUTHORIZATION_BEARER))
            throw new InvalidTokenException(message.ERROR_JWT_INVALID_TOKEN);

        String jwt = authHeader.substring(constant.HTTP_HEADER_AUTHORIZATION_BEARER.length());
        String username = jwtService.extractUsername(jwt);
        User user = (User) userDetailsService.loadUserByUsername(username);
        return UserMapper.toUserDTO(user);
    }

    public UserDTO updateInfo(Long id, String fullname, String email) throws NotFoundException {
        User user = getUserById(id);

        if (fullname != null && !fullname.trim().isEmpty()) {
            user.setFullname(fullname.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email.trim());
        }

        return UserMapper.toUserDTO(userRepository.save(user));
    }

    public SingleMessageResponse updateImage(Long id, MultipartFile file) throws NotFoundException, IOException {
        User user = getUserById(id);
        SingleMessageResponse singleMessageResponse = new SingleMessageResponse();
        if (file != null &&  !file.isEmpty()) {
            user.setImage(ImageUtil.compressImage(file.getBytes()));
            userRepository.save(user);
            singleMessageResponse.setMessage(message.INFORM_UPDATE_PROFILE_IMAGE_SUCCESS);
        }
        singleMessageResponse.setHttpStatus(HttpStatus.OK);
        return singleMessageResponse;
    }

    public SingleMessageResponse updatePassword(HttpServletRequest request, HttpServletResponse response, String newPassword) {
        final String authHeader = request.getHeader(constant.HTTP_HEADER_AUTHORIZATION);
        if (authHeader == null ||!authHeader.startsWith(constant.HTTP_HEADER_AUTHORIZATION_BEARER)) {
            return SingleMessageResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message(message.ERROR_JWT_INVALID_TOKEN)
                    .build() ;
        }
        final String accessToken =authHeader.substring(constant.HTTP_HEADER_AUTHORIZATION_BEARER.length());
        final String username = jwtService.extractUsername(accessToken);
        User user = userRepository.findByUsername(username).orElseThrow(null);
        if(user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        return SingleMessageResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message.INFORM_UPDATE_PASS_SUCCESS)
                .build() ;
    }

    public byte[] getImageById(Long id) {
        byte [] images = null;
        User user = getUserById(id);
        if (user.getImage() != null && user.getImage().length > 0) {
            images = ImageUtil.decompressImage(user.getImage());
        }
        return images;
    }

    private User getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(message.ERROR_USER_NOT_FOUND+" "+ id)
        );
    }
}

package swp.todoapp.mapper;

import org.springframework.stereotype.Component;
import swp.todoapp.dto.info.UserDTO;
import swp.todoapp.model.Role;
import swp.todoapp.model.User;

@Component
public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .image(user.getImage())
                .email(user.getEmail())
                .roleCode(user.getRole().getCode())
                .build();
    }

    public static User toUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .fullname(userDTO.getFullname())
                .image(userDTO.getImage())
                .email(userDTO.getEmail())
                .role(Role.builder().code(userDTO.getRoleCode()).build())
                .build();
    }
}

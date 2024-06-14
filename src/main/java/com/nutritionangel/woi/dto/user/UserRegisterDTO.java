package com.nutritionangel.woi.dto.user;

import com.nutritionangel.woi.entity.UserEntity;
import com.nutritionangel.woi.enums.OAuthProvider;
import com.nutritionangel.woi.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Login ID is mandatory")
    @Size(min = 6, message = "Login ID must be at least 6 characters")
    private String loginId;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "Password must be at least 6 characters long, contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;

    @NotBlank(message = "Password confirmation is mandatory")
    private String checkedPassword;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^(01[0-9])\\d{3,4}\\d{4}$", message = "Phone number should be in the format 01X-XXX-XXXX or 01X-XXXX-XXXX")
    private String phoneNum;

    @NotBlank(message = "School is mandatory")
    private String school;

    private UserRole role;

    private OAuthProvider oAuthProvider;



    public UserEntity toEntity() {
        UserRole role = (this.role != null) ? this.role : UserRole.USER;
        OAuthProvider oAuthProvider = (this.oAuthProvider != null) ? this.oAuthProvider : OAuthProvider.LOCAL;
        return UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .email(email)
                .phoneNum(phoneNum)
                .school(school)
                .role(role)
                .oAuthProvider(oAuthProvider)
                .build();
    }


}
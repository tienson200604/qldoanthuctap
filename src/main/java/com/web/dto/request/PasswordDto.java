package com.web.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PasswordDto {

    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String oldPass;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])\\S{8,}$",
            message = "Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
    )
    private String newPass;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPass;

    @AssertTrue(message = "Xác nhận mật khẩu không khớp")
    public boolean isPasswordConfirmed() {
        if (newPass == null || confirmPass == null) {
            return false;
        }
        return newPass.equals(confirmPass);
    }
}

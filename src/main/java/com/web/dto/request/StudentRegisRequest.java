package com.web.dto.request;

import com.web.enums.InternshipType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class StudentRegisRequest {

    @Size(max = 255, message = "Tên công ty không được vượt quá 255 ký tự")
    private String companyName;

    @Size(max = 500, message = "Địa chỉ công ty không được vượt quá 500 ký tự")
    private String companyAddress;

    @Pattern(regexp = "^(|0\\d{9,10})$", message = "Số điện thoại công ty không hợp lệ")
    private String companyPhone;

    @Email(message = "Email công ty không hợp lệ")
    @Size(max = 255, message = "Email công ty không được vượt quá 255 ký tự")
    private String companyEmail;

    @Pattern(regexp = "^(|\\d{10}|\\d{13})$", message = "Mã số thuế phải gồm 10 hoặc 13 chữ số")
    private String taxCode;

    @Size(max = 1000, message = "Link giấy giới thiệu không hợp lệ")
    private String introductionPaper;

    @NotNull(message = "Hình thức thực tập không được để trống")
    private InternshipType internshipType;

    private Long semesterTeacherId;

    private Long semesterCompanyId;
}

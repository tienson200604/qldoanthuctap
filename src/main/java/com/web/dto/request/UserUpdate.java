package com.web.dto.request;

import com.web.entity.Authority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdate {

    private Long id;

    private String fullname;

    private String phone;

    private String email;

    private String code;

    private String password;

    private Authority authorities;

    private String avatar;

    private Boolean actived;
}

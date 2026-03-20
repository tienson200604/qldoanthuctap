package com.web.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DocumentStatus {

    DANG_HIEN_THI("Đang hiển thị", "#007BFF","DANG_HIEN_THI"),
    DANG_CHO("Đang chờ duyệt", "#FF9F43","DANG_CHO"),
    TU_CHOI("Từ chối duyệt", "#FF4757","TU_CHOI"),
    DANG_DONG("Đang đóng", "#2F3542","DANG_DONG");

    private final String displayName;

    private final String color;

    private final String name;
}

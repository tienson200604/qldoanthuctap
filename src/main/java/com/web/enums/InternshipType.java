package com.web.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InternshipType {

    TAI_TRUONG("TAI_TRUONG","tại trường", "#007BFF",
            "Có sẵn đề tài đăng ký, có sẵn giáo viên hướng dẫn, tự động duyệt khi đăng ký"),

    DOANH_NGHIEP_LIEN_KET("DOANH_NGHIEP_LIEN_KET","Công ty liên kết", "#FF9F43",
            "Không biết trước đề tài, có sẵn giáo viên hướng dẫn, không cần xin giấy giới thiệu, tự động duyệt khi đăng ký"),

    DOANH_NGHIEP_NGOAI("DOANH_NGHIEP_NGOAI","Doanh nghiệp ngoài", "#FF4757",
            "Không biết trước đề tài, có sẵn giáo viên hướng dẫn, Cần xin giấy giới thiệu, chờ giảng viên hướng dẫn duyệt");

    private String name;

    private final String displayName;

    private final String color;

    private final String description;
}

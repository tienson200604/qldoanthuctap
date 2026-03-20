package com.web.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StudentRegisStatus {

    DANG_CHO("DANG_CHO", "Đang chờ", "#8a8a8a"),
    DANG_THUC_HIEN("DANG_THUC_HIEN", "Đang thực hiện", "#3498db"),
    DA_HOAN_THANH("DA_HOAN_THANH", "Đã hoàn thành", "#2ecc71"),
    DA_BI_DUOI("DA_BI_DUOI", "Đã bị đuổi", "#e74c3c"),
    CANH_CAO("CANH_CAO", "Cảnh cáo", "#f39c12");

    private final String name;
    private final String displayName;
    private final String color;
}

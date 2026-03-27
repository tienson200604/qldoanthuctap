package com.web.service;

import com.web.dto.response.StudentScoreResponse;
import com.web.entity.ScoreComponent;
import com.web.entity.ScoreRatio;
import com.web.entity.StudentRegis;
import com.web.exception.MessageException;
import com.web.repository.ScoreComponentRepository;
import com.web.repository.ScoreRatioRepository;
import com.web.repository.StudentRegisRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreComponentService {

    @Autowired
    private ScoreComponentRepository scoreComponentRepository;

    @Autowired
    private ScoreRatioRepository scoreRatioRepository;

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Transactional
    public ScoreComponent save(ScoreComponent scoreComponent){
        Long studentRegisId = null;
        if(scoreComponent.getId() != null){
            ScoreComponent ex = scoreComponentRepository.findById(scoreComponent.getId()).get();
            ex.setPoint(scoreComponent.getPoint());
            scoreComponentRepository.save(ex); // Cần lưu để updateScoreTotal thấy điểm mới
            studentRegisId = ex.getStudentRegis().getId();
        }
        else{
            ScoreRatio scoreRatio = scoreRatioRepository.findById(scoreComponent.getScoreRatioId()).orElseThrow(() -> new MessageException("Không tìm thấy mã điểm"));
            Optional<ScoreComponent> check = scoreComponentRepository.findByStudentRegisIdAndScoreRatioId(scoreComponent.getStudentRegis().getId(), scoreComponent.getScoreRatioId());
            if(check.isPresent()){
                throw new MessageException("Đầu điểm này đã được chấm");
            }
            scoreComponent.setName(scoreRatio.getName());
            scoreComponent.setScoreRatioId(scoreRatio.getId());
            scoreComponent.setPercent(scoreRatio.getPercent());
            scoreComponentRepository.save(scoreComponent);
            studentRegisId = scoreComponent.getStudentRegis().getId();
        }
        updateScoreTotal(studentRegisId);
        return scoreComponent;
    }

    public Map<String, Object> getByStudentRegis(Long studentRegisId){
        // ... (existing code unchanged in view, but the tool needs the full block if I'm replacing it)
        // Actually I'll use a more precise replacement if possible.
        // Let's replace save and delete specifically.
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId).orElseThrow(()-> new MessageException("Không tìm thấy sinh viên đã đăng ký"));
        Map<String, Object> map = new HashMap<>();
        List<ScoreComponent> scoreComponents = scoreComponentRepository.findByStudentRegisId(studentRegisId);
        List<ScoreRatio> allScoreRatios = scoreRatioRepository.findBySemesterId(studentRegis.getSemesterTeacher().getSemesterType().getSemester().getId());
        Set<Long> scoredRatioIds = scoreComponents.stream().map(ScoreComponent::getScoreRatioId).collect(Collectors.toSet());
        List<ScoreRatio> notScored = allScoreRatios.stream().filter(r -> !scoredRatioIds.contains(r.getId())).collect(Collectors.toList());
        map.put("notScored", notScored);
        map.put("scored", scoreComponents);
        return map;
    }

    @Transactional
    public Map<String, Object> delete(Long id){
        try {
            ScoreComponent sc = scoreComponentRepository.findById(id).get();
            Long studentRegisId = sc.getStudentRegis().getId();
            scoreComponentRepository.deleteById(id);
            updateScoreTotal(studentRegisId); // Phải truyền studentRegisId chứ không phải component id
            return Map.of("message","Xóa thành công");
        }
        catch (Exception e){
            throw new MessageException("Xóa thất bại");
        }
    }

    public void updateScoreTotal(Long studentRegisId){
        StudentRegis studentRegis = studentRegisRepository.findById(studentRegisId).get();
        List<ScoreComponent> list = scoreComponentRepository.findByStudentRegisId(studentRegisId);
        Float total = 0F;
        for(ScoreComponent s : list){
            total += s.getPoint() * s.getPercent() / 100f;
        }
        studentRegis.setTotalScore(total);

        // Tự động xếp loại theo thang điểm chữ
        String rate;
        if      (total >= 8.5f) rate = "A";
        else if (total >= 7.8f) rate = "B+";
        else if (total >= 7.0f) rate = "B";
        else if (total >= 6.3f) rate = "C+";
        else if (total >= 5.6f) rate = "C";
        else if (total >= 4.8f) rate = "D+";
        else if (total >= 4.0f) rate = "D";
        else                    rate = "F";
        studentRegis.setRate(rate);

        studentRegisRepository.save(studentRegis);
    }

    public List<StudentScoreResponse> getStudentScores(Long semesterId, Long teacherId, String keyword, String className) {
        List<StudentRegis> list = studentRegisRepository.findScoreByFilter(
            semesterId,
            teacherId,
            (keyword == null || keyword.isBlank()) ? null : keyword,
            (className == null || className.isBlank()) ? null : className
        );
        List<StudentScoreResponse> result = new ArrayList<>();
        for (StudentRegis s : list) {
            StudentScoreResponse dto = new StudentScoreResponse();
            dto.setStudentRegisId(s.getId());
            dto.setStudentCode(s.getStudent().getCode());
            dto.setStudentUsername(s.getStudent().getUsername());
            dto.setStudentName(s.getStudent().getFullname());
            dto.setClassName(s.getStudent().getClassName());
            dto.setSemesterYear(s.getSemesterTeacher().getSemesterType().getSemester().getYearName());
            dto.setTeacherName(s.getSemesterTeacher().getTeacher().getFullname());
            dto.setInternshipType(s.getInternshipType() != null ? s.getInternshipType().name() : "");
            dto.setTotalScore(s.getTotalScore());
            dto.setEvaluate(s.getEvaluate());
            dto.setRate(s.getRate());
            result.add(dto);
        }
        return result;
    }

    public void exportStudentScoresToExcel(Long semesterId, Long teacherId, String keyword, String className, HttpServletResponse response) throws IOException {
        List<StudentScoreResponse> list = getStudentScores(semesterId, teacherId, keyword, className);

        // Lấy StudentRegis IDs để tra chi tiết điểm
        List<Long> regisIds = studentRegisRepository.findScoreByFilter(
            semesterId, teacherId,
            (keyword == null || keyword.isBlank()) ? null : keyword,
            (className == null || className.isBlank()) ? null : className
        ).stream().map(s -> s.getId()).collect(java.util.stream.Collectors.toList());

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // ── Shared styles ──
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle subHeaderStyle = workbook.createCellStyle();
            Font subHeaderFont = workbook.createFont();
            subHeaderFont.setBold(true);
            subHeaderStyle.setFont(subHeaderFont);
            subHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            subHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            subHeaderStyle.setBorderBottom(BorderStyle.THIN);
            subHeaderStyle.setAlignment(HorizontalAlignment.CENTER);

            // ════════════════════════════════
            // SHEET 1 – Tổng quan
            // ════════════════════════════════
            Sheet sheet1 = workbook.createSheet("Tổng quan");
            String[] headers1 = {"STT", "Mã SV", "Họ và tên", "Lớp", "Học kỳ", "Giáo viên HD", "Loại thực tập", "Tổng điểm", "Xếp loại", "Nhận xét"};
            Row h1 = sheet1.createRow(0);
            for (int i = 0; i < headers1.length; i++) {
                Cell c = h1.createCell(i);
                c.setCellValue(headers1[i]);
                c.setCellStyle(headerStyle);
                sheet1.setColumnWidth(i, 5000);
            }
            sheet1.setColumnWidth(2, 7000);
            sheet1.setColumnWidth(9, 12000);

            for (int i = 0; i < list.size(); i++) {
                StudentScoreResponse dto = list.get(i);
                Row row = sheet1.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(dto.getStudentCode() != null ? dto.getStudentCode() : "");
                row.createCell(2).setCellValue(dto.getStudentName() != null ? dto.getStudentName() : "");
                row.createCell(3).setCellValue(dto.getClassName() != null ? dto.getClassName() : "");
                row.createCell(4).setCellValue(dto.getSemesterYear() != null ? dto.getSemesterYear() : "");
                row.createCell(5).setCellValue(dto.getTeacherName() != null ? dto.getTeacherName() : "");
                row.createCell(6).setCellValue(dto.getInternshipType() != null ? dto.getInternshipType() : "");
                row.createCell(7).setCellValue(dto.getTotalScore() != null ? dto.getTotalScore() : 0);
                row.createCell(8).setCellValue(dto.getRate() != null ? dto.getRate() : "");
                row.createCell(9).setCellValue(dto.getEvaluate() != null ? dto.getEvaluate() : "");
            }

            // ════════════════════════════════
            // SHEET 2 – Chi tiết đầu điểm (1 dòng/SV, cột động)
            // ════════════════════════════════
            // Lấy danh sách đầu điểm của học kỳ để làm header động
            List<ScoreRatio> scoreRatios = scoreRatioRepository.findBySemesterId(semesterId);

            Sheet sheet2 = workbook.createSheet("Chi tiết đầu điểm");

            // Tạo header: STT | Mã SV | Họ tên | Lớp | [đầu điểm 1 (X%)] | ... | Tổng điểm
            Row h2 = sheet2.createRow(0);
            String[] fixedHeaders = {"STT", "Mã SV", "Họ và tên", "Lớp"};
            for (int i = 0; i < fixedHeaders.length; i++) {
                Cell c = h2.createCell(i);
                c.setCellValue(fixedHeaders[i]);
                c.setCellStyle(subHeaderStyle);
                sheet2.setColumnWidth(i, 4500);
            }
            sheet2.setColumnWidth(2, 7000); // Họ tên
            // Cột điểm động
            for (int i = 0; i < scoreRatios.size(); i++) {
                ScoreRatio sr = scoreRatios.get(i);
                String colHeader = sr.getName() + " (" + sr.getPercent().intValue() + "%)";
                Cell c = h2.createCell(fixedHeaders.length + i);
                c.setCellValue(colHeader);
                c.setCellStyle(subHeaderStyle);
                sheet2.setColumnWidth(fixedHeaders.length + i, 7000);
            }
            // Cột Tổng điểm cuối
            int totalCol = fixedHeaders.length + scoreRatios.size();
            Cell totalHeader = h2.createCell(totalCol);
            totalHeader.setCellValue("Tổng điểm");
            totalHeader.setCellStyle(subHeaderStyle);
            sheet2.setColumnWidth(totalCol, 4500);

            // Tạo map scoreRatioId -> column index để điền điểm nhanh
            java.util.Map<Long, Integer> ratioColMap = new java.util.HashMap<>();
            for (int i = 0; i < scoreRatios.size(); i++) {
                ratioColMap.put(scoreRatios.get(i).getId(), fixedHeaders.length + i);
            }

            // Data: 1 dòng / sinh viên
            for (int i = 0; i < list.size(); i++) {
                StudentScoreResponse dto = list.get(i);
                Long regisId = regisIds.get(i);
                List<ScoreComponent> components = scoreComponentRepository.findByStudentRegisId(regisId);

                Row row = sheet2.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(dto.getStudentCode() != null ? dto.getStudentCode() : "");
                row.createCell(2).setCellValue(dto.getStudentName() != null ? dto.getStudentName() : "");
                row.createCell(3).setCellValue(dto.getClassName() != null ? dto.getClassName() : "");

                // Điền điểm vào đúng cột theo scoreRatioId
                for (ScoreComponent sc : components) {
                    Integer colIdx = ratioColMap.get(sc.getScoreRatioId());
                    if (colIdx != null) {
                        row.createCell(colIdx).setCellValue(sc.getPoint() != null ? sc.getPoint() : 0);
                    }
                }

                // Tổng điểm
                row.createCell(totalCol).setCellValue(dto.getTotalScore() != null ? dto.getTotalScore() : 0);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=diem-sinh-vien.xlsx");
            workbook.write(response.getOutputStream());
        }
    }
}



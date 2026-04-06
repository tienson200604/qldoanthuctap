package com.web.service;

import com.web.dto.request.StudentRegisRequest;
import com.web.dto.response.StudentRegisAdminDto;
import com.web.entity.*;
import com.web.enums.InternshipType;
import com.web.enums.StudentRegisStatus;
import com.web.exception.MessageException;
import com.web.repository.SemesterCompanyRepository;
import com.web.repository.SemesterTeacherRepository;
import com.web.repository.StudentRegisRepository;
import com.web.repository.UserRepository;
import com.web.utils.Contains;
import com.web.utils.UserUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentRegisService {

    @Autowired
    private StudentRegisRepository studentRegisRepository;

    @Autowired
    private SemesterTeacherRepository semesterTeacherRepository;

    @Autowired
    private SemesterCompanyRepository semesterCompanyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public StudentRegis create(StudentRegisRequest request){
        SemesterTeacher semesterTeacher = null;
        SemesterCompany semesterCompany = null;
        StudentRegis studentRegis = new StudentRegis();
        if(request.getSemesterTeacherId() != null){
            semesterTeacher = semesterTeacherRepository.findById(request.getSemesterTeacherId()).orElseThrow(() -> new MessageException("Không tồn tại semesterTeacherId: "+request.getSemesterTeacherId()));
        }
        if(semesterTeacher.getSemesterType().getDeadlineRegis().isBefore(LocalDateTime.now())){
            throw new MessageException("Đã hết hạn đăng ký cho loại thực tập này");
        }
        if(request.getSemesterCompanyId() != null){
            semesterCompany = semesterCompanyRepository.findById(request.getSemesterCompanyId()).orElseThrow(() -> new MessageException("Không tồn tại semesterCompanyId: "+request.getSemesterCompanyId()));
        }
        if(request.getInternshipType().equals(InternshipType.TAI_TRUONG)){
            if(semesterTeacher == null){
                throw new MessageException("Hãy chọn semesterTeacher");
            }
            else{
                if(!semesterTeacher.getSemesterType().getType().equals(InternshipType.TAI_TRUONG)){
                    throw new MessageException("giảng viên này không hướng dẫn tai trường");
                }
            }
            if(semesterTeacher.getSemesterType().getSemester().getIsActive() == false){
                throw new MessageException("Không được chọn học kỳ khác học kỳ hiện tại");
            }
            studentRegis.setStudentRegisStatus(StudentRegisStatus.DANG_THUC_HIEN);
        }
        if(request.getInternshipType().equals(InternshipType.DOANH_NGHIEP_LIEN_KET)){
            if(semesterTeacher == null || semesterCompany == null){
                throw new MessageException("Hãy chọn semesterTeacher và semesterCompany ");
            }
            if(semesterTeacher.getSemesterType().getSemester().getId() != semesterCompany.getSemester().getId()){
                throw new MessageException("Semester không tương thích");
            }
            if(semesterTeacher.getSemesterType().getSemester().getIsActive() == false){
                throw new MessageException("Không được chọn học kỳ khác học kỳ hiện tại");
            }
            // kiểm tra số lượng
            if(semesterCompany.getMaxStudent() == semesterCompany.getCurrentStudent()){
                throw new MessageException("Công ty này đã nhận đủ sinh viên");
            }
            studentRegis.setStudentRegisStatus(StudentRegisStatus.DANG_THUC_HIEN);
        }
        if(semesterTeacher.getMaxStudents() == semesterTeacher.getCurrentStudents()){
            throw new MessageException("giảng viên này đã nhận đủ sinh viên");
        }

        User user = userUtils.getUserWithAuthority();
        Optional<StudentRegis> optional = studentRegisRepository.findByStudentAndSemester(user.getId(), semesterTeacher.getSemesterType().getSemester().getId());
        if(optional.isPresent()){
            if(optional.get().getStudentRegisStatus().equals(StudentRegisStatus.TU_CHOI)){
                studentRegisRepository.delete(optional.get());
            } else {
                throw new MessageException("Bạn đã đăng ký thực tập "+optional.get().getInternshipType().getDisplayName()+" trước đó rồi");
            }
        }
        studentRegis.setAccept(true);
        studentRegis.setStudent(user);
        studentRegis.setLocalDateTime(LocalDateTime.now());
        studentRegis.setSemesterTeacher(semesterTeacher);
        studentRegis.setTotalScore(0F);
        studentRegis.setInternshipType(request.getInternshipType());
        if(request.getInternshipType().equals(InternshipType.DOANH_NGHIEP_NGOAI)){
            validateExternalCompanyRequest(request);
            studentRegis.setCompanyAddress(request.getCompanyAddress());
            studentRegis.setCompanyPhone(request.getCompanyPhone());
            studentRegis.setCompanyEmail(normalizeBlank(request.getCompanyEmail()));
            studentRegis.setCompanyName(request.getCompanyName());
            studentRegis.setTaxCode(normalizeBlank(request.getTaxCode()));
            studentRegis.setIntroductionPaper(request.getIntroductionPaper());
            studentRegis.setAccept(false);
            studentRegis.setStudentRegisStatus(StudentRegisStatus.DANG_CHO);
        }
        if(request.getInternshipType().equals(InternshipType.TAI_TRUONG)){
            studentRegis.setCompanyAddress("Tại trường");
        }
        if(request.getInternshipType().equals(InternshipType.DOANH_NGHIEP_LIEN_KET)){
            studentRegis.setSemesterCompany(semesterCompany);
        }
        studentRegisRepository.save(studentRegis);

        semesterTeacher.setCurrentStudents(semesterTeacher.getCurrentStudents() + 1);
        semesterTeacherRepository.save(semesterTeacher);

        if(semesterCompany != null){
            semesterCompany.setCurrentStudent(semesterCompany.getCurrentStudent() + 1);
            semesterCompanyRepository.save(semesterCompany);
        }
        String linkStudent = "/student/project";
        String linkTeacher = "/teacher/project-detail/" + semesterTeacher.getId() + "#tab1";
        if(studentRegis.getInternshipType().equals(InternshipType.DOANH_NGHIEP_NGOAI)){
            notificationService.saveSingle("Đăng ký thành công", linkStudent, "Bạn đã đăng ký thực tập doanh nghiệp ngoài thành công, vui lòng chờ duyệt", user.getId());
            notificationService.saveSingle("Sinh viên đăng ký mới", linkTeacher, "Có sinh viên đăng ký thực tập doanh nghiệp ngoài đang chờ duyệt: "+user.getFullname(), semesterTeacher.getTeacher().getId());
        } else {
            notificationService.saveSingle("Đăng ký thành công", linkStudent, "Bạn đã đăng ký thực tập "+studentRegis.getInternshipType().getDisplayName()+" thành công", user.getId());
            notificationService.saveSingle("Sinh viên đăng ký mới", linkTeacher, "Có sinh viên vừa đăng ký vào lớp của bạn: "+user.getFullname(), semesterTeacher.getTeacher().getId());
        }
        return studentRegis;
    }

    public List<StudentRegis> myRegis(){
        User user = userUtils.getUserWithAuthority();
        List<StudentRegis> studentRegis = studentRegisRepository.findByUser(user.getId());
        return studentRegis;
    }

    public List<StudentRegis> findBySemesterId(Long semesterId){
        User user = userUtils.getUserWithAuthority();
        List<StudentRegis> studentRegis = studentRegisRepository.findBySemesterIdAndTeacherId(semesterId, user.getId());
        return studentRegis;
    }

    public StudentRegis findById(Long id){
        StudentRegis s = studentRegisRepository.findById(id).orElseThrow(()->new MessageException("Không có dữ liệu"));
        if(s.getStudent().getId() != userUtils.getUserWithAuthority().getId()){
            throw new MessageException("Không có quyền truy cập");
        }
        return s;
    }

    public void createAll() {
        List<SemesterTeacher> semesterTeachers = semesterTeacherRepository.findByTypeAndSemesterActive(InternshipType.TAI_TRUONG);
        List<User> students = userRepository.getUserByRole(Contains.ROLE_STUDENT);
        for(User student : students){

            // lấy semester hiện tại
            Semester semester = semesterTeachers.get(0).getSemesterType().getSemester();

            // kiểm tra đã đăng ký chưa
            Optional<StudentRegis> optional =
                    studentRegisRepository.findByStudentAndSemester(student.getId(), semester.getId());

            if(optional.isPresent()){
                continue;
            }

            // tìm giảng viên còn slot
            SemesterTeacher availableTeacher = null;

            for(SemesterTeacher st : semesterTeachers){
                if(st.getCurrentStudents() < st.getMaxStudents()){
                    availableTeacher = st;
                    break;
                }
            }

            if(availableTeacher == null){
                throw new MessageException("Không đủ giảng viên để phân công sinh viên");
            }

            StudentRegis studentRegis = new StudentRegis();
            studentRegis.setAccept(true);
            studentRegis.setStudent(student);
            studentRegis.setLocalDateTime(LocalDateTime.now());
            studentRegis.setSemesterTeacher(availableTeacher);
            studentRegis.setTotalScore(0F);
            studentRegis.setInternshipType(InternshipType.TAI_TRUONG);
            studentRegis.setCompanyAddress("Tại trường");

            studentRegisRepository.save(studentRegis);

            // tăng số lượng sinh viên
            availableTeacher.setCurrentStudents(
                    availableTeacher.getCurrentStudents() + 1
            );
            semesterTeacherRepository.save(availableTeacher);
        }
    }

    public StudentRegis acceptRequest(Long id) {
        StudentRegis studentRegis = studentRegisRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        studentRegis.setStudentRegisStatus(StudentRegisStatus.DANG_THUC_HIEN);
        studentRegis.setAccept(true);
        studentRegisRepository.save(studentRegis);
        notificationService.saveSingle("Yêu cầu đã được duyệt", "/student/project", "Yêu cầu thực tập ngoài của bạn đã được duyệt", studentRegis.getStudent().getId());
        return studentRegis;
    }

    @Transactional
    public StudentRegis rejectRequest(Long id) {
        StudentRegis studentRegis = studentRegisRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        if (!studentRegis.getStudentRegisStatus().equals(StudentRegisStatus.DANG_CHO)) {
            throw new MessageException("Chỉ có thể từ chối yêu cầu đang chờ");
        }
        studentRegis.setStudentRegisStatus(StudentRegisStatus.TU_CHOI);
        studentRegis.setAccept(false);
        studentRegisRepository.save(studentRegis);

        // Giảm số lượng sinh viên hiện tại của giảng viên
        SemesterTeacher st = studentRegis.getSemesterTeacher();
        if (st != null && st.getCurrentStudents() > 0) {
            st.setCurrentStudents(st.getCurrentStudents() - 1);
            semesterTeacherRepository.save(st);
        }

        // Giảm số lượng sinh viên hiện tại của công ty (nếu có)
        SemesterCompany sc = studentRegis.getSemesterCompany();
        if (sc != null && sc.getCurrentStudent() > 0) {
            sc.setCurrentStudent(sc.getCurrentStudent() - 1);
            semesterCompanyRepository.save(sc);
        }

        notificationService.saveSingle("Yêu cầu bị từ chối", "/student/project", "Yêu cầu thực tập ngoài của bạn đã bị từ chối", studentRegis.getStudent().getId());
        return studentRegis;
    }

    public StudentRegis completeByTeacher(Long id) {
        StudentRegis studentRegis = studentRegisRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        User teacher = userUtils.getUserWithAuthority();
        if (studentRegis.getSemesterTeacher() == null || studentRegis.getSemesterTeacher().getTeacher() == null ||
                !studentRegis.getSemesterTeacher().getTeacher().getId().equals(teacher.getId())) {
            throw new MessageException("Bạn không có quyền cập nhật sinh viên này");
        }
        return completeStudentRegis(studentRegis);
    }

    public StudentRegis completeByAdmin(Long id) {
        StudentRegis studentRegis = studentRegisRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy dữ liệu"));
        return completeStudentRegis(studentRegis);
    }

    public Page<StudentRegisAdminDto> searchAdmin(String keyword, Long semesterId, InternshipType internshipType,
                                                  StudentRegisStatus status, Pageable pageable) {
        return studentRegisRepository.findAll(buildAdminSpecification(keyword, semesterId, internshipType, status), pageable)
                .map(this::toAdminDto);
    }

    public byte[] exportAdminExcel(String keyword, Long semesterId, InternshipType internshipType,
                                   StudentRegisStatus status) {
        List<StudentRegisAdminDto> data = studentRegisRepository
                .findAll(buildAdminSpecification(keyword, semesterId, internshipType, status))
                .stream()
                .map(this::toAdminDto)
                .collect(Collectors.toList());
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("SinhVienThucTap");
            String[] headers = {"Họ tên sinh viên", "Mã sinh viên", "Lớp", "Đợt thực tập",
                    "Hình thức thực tập", "Giảng viên hướng dẫn", "Công ty", "Trạng thái", "Ngày đăng ký"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            for (int i = 0; i < data.size(); i++) {
                StudentRegisAdminDto item = data.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(defaultString(item.getStudentName()));
                row.createCell(1).setCellValue(defaultString(item.getStudentCode()));
                row.createCell(2).setCellValue(defaultString(item.getClassName()));
                row.createCell(3).setCellValue(defaultString(item.getSemesterName()));
                row.createCell(4).setCellValue(item.getInternshipType() == null ? "" : item.getInternshipType().getDisplayName());
                row.createCell(5).setCellValue(defaultString(item.getTeacherName()));
                row.createCell(6).setCellValue(defaultString(item.getCompanyName()));
                row.createCell(7).setCellValue(item.getStatus() == null ? "" : item.getStatus().getDisplayName());
                row.createCell(8).setCellValue(item.getRegisterDate() == null ? "" : item.getRegisterDate().toString());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new MessageException("Xuất file Excel thất bại");
        }
    }

    private Specification<StudentRegis> buildAdminSpecification(String keyword, Long semesterId,
                                                                InternshipType internshipType, StudentRegisStatus status) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("student").get("fullname")), pattern),
                        cb.like(cb.lower(root.get("student").get("code")), pattern),
                        cb.like(cb.lower(root.get("student").get("classname")), pattern)
                ));
            }
            if (semesterId != null) {
                predicates.add(cb.equal(root.get("semesterTeacher").get("semesterType").get("semester").get("id"), semesterId));
            }
            if (internshipType != null) {
                predicates.add(cb.equal(root.get("internshipType"), internshipType));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("studentRegisStatus"), status));
            }
            query.orderBy(cb.desc(root.get("localDateTime")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private StudentRegisAdminDto toAdminDto(StudentRegis entity) {
        StudentRegisAdminDto dto = new StudentRegisAdminDto();
        dto.setId(entity.getId());
        dto.setStudentName(entity.getStudent() == null ? "" : entity.getStudent().getFullname());
        dto.setStudentCode(entity.getStudent() == null ? "" : entity.getStudent().getCode());
        dto.setClassName(entity.getStudent() == null ? "" : entity.getStudent().getClassname());
        dto.setSemesterName(entity.getSemesterTeacher() == null ? "" : entity.getSemesterTeacher().getSemesterType().getSemester().getYearName());
        dto.setInternshipType(entity.getInternshipType());
        dto.setTeacherName(entity.getSemesterTeacher() == null ? "" : entity.getSemesterTeacher().getTeacher().getFullname());
        dto.setCompanyName(resolveCompanyName(entity));
        dto.setStatus(entity.getStudentRegisStatus());
        dto.setRegisterDate(entity.getLocalDateTime());
        return dto;
    }

    private StudentRegis completeStudentRegis(StudentRegis studentRegis) {
        if (studentRegis.getStudentRegisStatus() == StudentRegisStatus.DA_HOAN_THANH) {
            throw new MessageException("Sinh viên này đã ở trạng thái hoàn thành");
        }
        if (studentRegis.getStudentRegisStatus() == StudentRegisStatus.DANG_CHO ||
                studentRegis.getStudentRegisStatus() == StudentRegisStatus.TU_CHOI ||
                studentRegis.getStudentRegisStatus() == StudentRegisStatus.DA_BI_DUOI) {
            throw new MessageException("Trạng thái hiện tại không thể chuyển sang hoàn thành");
        }
        studentRegis.setStudentRegisStatus(StudentRegisStatus.DA_HOAN_THANH);
        studentRegis.setAccept(true);
        studentRegisRepository.save(studentRegis);
        notificationService.saveSingle("Thực tập đã hoàn thành", "/student/project",
                "Trạng thái thực tập của bạn đã được cập nhật sang hoàn thành", studentRegis.getStudent().getId());
        return studentRegis;
    }

    private String resolveCompanyName(StudentRegis entity) {
        if (entity.getInternshipType() == InternshipType.TAI_TRUONG) {
            return "Tại trường";
        }
        if (entity.getInternshipType() == InternshipType.DOANH_NGHIEP_LIEN_KET) {
            if (entity.getSemesterCompany() != null && entity.getSemesterCompany().getCompany() != null) {
                return entity.getSemesterCompany().getCompany().getName();
            }
            return "";
        }
        return defaultString(entity.getCompanyName());
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }

    private void validateExternalCompanyRequest(StudentRegisRequest request) {
        if(isBlank(request.getCompanyName())){
            throw new MessageException("Tên công ty không được để trống");
        }
        if(request.getCompanyName().trim().length() < 3){
            throw new MessageException("Tên công ty phải có ít nhất 3 ký tự");
        }
        if(isBlank(request.getCompanyAddress())){
            throw new MessageException("Địa chỉ công ty không được để trống");
        }
        if(request.getCompanyAddress().trim().length() < 10){
            throw new MessageException("Địa chỉ công ty phải có ít nhất 10 ký tự");
        }
        if(isBlank(request.getCompanyEmail())){
            throw new MessageException("Email công ty không được để trống");
        }
        if(isBlank(request.getCompanyPhone())){
            throw new MessageException("Số điện thoại công ty không được để trống");
        }
        if(isBlank(request.getTaxCode())){
            throw new MessageException("Mã số thuế không được để trống");
        }
        if(isBlank(request.getIntroductionPaper())){
            throw new MessageException("Vui lòng upload giấy giới thiệu");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeBlank(String value) {
        return value == null ? null : value.trim();
    }
}

package com.web.service;

import com.web.dto.request.StudentRegisRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
            throw new MessageException("Bạn đã đăng ký thực tập "+optional.get().getInternshipType().getDisplayName()+" trước đó rồi");
        }
        studentRegis.setAccept(true);
        studentRegis.setStudent(user);
        studentRegis.setLocalDateTime(LocalDateTime.now());
        studentRegis.setSemesterTeacher(semesterTeacher);
        studentRegis.setTotalScore(0F);
        studentRegis.setInternshipType(request.getInternshipType());
        if(request.getInternshipType().equals(InternshipType.DOANH_NGHIEP_NGOAI)){
            studentRegis.setCompanyAddress(request.getCompanyAddress());
            studentRegis.setCompanyPhone(request.getCompanyPhone());
            studentRegis.setCompanyName(request.getCompanyName());
            studentRegis.setTaxCode(request.getTaxCode());
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
        studentRegisRepository.save(studentRegis);
        return studentRegis;
    }
}

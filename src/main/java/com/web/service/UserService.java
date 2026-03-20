package com.web.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.web.dto.request.UserUpdate;
import com.web.dto.response.CustomUserDetails;
import com.web.dto.request.TokenDto;
import com.web.dto.response.UserDto;
import com.web.entity.Authority;
import com.web.entity.User;
import com.web.enums.UserType;
import com.web.exception.MessageException;
import com.web.jwt.JwtTokenProvider;
import com.web.mapper.UserMapper;
import com.web.repository.AuthorityRepository;
import com.web.repository.UserRepository;
import com.web.utils.CommonPage;
import com.web.utils.Contains;
import com.web.utils.MailService;
import com.web.utils.UserUtils;
import org.apache.log4j.lf5.LogLevel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CommonPage commonPage;

    @Value("${url.frontend}")
    private String feUrl;


    public TokenDto login(String username, String password, String tokenFcm) throws Exception {
        Optional<User> users = userRepository.findByUsername(username);
        // check infor user
        checkUser(users);
        if(passwordEncoder.matches(password, users.get().getPassword())){
            CustomUserDetails customUserDetails = new CustomUserDetails(users.get());
            String token = jwtTokenProvider.generateToken(customUserDetails);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setUser(userMapper.userToUserDto(users.get()));
            if(tokenFcm != null){
                if (!tokenFcm.equals("")){
                    users.get().setTokenFcm(tokenFcm);
                    userRepository.save(users.get());
                }
            }
            return tokenDto;
        }
        else{
            throw new MessageException("Mật khẩu không chính xác", 400);
        }
    }


    public User regisUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(exist->{
                    if(exist.getActivation_key() != null){
                        throw new MessageException("Tài khoản chưa được kích hoạt", 330);
                    }
                    throw new MessageException("Email đã được sử dụng", 400);
                });
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActived(false);
        user.setActivation_key(userUtils.randomKey());
        Authority authority = authorityRepository.findById(Contains.ROLE_STUDENT).get();
        user.setAuthorities(authority);
        User result = userRepository.save(user);
        mailService.sendEmail(user.getEmail(), "Xác nhận tài khoản của bạn","Cảm ơn bạn đã tin tưởng và xử dụng dịch vụ của chúng tôi:<br>" +
                "Để kích hoạt tài khoản của bạn, hãy nhập mã xác nhận bên dưới để xác thực tài khoản của bạn<br><br>" +
                "<a style=\"background-color: #2f5fad; padding: 10px; color: #fff; font-size: 18px; font-weight: bold;\">"+user.getActivation_key()+"</a>",false, true);
        return result;
    }

    public User addAccount(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(exist->{
                    if(exist.getActivation_key() != null){
                        throw new MessageException("Tài khoản chưa được kích hoạt", 330);
                    }
                    throw new MessageException("Ẻmail đã được sử dụng", 400);
                });
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActived(true);
        user.setUsername(user.getEmail());
        User result = userRepository.save(user);
        return result;
    }

    // kich hoat tai khoan
    public void activeAccount(String activationKey, String email) {
        Optional<User> user = userRepository.getUserByActivationKeyAndEmail(activationKey, email);
        user.ifPresent(exist->{
            exist.setActivation_key(null);
            exist.setActived(true);
            userRepository.save(exist);
            return;
        });
        if(user.isEmpty()){
            throw new MessageException("email hoặc mã xác nhận không chính xác", 404);
        }
    }

    public Boolean checkUser(Optional<User> users){
        if(users.isPresent() == false){
            throw new MessageException("Không tìm thấy tài khoản", 404);
        }
        else if(users.get().getActivation_key() != null && users.get().getActived() == false){
            throw new MessageException("Tài khoản chưa được kích hoạt", 300);
        }
        else if(users.get().getActived() == false && users.get().getActivation_key() == null){
            throw new MessageException("Tài khoản đã bị khóa", 500);
        }
        return true;
    }

    public Page<User> getUserByRole(String search,String role, Pageable pageable) {
        Page<User> page = null;
        if(role != null){
            page = userRepository.getUserByRole(search,role, pageable);
        }
        else{
            page = userRepository.findAll(search,pageable);
        }
        return page;
    }

    public void changePass(String oldPass, String newPass) {
        User user = userUtils.getUserWithAuthority();
//        if (user.getUserType().equals(UserType.GOOGLE)) {
//            throw new MessageException("Xin lỗi, chức năng này không hỗ trợ đăng nhập bằng google");
//        }
//        if(isValidPassword(newPass) == false){
//            throw new MessageException("Mật khẩu phải có ít nhất 1 chữ hoa, ký tự đặc biệt và chữ viết thường");
//        }
        if(passwordEncoder.matches(oldPass, user.getPassword())){
            if(passwordEncoder.matches(newPass, user.getPassword())){
                throw new MessageException("Mật khẩu mới không được trùng với mật khảu cũ");
            }
            user.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(user);
        }
        else{
            throw new MessageException("Mật khẩu không chính xác", 500);
        }
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>])\\S{8,}$";
        return password.matches(regex);
    }

    public void forgotPassword(String email) {
        Optional<User> users = userRepository.findByEmail(email);
        // check infor user
        checkUser(users);
        String randomPass = userUtils.randomPass();
        users.get().setPassword(passwordEncoder.encode(randomPass));
        userRepository.save(users.get());
        mailService.sendEmail(email, "Quên mật khẩu","Cảm ơn bạn đã tin tưởng và xử dụng dịch vụ của chúng tôi:<br>" +
                "Chúng tôi đã tạo một mật khẩu mới từ yêu cầu của bạn<br>" +
                "Tuyệt đối không được chia sẻ mật khẩu này với bất kỳ ai. Bạn hãy thay đổi mật khẩu ngay sau khi đăng nhập<br><br>" +
                "<a style=\"background-color: #2f5fad; padding: 10px; color: #fff; font-size: 18px; font-weight: bold;\">"+randomPass+"</a>",false, true);

    }

    public void guiYeuCauQuenMatKhau(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        checkUser(user);
        String random = userUtils.randomKey();
        user.get().setRememberKey(random);
        userRepository.save(user.get());

        mailService.sendEmail(email, "Đặt lại mật khẩu","Cảm ơn bạn đã tin tưởng và xử dụng dịch vụ của chúng tôi:<br>" +
                "Chúng tôi đã tạo một mật khẩu mới từ yêu cầu của bạn<br>" +
                "Hãy lick vào bên dưới để đặt lại mật khẩu mới của bạn<br><br>" +
                "<a href='"+feUrl+"/datlaimatkhau?email="+email+"&key="+random+"' style=\"background-color: #2f5fad; padding: 10px; color: #fff; font-size: 18px; font-weight: bold;\">Đặt lại mật khẩu</a>",false, true);

    }

    public void xacNhanDatLaiMatKhau(String email, String password, String key) {
        Optional<User> user = userRepository.findByEmail(email);
        checkUser(user);
        if(user.get().getRememberKey().equals(key)){
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
        }
        else{
            throw new MessageException("Mã xác thực không chính xác");
        }
    }

    public TokenDto loginWithGoogle(GoogleIdToken.Payload payload) {
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setUsername(payload.getEmail());
        user.setFullname(payload.get("name").toString());
        user.setAvatar(payload.get("picture").toString());
        user.setActived(true);
        user.setAuthorities(authorityRepository.findByName(Contains.ROLE_STUDENT));
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        user.setUserType(UserType.GOOGLE);

        Optional<User> users = userRepository.findByEmail(user.getEmail());
        // check infor user

        if(users.isPresent()){
            if(users.get().getActived() == false){
                throw new MessageException("Tài khoản đã bị khóa");
            }
            CustomUserDetails customUserDetails = new CustomUserDetails(users.get());
            String token = jwtTokenProvider.generateToken(customUserDetails);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setUser(userMapper.userToUserDto(users.get()));
            return tokenDto;
        }
        else{
            User u = userRepository.save(user);
            CustomUserDetails customUserDetails = new CustomUserDetails(u);
            String token = jwtTokenProvider.generateToken(customUserDetails);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setUser(userMapper.userToUserDto(u));
            return tokenDto;
        }
    }

    public User changeRole(Long id, String role) {
        User user = userRepository.findById(id).get();
        user.setAuthorities(authorityRepository.findByName(role));
        userRepository.save(user);
        return user;
    }


    public Map<String, Object> importUser(MultipartFile file) {

        int success = 0;
        int fail = 0;
        int numEmailExist = 0;
        List<String> emailExist = new ArrayList<>();

        try {

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            // bỏ dòng header
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {

                Row row = rows.next();
                try {
                    String username = getCellValue(row.getCell(0));
                    String password = getCellValue(row.getCell(1));
                    String fullname = getCellValue(row.getCell(2));
                    String email = getCellValue(row.getCell(3));
                    String phone = getCellValue(row.getCell(4));
                    String code = getCellValue(row.getCell(5));
                    String activedStr = getCellValue(row.getCell(6));
                    String authorityName = getCellValue(row.getCell(7));
                    String avatar = getCellValue(row.getCell(8));
                    if(userRepository.findByEmail(email).isPresent()){
                        ++numEmailExist;
                        emailExist.add(email);
                        continue;
                    }
                    if(username == null || username.isEmpty()){
                        fail++;
                        continue;
                    }

                    User user = new User();

                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setFullname(fullname);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setCode(code);
                    user.setAvatar(avatar);

                    if(activedStr != null){
                        user.setActived(Boolean.parseBoolean(activedStr));
                    }

                    if(authorityName != null){
                        Authority authority = authorityRepository.findByName(authorityName);
                        user.setAuthorities(authority);
                    }

                    user.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));

                    userRepository.save(user);

                    success++;

                } catch (Exception e) {
                    fail++;
                }
            }

            workbook.close();

        } catch (Exception e) {
            throw new RuntimeException("Import thất bại");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", String.valueOf(success));
        map.put("fail", String.valueOf(fail));
        map.put("numEmailExist", String.valueOf(numEmailExist));
        map.put("listEmailExist", emailExist);
        return map;
    }


    private String getCellValue(Cell cell){

        if(cell == null){
            return null;
        }

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                return String.valueOf((long)cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            default:
                return null;
        }
    }

    public Page<User> searchUsers(String keyword, String authority, Boolean actived, Pageable pageable) {
        return userRepository.findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // tránh duplicate do join many
            query.distinct(true);

        /*
         keyword search
         */
            if (keyword != null && !keyword.trim().isEmpty()) {

                String pattern = "%" + keyword.toLowerCase().trim() + "%";

                predicates.add(cb.or(

                        cb.like(cb.lower(root.get("email")), pattern),

                        cb.like(cb.lower(root.get("fullname")), pattern),

                        cb.like(cb.lower(root.get("phone")), pattern)

                ));
            }


            if (actived != null) {

                predicates.add(cb.equal(root.get("actived"), actived));

            }

            if (authority != null) {
                predicates.add(cb.equal(root.get("authorities").get("name"), authority));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        }, pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy user"));
    }

    public User updateInfor(UserUpdate update) {
        User user = null;
        if(update.getId() != null){
            user = userRepository.findById(update.getId()).orElseThrow(() -> new MessageException("Không tìm thấy user"));
            if(userRepository.findByEmailAndId(update.getEmail(), update.getId()) != null){
                throw new MessageException("Email đã được sử dụng cho tài khoản khác");
            }
            if(userRepository.findByCode(update.getCode(), update.getId()).isPresent()){
                throw new MessageException("Mã sinh viên/ giảng viên đã được sử dụng cho tài khoản khác");
            }
        }
        else{
            user = new User();
            if(userRepository.findByEmail(update.getEmail()).isPresent()){
                throw new MessageException("Email đã được sử dụng cho tài khoản khác");
            }
            if(userRepository.findByCode(update.getCode()).isPresent()){
                throw new MessageException("Mã sinh viên/ giảng viên đã được sử dụng cho tài khoản khác");
            }
            if(update.getPassword() == null || update.getPassword().trim().equals("")){
                throw new MessageException("Mật khẩu không được để trống");
            }
        }
        user.setEmail(update.getEmail());
        user.setUsername(update.getEmail());
        user.setCode(update.getCode());
        user.setAvatar(update.getAvatar());
        user.setFullname(update.getFullname());
        user.setPhone(update.getPhone());
        user.setActived(update.getActived());
        user.setAuthorities(update.getAuthorities());
        if(update.getPassword() != null && !update.getPassword().equals("")){
            user.setPassword(passwordEncoder.encode(update.getPassword()));
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        }catch (Exception e){
            throw new MessageException("User này không thể xóa do có nhiều liên kết");
        }
    }

    public List<User> allTeacher() {
        return userRepository.getUserByRole(Contains.ROLE_TEACHER);
    }
}

# Hệ Thống Quản Lý Đồ Án Thực Tập (Internship Management System)

Dự án **Quản lý Đồ án - Thực tập sinh viên** là một nền tảng Web Application toàn diện, nhằm mục đích số hóa và tối ưu hóa toàn bộ quy trình thực tập của sinh viên tại các trường Đại học/Cao đẳng. 

Hệ thống kết nối trực tiếp 3 vai trò chính: **Quản trị viên (Admin)**, **Giảng viên hướng dẫn (Teacher)** và **Sinh viên (Student)**. Từ việc đăng ký nguyện vọng, tìm kiếm doanh nghiệp, báo cáo tiến độ hàng tuần, trao đổi trực tuyến (Real-time Chat), cho đến thao tác nộp duyệt tài liệu và chấm điểm cuối khóa.

---

## 🌟 Chức Năng Nổi Bật

Hệ thống phân quyền truy cập chặt chẽ với những tính năng chuyên biệt cho từng đối tượng:

### 1. Dành cho Sinh Viên (Student)
- 🏢 **Thông tin doanh nghiệp:** Tra cứu danh sách công ty, thông tin tuyển dụng thực tập do nhà trường liên kết hoặc tự tìm kiếm ngoài.
- 📝 **Đăng ký đồ án/thực tập:** Nộp đơn đăng ký thực tập (Tại trường / Liên kết doanh nghiệp / Tự túc).
- 📈 **Báo cáo tiến độ:** Nộp tài liệu, báo cáo tuần, và nhận feedback trực tiếp từ giảng viên.
- 💬 **Thảo luận trực tuyến (Chat):** Kênh chat thời gian thực (Real-time) để trao đổi nhanh với Giảng viên và nhóm làm việc.
- 🌙 **Cá nhân hóa:** Quản lý thông tin hồ sơ cá nhân, đổi mật khẩu và tuỳ biến giao diện sáng/tối (Dark Mode).

### 2. Dành cho Giảng Viên (Teacher)
- 👥 **Quản lý nhóm sinh viên:** Xét duyệt danh sách sinh viên đăng ký, theo dõi số lượng và tiến độ.
- 📊 **Tiếp nhận & Đánh giá báo cáo:** Duyệt các tài liệu, báo cáo tiến độ tuần từ sinh viên và cho điểm đánh giá.
- 💬 **Tương tác trực tiếp:** Trả lời tin nhắn, thông báo nhắc nhở nội bộ nhanh chóng qua WebSocket Chat.
- 📰 **Tin tức & Thông báo:** Quản trị các bài đăng blog, bản tin nội bộ gửi tới sinh viên.

### 3. Dành cho Quản Trị Viên (Admin)
- 🗄️ **Quản lý tài khoản:** Thêm/sửa/xoá/phân quyền User. Hỗ trợ tính năng Import danh sách hàng loạt (Import Excel).
- 🏫 **Quản lý danh mục cốt lõi:** Quản lý Niên khóa/Năm học, Danh mục điểm, Ngành/Lớp, Doanh nghiệp.
- 📰 **Quản trị nội dung:** Phê duyệt hoặc soạn thảo các bài đăng Thông báo, Tin tức, Banner hệ thống.
- ⚙️ **Nhật ký & Cài đặt:** Theo dõi nhật ký hoạt động (System Logs), tuỳ chỉnh cấu hình toàn trang (Theme, Cài đặt hệ thống).

---

## 💻 Công Nghệ Sử Dụng (Tech Stack)

Dự án được xây dựng trên mô hình nguyên khối **Monolithic** với các công nghệ mạnh mẽ và hiện đại:

**Back-end:**
- **Java Spring Boot 3+**: Framework chính.
- **Spring Data JPA / Hibernate**: Tương tác và thao tác cơ sở dữ liệu.
- **Spring Security**: Xác thực, định danh và phân quyền bằng JWT / Session.
- **Spring WebSocket / STOMP**: Giao tiếp thời gian thực (Tính năng Chat).

**Front-end:**
- **Thymeleaf**: Template Engine cho phép nhúng dữ liệu vào HTML cực kỳ linh hoạt.
- **Bootstrap 5.3**: Xây dựng UI/UX chuyên nghiệp, Responsive, Native Dark mode.
- **Vanilla JS / jQuery**: Xử lý logic phía client và thực hiện gọi API (Fetch, Ajax).
- **Libraries khác**: TinyMCE (Rich Text Editor), SweetAlert, Select2, Toastr.

**Database & Dịch vụ ngoài:**
- **MySQL**: Hệ quản trị cơ sở dữ liệu quan hệ (Relational Database Server).
- **Cloudinary**: Dịch vụ lưu trữ file Media, hình ảnh, tài liệu tĩnh.

---

## 📂 Cấu Trúc Thư Mục Chính

```text
quanlydoan/
├── src/main/java/             # Source code cốt lõi Backend (Controller, Service, Repository, Entity, Configuration...)
├── src/main/resources/        # File cấu hình ứng dụng application.properties, file tĩnh (static)
├── src/main/webapp/           # Các Frontend Resources (Thymeleaf, CSS, JS)
│   ├── admin/js/              # Javascript xử lý nội bộ luồng Admin
│   ├── student/js/            # Javascript xử lý nội bộ luồng Student 
│   ├── teacher/js/            # Javascript xử lý nội bộ luồng Teacher
│   ├── css/                   # Stylesheets chung và giao diện Dark/Light mode (main.css, styleadmin.css)
│   └── views/                 # Chứa tất cả các trang giao diện HTML (.html)
│       ├── admin/             # Màn hình cho Admin
│       ├── student/           # Màn hình cho Sinh viên
│       ├── teacher/           # Màn hình cho Giảng viên
│       └── common/            # Fragment dùng chung (Header, Footer, Sidebar, Chat layout)
└── quanlydoan.sql             # Script khởi tạo cơ sở dữ liệu có sẵn
```

---

## 🛠️ Hướng Dẫn Cài Đặt (Setup Guide)

1. **Chuẩn bị môi trường:**
    - Cài đặt Java Development Kit (JDK) 17 trở lên.
    - Cài đặt MySQL Server.
    - Một IDE tuỳ ý (IntelliJ IDEA, Eclipse, VS Code).
    - Tạo tài khoản Cloudinary để lấy API Keys (Nếu muốn kiểm thử tính năng Upload File).

2. **Thiết lập Database:**
    - Tạo một Database rỗng trong MySQL tên `quanlydoan` (hoặc tên theo ý muốn).
    - Mở SQL Client hoặc MySQL Workbench và chạy script file `quanlydoan.sql` để tạo toàn bộ bảng và Seed dữ liệu mốc.

3. **Cấu hình Application:**
    - Đi tới `src/main/resources/application.properties`.
    - Thay đổi chuỗi kết nối Database `spring.datasource.url`, `username`, `password` tương ứng với máy tính của bạn.
    - Gắn Auth Keys của Cloudinary.

4. **Biên dịch & Chạy Server:**
    - Chạy file `QuanlydoanApplication.java` từ IDE của bạn.
    - Ứng dụng sẽ tự động khởi chạy Web Tomcat trên Port mặc định là `8080`.
    - Truy cập ứng dụng qua Localhost: `http://localhost:8080`

---
*Dự án được duy trì và phát triển mục đích phục vụ Đồ án / Bài tập lớn cấp trường Đại học.*
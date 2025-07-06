# 🛒 Shop E-Commerce Platform
Video dự án: https://drive.google.com/file/d/1pQMrpriLKPpha52gu_A05uPpNXFTc9bG/view?usp=sharing
Một nền tảng thương mại điện tử hoàn chỉnh được xây dựng với kiến trúc microservices, hỗ trợ đầy đủ các chức năng cho cả người dùng và quản trị viên.

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Chức năng chính](#chức-năng-chính)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Đóng góp](#đóng-góp)

## 🎯 Tổng quan

Shop E-Commerce là một nền tảng thương mại điện tử hiện đại với các tính năng:

- **Frontend User**: Giao diện mua sắm thân thiện với người dùng
- **Frontend Admin**: Dashboard quản lý toàn diện
- **Backend API**: RESTful API với Spring Boot
- **Real-time Communication**: WebSocket cho thông báo real-time
- **Payment Integration**: Tích hợp thanh toán Vnpay
- **Security**: JWT Authentication & Authorization

## 🏗️ Kiến trúc hệ thống

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client User   │    │  Client Admin   │    │   Backend API   │
│   (React/Vite)  │    │  (React/MUI)    │    │ (Spring Boot)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   WebSocket     │
                    │   (Real-time)   │
                    └─────────────────┘
```

## 🛠️ Công nghệ sử dụng

### Frontend (User)
- **React 18** - UI Framework
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **React Query** - State management
- **React Router** - Routing
- **Axios** - HTTP client
- **React Hook Form** - Form handling
- **Yup** - Validation

### Frontend (Admin)
- **React 18** - UI Framework
- **Material-UI (MUI)** - Component library
- **React Query** - State management
- **Chart.js** - Data visualization
- **WebSocket** - Real-time communication

### Backend
- **Spring Boot 3.2.2** - Framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database access
- **Spring WebSocket** - Real-time communication
- **MySQL** - Database
- **JWT** - Token-based authentication
- **Stripe** - Payment processing
- **Cloudinary** - Image upload
- **Lombok** - Code generation
- **MapStruct** - Object mapping

## 🚀 Chức năng chính

### 👤 Chức năng cho User

#### 🔐 Authentication & Authorization
- Đăng ký tài khoản mới
- Đăng nhập/Đăng xuất
- Quên mật khẩu và reset
- JWT token authentication
- Refresh token mechanism

#### 🛍️ Mua sắm
- **Trang chủ**: Hiển thị sản phẩm nổi bật, danh mục
- **Danh sách sản phẩm**: Tìm kiếm, lọc, sắp xếp
- **Chi tiết sản phẩm**: Thông tin chi tiết, hình ảnh, đánh giá
- **Giỏ hàng**: Thêm/xóa sản phẩm, cập nhật số lượng
- **Thanh toán**: Tích hợp Stripe, VNPay

#### 📦 Quản lý đơn hàng
- Tạo đơn hàng mới
- Xem lịch sử đơn hàng
- Hủy đơn hàng
- Theo dõi trạng thái đơn hàng

#### 👤 Quản lý tài khoản
- **Hồ sơ cá nhân**: Cập nhật thông tin
- **Đổi mật khẩu**: Bảo mật tài khoản
- **Địa chỉ giao hàng**: Quản lý địa chỉ
- **Lịch sử mua hàng**: Xem các đơn hàng đã mua

#### 💬 Tương tác
- **Đánh giá sản phẩm**: Viết review và rating
- **Hỏi đáp**: Gửi câu hỏi về sản phẩm
- **Thông báo**: Nhận thông báo real-time

### 👨‍💼 Chức năng cho Admin

#### 📊 Dashboard
- **Thống kê tổng quan**: Doanh thu, đơn hàng, người dùng
- **Biểu đồ**: Phân tích dữ liệu bán hàng
- **Real-time notifications**: Thông báo đơn hàng mới

#### 🛍️ Quản lý sản phẩm
- **Thêm/Sửa/Xóa sản phẩm**: CRUD operations
- **Quản lý danh mục**: Phân loại sản phẩm
- **Upload hình ảnh**: Tích hợp Cloudinary
- **Quản lý kho**: Cập nhật số lượng tồn kho

#### 📦 Quản lý đơn hàng
- **Xem tất cả đơn hàng**: Danh sách và chi tiết
- **Cập nhật trạng thái**: Xác nhận, giao hàng, hoàn thành
- **Xử lý hủy đơn**: Quản lý đơn hàng bị hủy
- **Thông báo real-time**: WebSocket notifications

#### 👥 Quản lý người dùng
- **Danh sách người dùng**: Xem thông tin khách hàng
- **Phân quyền**: Quản lý roles và permissions
- **Khóa/Mở khóa tài khoản**: Bảo mật hệ thống

#### 💬 Quản lý tương tác
- **Câu hỏi & Trả lời**: Xử lý câu hỏi từ khách hàng
- **Đánh giá sản phẩm**: Quản lý reviews
- **Thông báo**: Gửi thông báo cho người dùng

#### 🎫 Quản lý khuyến mãi
- **Mã giảm giá**: Tạo và quản lý discount codes
- **Khuyến mãi**: Thiết lập các chương trình khuyến mãi

## 🔧 Cài đặt và chạy

### Yêu cầu hệ thống
- Java 21
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### Backend Setup

```bash
# Clone repository
git clone <repository-url>
cd Shop-E-Commerce/ShopAppEcomere/ShopAppEcomere

# Cấu hình database trong application.properties
# Cập nhật thông tin kết nối MySQL

# Build và chạy
mvn clean install
mvn spring-boot:run
```

### Frontend User Setup

```bash
cd client
npm install
npm run dev
```

### Frontend Admin Setup

```bash
cd client-admin
npm install
npm start
```

### Cấu hình môi trường

Tạo file `.env` trong thư mục backend:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/shop_ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.signerKey=your_jwt_secret_key_here
jwt.valid_duration=86400000
jwt.refreshable-duration=604800000

# Stripe
stripe.secret-key=your_stripe_secret_key
stripe.public-key=your_stripe_public_key

# Cloudinary
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```
## 🔐 Bảo mật

- **JWT Authentication**: Token-based authentication
- **Role-based Access Control**: Phân quyền theo vai trò
- **Password Encryption**: Mã hóa mật khẩu với BCrypt
- **CORS Configuration**: Bảo mật cross-origin requests
- **Input Validation**: Validate dữ liệu đầu vào

## 🌐 WebSocket Features

- **Real-time Notifications**: Thông báo đơn hàng mới cho admin
- **Order Status Updates**: Cập nhật trạng thái đơn hàng real-time
- **Admin Dashboard**: Live updates cho dashboard

## 💳 Payment Integration

- **Stripe**: Thanh toán quốc tế
- **VNPay**: Thanh toán nội địa Việt Nam
- **Secure Checkout**: Quy trình thanh toán an toàn

## 📱 Responsive Design

- **Mobile-first**: Tối ưu cho thiết bị di động
- **Progressive Web App**: Trải nghiệm app-like
- **Cross-browser**: Hỗ trợ đa trình duyệt

## 🤝 Đóng góp

1. Fork dự án
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

## 📄 License

Dự án này được phân phối dưới MIT License. Xem file `LICENSE` để biết thêm chi tiết.

## 📞 Liên hệ

- **Email**: huyhoang23104@gmail.com
- **GitHub**: https://github.com/hoangcode204

---

⭐ Nếu dự án này hữu ích, hãy cho chúng tôi một star! 

LearnEnglish – Back-End

Backend của LearnEnglish (EngBright) — ứng dụng học tiếng Anh trực tuyến, cung cấp RESTful API và WebSocket realtime, được xây dựng bằng Java Spring Boot.

🧩 Tổng quan

Ngôn ngữ: Java

Framework: Spring Boot

Build tool: Maven (mvnw, mvnw.cmd)

Kiến trúc: REST API + WebSocket

Database: MySQL (JPA / Hibernate)

Hệ thống hỗ trợ các chức năng: xác thực người dùng, quản lý nội dung học tập, làm bài tập, chat realtime và gửi thông báo.

🚀 Tính năng chính

🔐 Xác thực & phân quyền (JWT)

👤 Quản lý người dùng

📘 Bài học: từ vựng, ngữ pháp, bài đọc

📝 Bài tập & lưu kết quả làm bài

💬 Chat realtime theo topic (WebSocket + STOMP)

🔔 Thông báo hệ thống

📊 Theo dõi tiến độ học tập

🛠️ Yêu cầu hệ thống

Java: 11+

Maven: hoặc dùng mvnw / mvnw.cmd

Database: MySQL

Kiểm tra biến môi trường JAVA_HOME

▶️ Cài đặt & chạy (Windows)
1. Build project
.\mvnw.cmd clean package -DskipTests

2. Chạy ứng dụng
java -jar target/EngBrightBackend-0.0.1-SNAPSHOT.jar


Hoặc:

.\mvnw.cmd spring-boot:run
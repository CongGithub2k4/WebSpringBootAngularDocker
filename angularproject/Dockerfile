## ==============================================================================
## STAGE 1: BUILD ANGULAR APPLICATION
## Mục tiêu: Biên dịch ứng dụng Angular thành các tệp tĩnh sẵn sàng phục vụ.
## ==============================================================================
#FROM node:18-alpine AS angular-builder
#
## Đặt thư mục làm việc bên trong container.
## Tất cả các lệnh tiếp theo sẽ chạy trong thư mục này.
#WORKDIR /app
#
## Copy các tệp cấu hình dependencies trước để tận dụng Docker cache.
## Nếu package.json và package-lock.json không thay đổi, Docker sẽ sử dụng lại lớp này.
## Lệnh này chỉ copy các tệp này từ thư mục gốc của build context (angularproject/)
## vào thư mục /app bên trong container.
#COPY package.json package-lock.json ./
#
## Cài đặt tất cả các phụ thuộc của Node.js.
## '--legacy-peer-deps' giúp giải quyết các vấn đề tương thích phiên bản
## của các dependency trong môi trường Node.js mới hơn.
#RUN npm install --legacy-peer-deps
#
## Copy toàn bộ mã nguồn của ứng dụng Angular vào container.
## Lệnh này sẽ tôn trọng các quy tắc trong tệp .dockerignore.
#COPY . .
#
## Biên dịch ứng dụng Angular cho môi trường production.
## Kết quả build sẽ nằm trong thư mục 'dist/angularproject' bên trong /app,
## theo cấu hình 'outputPath' trong angular.json của bạn.
#RUN npm run build -- --configuration production
#
## ==============================================================================
## STAGE 2: SERVE WITH NGINX
## Mục tiêu: Tạo một lightweight image chỉ chứa Nginx và các tệp tĩnh đã build,
##           sẵn sàng để phục vụ ứng dụng Angular.
## ==============================================================================
#FROM nginx:alpine
#
## Xóa cấu hình Nginx mặc định để tránh xung đột hoặc cấu hình không cần thiết.
#RUN rm /etc/nginx/conf.d/default.conf
#
## (Tùy chọn) Copy file cấu hình Nginx tùy chỉnh của bạn.
## Nếu bạn có một file tên 'nginx.conf' trong thư mục 'angularproject/'
## để cấu hình Nginx, hãy bỏ comment dòng dưới đây:
## COPY ./nginx.conf /etc/nginx/conf.d/default.conf
#
## Copy các tệp tĩnh đã build từ giai đoạn 'angular-builder'.
## Chúng ta lấy output từ đường dẫn `/app/dist/angularproject`
## và sao chép vào thư mục mặc định của Nginx để phục vụ nội dung web.
#COPY --from=angular-builder /app/dist/angularproject /usr/share/nginx/html
#
## Mở cổng 80 của container.
## Đây là cổng mặc định mà Nginx sẽ lắng nghe các kết nối HTTP.
#EXPOSE 80
#
## Lệnh mặc định để khởi động Nginx ở chế độ foreground.
## Điều này đảm bảo container sẽ tiếp tục chạy miễn là Nginx đang chạy.
#CMD ["nginx", "-g", "daemon off;"]
#
##Cấu hình mới để sửa lỗi ko có file nginx để copy, thì angular mưới chạy đc cổng 80
FROM node:18-alpine AS angular-builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps
COPY . .
RUN npm run build

FROM nginx:alpine
RUN rm /etc/nginx/conf.d/default.conf
COPY --from=angular-builder /app/dist/angularproject/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

#Cấu hình mới để sửa lỗi ko có file nginx để copy, thì angular mưới chạy đc cổng 80
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

# build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm ci --silent
COPY public ./public
COPY src ./src
RUN npm run build

# production stage
FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html
# small default nginx conf is ok for single page app
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

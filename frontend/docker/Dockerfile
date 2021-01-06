FROM node:12.2.0-alpine as BUILD
WORKDIR /app
COPY package.json /app
RUN npm install
COPY ./ /app
RUN npm run-script build

FROM nginx:1.16.1-alpine
COPY --from=BUILD /app/build/ /usr/share/nginx/html
COPY --from=BUILD /app/nginx.conf /etc/nginx/conf.d/default.conf
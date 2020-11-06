cd backend || return
yarn install
yarn build
cd frontend || return
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=cs201-backend-docker:latest
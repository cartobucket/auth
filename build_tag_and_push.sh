./gradlew :web:build
./gradlew :web:imageBuild
docker tag revet/auth:latest harbor.revethq.com/revet/auth:latest
docker push harbor.revethq.com/revet/auth:latest

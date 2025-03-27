./gradlew :auth-single-server:build
./gradlew :auth-single-server:imageBuild
docker tag cartobucket/auth-single-server:latest harbor.cartobucket.com/cartobucket/auth-single-server:latest
docker push harbor.cartobucket.com/cartobucket/auth-single-server:latest

<!-- Start SDK Example Usage -->
```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateApplicationResponse;
import com.cartobucket.auth.api.models.shared.ApplicationRequest;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.ApplicationRequest req = new ApplicationRequest("corrupti", "provident") {{
                clientId = "distinctio";
                clientSecret = "quibusdam";
                profile = new java.util.HashMap<String, Object>() {{
                    put("nulla", "corrupti");
                    put("illum", "vel");
                    put("error", "deserunt");
                }};
            }};            

            CreateApplicationResponse res = sdk.applications.createApplication(req);

            if (res.applicationResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```
<!-- End SDK Example Usage -->
# templates

## Overview

Templates are used to render information such as the login page.

### Available Operations

* [createTemplate](#createtemplate)
* [deleteTemplate](#deletetemplate)
* [getTemplate](#gettemplate)
* [listTemplates](#listtemplates)
* [updateTemplate](#updatetemplate)

## createTemplate

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.CreateTemplateResponse;
import com.cartobucket.auth.api.models.shared.TemplateRequest;
import com.cartobucket.auth.api.models.shared.TemplateTypeEnumEnum;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            com.cartobucket.auth.api.models.shared.TemplateRequest req = new TemplateRequest("error", "quia", TemplateTypeEnumEnum.LOGIN);            

            CreateTemplateResponse res = sdk.templates.createTemplate(req);

            if (res.templateResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## deleteTemplate

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.DeleteTemplateRequest;
import com.cartobucket.auth.api.models.operations.DeleteTemplateResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            DeleteTemplateRequest req = new DeleteTemplateRequest("quis");            

            DeleteTemplateResponse res = sdk.templates.deleteTemplate(req);

            if (res.statusCode == 200) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## getTemplate

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.GetTemplateRequest;
import com.cartobucket.auth.api.models.operations.GetTemplateResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            GetTemplateRequest req = new GetTemplateRequest("vitae");            

            GetTemplateResponse res = sdk.templates.getTemplate(req);

            if (res.templateResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## listTemplates

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.ListTemplatesRequest;
import com.cartobucket.auth.api.models.operations.ListTemplatesResponse;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            ListTemplatesRequest req = new ListTemplatesRequest() {{
                authorizationServerIds = new String[]{{
                    add("animi"),
                    add("enim"),
                    add("odit"),
                }};
            }};            

            ListTemplatesResponse res = sdk.templates.listTemplates(req);

            if (res.templateResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

## updateTemplate

### Example Usage

```java
package hello.world;

import com.cartobucket.auth.api.SDK;
import com.cartobucket.auth.api.models.operations.UpdateTemplateRequest;
import com.cartobucket.auth.api.models.operations.UpdateTemplateResponse;
import com.cartobucket.auth.api.models.shared.TemplateRequest;
import com.cartobucket.auth.api.models.shared.TemplateTypeEnumEnum;

public class Application {
    public static void main(String[] args) {
        try {
            SDK sdk = SDK.builder()
                .build();

            UpdateTemplateRequest req = new UpdateTemplateRequest(                new TemplateRequest("quo", "sequi", TemplateTypeEnumEnum.LOGIN);, "tenetur");            

            UpdateTemplateResponse res = sdk.templates.updateTemplate(req);

            if (res.templateResponse != null) {
                // handle response
            }
        } catch (Exception e) {
            // handle exception
        }
    }
}
```

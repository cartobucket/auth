
/*
 * Copyright 2023 Bryce Groff (Cartobucket LLC)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cartobucket.auth.data.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* 
**/
public class JWK   {
    private String kid;
    private String kty;
    private String alg;
    private String use;
    private String n;
    private String e;
    private List<String> x5c;
    private String x5t;
    private String x5tHashS256;

    /**
    **/
    public JWK kid(String kid) {
    this.kid = kid;
    return this;
    }

      public String getKid() {
    return kid;
    }

    public void setKid(String kid) {
    this.kid = kid;
    }

    /**
    **/
    public JWK kty(String kty) {
    this.kty = kty;
    return this;
    }

      public String getKty() {
    return kty;
    }

    public void setKty(String kty) {
    this.kty = kty;
    }

    /**
    **/
    public JWK alg(String alg) {
    this.alg = alg;
    return this;
    }

      public String getAlg() {
    return alg;
    }

    public void setAlg(String alg) {
    this.alg = alg;
    }

    /**
    **/
    public JWK use(String use) {
    this.use = use;
    return this;
    }

      public String getUse() {
    return use;
    }

    public void setUse(String use) {
    this.use = use;
    }

    /**
    **/
    public JWK n(String n) {
    this.n = n;
    return this;
    }

      public String getN() {
    return n;
    }

    public void setN(String n) {
    this.n = n;
    }

    /**
    **/
    public JWK e(String e) {
    this.e = e;
    return this;
    }

      public String getE() {
    return e;
    }

    public void setE(String e) {
    this.e = e;
    }

    /**
    **/
    public JWK x5c(List<String> x5c) {
    this.x5c = x5c;
    return this;
    }

      public List<String> getX5c() {
    return x5c;
    }

    public void setX5c(List<String> x5c) {
    this.x5c = x5c;
    }

        public JWK addX5cItem(String x5cItem) {
        if (this.x5c == null) {
        this.x5c = new ArrayList<>();
        }

        this.x5c.add(x5cItem);
        return this;
        }

        public JWK removeX5cItem(String x5cItem) {
        if (x5cItem != null && this.x5c != null) {
        this.x5c.remove(x5cItem);
        }

        return this;
        }
    /**
    **/
    public JWK x5t(String x5t) {
    this.x5t = x5t;
    return this;
    }

      public String getX5t() {
    return x5t;
    }

    public void setX5t(String x5t) {
    this.x5t = x5t;
    }

    /**
    **/
    public JWK x5tHashS256(String x5tHashS256) {
    this.x5tHashS256 = x5tHashS256;
    return this;
    }

      public String getX5tHashS256() {
    return x5tHashS256;
    }

    public void setX5tHashS256(String x5tHashS256) {
    this.x5tHashS256 = x5tHashS256;
    }


@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    JWK JWK = (JWK) o;
    return Objects.equals(this.kid, JWK.kid) &&
    Objects.equals(this.kty, JWK.kty) &&
    Objects.equals(this.alg, JWK.alg) &&
    Objects.equals(this.use, JWK.use) &&
    Objects.equals(this.n, JWK.n) &&
    Objects.equals(this.e, JWK.e) &&
    Objects.equals(this.x5c, JWK.x5c) &&
    Objects.equals(this.x5t, JWK.x5t) &&
    Objects.equals(this.x5tHashS256, JWK.x5tHashS256);
}

@Override
public int hashCode() {
return Objects.hash(kid, kty, alg, use, n, e, x5c, x5t, x5tHashS256);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class JWK {\n");

sb.append("    kid: ").append(toIndentedString(kid)).append("\n");
sb.append("    kty: ").append(toIndentedString(kty)).append("\n");
sb.append("    alg: ").append(toIndentedString(alg)).append("\n");
sb.append("    use: ").append(toIndentedString(use)).append("\n");
sb.append("    n: ").append(toIndentedString(n)).append("\n");
sb.append("    e: ").append(toIndentedString(e)).append("\n");
sb.append("    x5c: ").append(toIndentedString(x5c)).append("\n");
sb.append("    x5t: ").append(toIndentedString(x5t)).append("\n");
sb.append("    x5tHashS256: ").append(toIndentedString(x5tHashS256)).append("\n");
sb.append("}");
return sb.toString();
}

/**
* Convert the given object to string with each line indented by 4 spaces
* (except the first line).
*/
private String toIndentedString(Object o) {
if (o == null) {
return "null";
}
return o.toString().replace("\n", "\n    ");
}


}

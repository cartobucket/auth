
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
public class JWKS   {
    private List<JWK> keys;

    /**
    **/
    public JWKS keys(List<JWK> keys) {
    this.keys = keys;
    return this;
    }

      public List<JWK> getKeys() {
    return keys;
    }

    public void setKeys(List<JWK> keys) {
    this.keys = keys;
    }

        public JWKS addKeysItem(JWK keysItem) {
        if (this.keys == null) {
        this.keys = new ArrayList<>();
        }

        this.keys.add(keysItem);
        return this;
        }

        public JWKS removeKeysItem(JWK keysItem) {
        if (keysItem != null && this.keys != null) {
        this.keys.remove(keysItem);
        }

        return this;
        }

@Override
public boolean equals(Object o) {
if (this == o) {
return true;
}
if (o == null || getClass() != o.getClass()) {
return false;
}
    JWKS JWKS = (JWKS) o;
    return Objects.equals(this.keys, JWKS.keys);
}

@Override
public int hashCode() {
return Objects.hash(keys);
}

@Override
public String toString() {
StringBuilder sb = new StringBuilder();
sb.append("class JWKS {\n");

sb.append("    keys: ").append(toIndentedString(keys)).append("\n");
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

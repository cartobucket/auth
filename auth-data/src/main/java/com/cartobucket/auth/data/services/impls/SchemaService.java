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

package com.cartobucket.auth.data.services.impls;

import com.cartobucket.auth.data.domain.Profile;
import com.cartobucket.auth.data.domain.Schema;
import com.cartobucket.auth.data.exceptions.notfound.SchemaNotFound;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@DefaultBean
@ApplicationScoped
public class SchemaService implements com.cartobucket.auth.data.services.SchemaService {

    @Override
    public Set<String> validateProfileAgainstSchema(Profile profile, Schema schema) {
        return null;
    }

    @Override
    public Schema createSchema(Schema schema) {
        return null;
    }

    @Override
    public void deleteSchema(UUID schemaId) throws SchemaNotFound {

    }

    @Override
    public Schema getSchema(UUID schemaId) throws SchemaNotFound {
        return null;
    }

    @Override
    public List<Schema> getSchemas(List<UUID> authorizationServerIds) {
        return null;
    }

    @Override
    public Schema updateSchema(UUID schemaId, Schema schema) throws SchemaNotFound {
        return null;
    }
}

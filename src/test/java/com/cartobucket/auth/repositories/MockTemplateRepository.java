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

package com.cartobucket.auth.repositories;

import com.cartobucket.auth.model.generated.TemplateTypeEnum;
import com.cartobucket.auth.models.Template;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(1)
@Alternative
@ApplicationScoped
public class MockTemplateRepository implements TemplateRepository {

    @Override
    public List<Template> findAllByAuthorizationServerId(UUID authorizationServer) {
        throw new NotImplementedException();
    }

    @Override
    public List<Template> findAllByAuthorizationServerIdAndTemplateType(UUID authorizationServer, TemplateTypeEnum templateType) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Template> findByAuthorizationServerIdAndTemplateType(UUID authorizationServer, TemplateTypeEnum templateType) {
        throw new NotImplementedException();
    }

    @Override
    public List<Template> findAllByAuthorizationServerIdIn(List<UUID> authorizationServerIds) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends Template> S save(S entity) {
        throw new NotImplementedException();
    }

    @Override
    public <S extends Template> Iterable<S> saveAll(Iterable<S> entities) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Template> findById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public boolean existsById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Template> findAll() {
        throw new NotImplementedException();
    }

    @Override
    public Iterable<Template> findAllById(Iterable<UUID> uuids) {
        throw new NotImplementedException();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Template entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll(Iterable<? extends Template> entities) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException();
    }
}

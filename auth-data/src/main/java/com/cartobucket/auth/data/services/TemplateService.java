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

package com.cartobucket.auth.data.services;

import com.cartobucket.auth.data.domain.Page;
import com.cartobucket.auth.data.domain.Template;
import com.cartobucket.auth.data.exceptions.notfound.TemplateNotFound;

import java.util.List;
import java.util.UUID;

public interface TemplateService {
    List<Template> getTemplates(final List<UUID> authorizationServerIds, Page page);

    Template createTemplate(final Template template);

    void deleteTemplate(final UUID templateId) throws TemplateNotFound;

    Template getTemplate(final UUID templateId) throws TemplateNotFound;

//    Template getTemplateForAuthorizationServer(UUID authorizationServer, TemplateTypeEnum templateType);

    Template updateTemplate(final UUID templateId, Template template) throws TemplateNotFound;
}

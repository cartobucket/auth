--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5 (Debian 14.5-1.pgdg110+1)
-- Dumped by pg_dump version 14.5 (Debian 14.5-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: application; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.application (
                                    id uuid NOT NULL,
                                    authorizationserverid uuid,
                                    clientid character varying(255),
                                    createdon timestamp without time zone,
                                    name character varying(255),
                                    updatedon timestamp without time zone
);


ALTER TABLE public.application OWNER TO "cartobucket-auth";

--
-- Name: applicationsecret; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.applicationsecret (
                                          id uuid NOT NULL,
                                          applicationid uuid,
                                          applicationsecrethash character varying(255),
                                          authorizationserverid uuid,
                                          createdon timestamp without time zone,
                                          name character varying(255),
                                          scopes jsonb,
                                          updatedon timestamp without time zone
);


ALTER TABLE public.applicationsecret OWNER TO "cartobucket-auth";

--
-- Name: authorizationserver; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.authorizationserver (
                                            id uuid NOT NULL,
                                            audience character varying(255),
                                            authorizationcodetokenexpiration bigint,
                                            clientcredentialstokenexpiration bigint,
                                            createdon timestamp without time zone,
                                            name character varying(255),
                                            serverurl character varying(255),
                                            updatedon timestamp without time zone
);


ALTER TABLE public.authorizationserver OWNER TO "cartobucket-auth";

--
-- Name: client; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.client (
                               id uuid NOT NULL,
                               authorizationserverid uuid,
                               createdon timestamp without time zone,
                               name character varying(255),
                               redirecturis jsonb,
                               scopes jsonb,
                               updatedon timestamp without time zone
);


ALTER TABLE public.client OWNER TO "cartobucket-auth";

--
-- Name: clientcode; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.clientcode (
                                   id uuid NOT NULL,
                                   authorizationserverid uuid,
                                   clientid uuid,
                                   code character varying(255),
                                   codechallenge character varying(255),
                                   codechallengemethod character varying(255),
                                   createdon timestamp without time zone,
                                   nonce character varying(255),
                                   redirecturi character varying(255),
                                   scopes jsonb,
                                   state character varying(255),
                                   userid uuid
);


ALTER TABLE public.clientcode OWNER TO "cartobucket-auth";

--
-- Name: profile; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.profile (
                                id uuid NOT NULL,
                                authorizationserverid uuid,
                                createdon timestamp without time zone,
                                profile jsonb,
                                profiletype integer,
                                resource uuid,
                                updatedon timestamp without time zone
);


ALTER TABLE public.profile OWNER TO "cartobucket-auth";

--
-- Name: scope; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.scope (
                              id uuid NOT NULL,
                              authorizationserverid uuid,
                              createdon timestamp without time zone,
                              name character varying(255),
                              updatedon timestamp without time zone
);


ALTER TABLE public.scope OWNER TO "cartobucket-auth";

--
-- Name: signingkey; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.signingkey (
                                   id uuid NOT NULL,
                                   authorizationserverid uuid,
                                   createdon timestamp without time zone,
                                   keytype character varying(255),
                                   metadata jsonb,
                                   privatekey character varying(255),
                                   publickey character varying(255),
                                   updatedon timestamp without time zone
);


ALTER TABLE public.signingkey OWNER TO "cartobucket-auth";

--
-- Name: template; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.template (
                                 id uuid NOT NULL,
                                 authorizationserverid uuid,
                                 createdon timestamp without time zone,
                                 template bytea,
                                 templatetype integer,
                                 updatedon timestamp without time zone
);


ALTER TABLE public.template OWNER TO "cartobucket-auth";

--
-- Name: users; Type: TABLE; Schema: public; Owner: cartobucket-auth
--

CREATE TABLE public.users (
                              id uuid NOT NULL,
                              authorizationserverid uuid,
                              createdon timestamp without time zone,
                              email character varying(255),
                              passwordhash character varying(255),
                              updatedon timestamp without time zone,
                              username character varying(255)
);


ALTER TABLE public.users OWNER TO "cartobucket-auth";

--
-- Data for Name: application; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.application (id, authorizationserverid, clientid, createdon, name, updatedon) FROM stdin;
\.


--
-- Data for Name: applicationsecret; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.applicationsecret (id, applicationid, applicationsecrethash, authorizationserverid, createdon, name, scopes, updatedon) FROM stdin;
\.


--
-- Data for Name: authorizationserver; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.authorizationserver (id, audience, authorizationcodetokenexpiration, clientcredentialstokenexpiration, createdon, name, serverurl, updatedon) FROM stdin;
4787ae72-0f33-4230-9c89-d15e5c54e31a	api://	300	100	2023-01-20 04:50:33.557467	Admin Console	http://localhost:8080	2023-01-20 04:50:33.557518
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.client (id, authorizationserverid, createdon, name, redirecturis, scopes, updatedon) FROM stdin;
\.


--
-- Data for Name: clientcode; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.clientcode (id, authorizationserverid, clientid, code, codechallenge, codechallengemethod, createdon, nonce, redirecturi, scopes, state, userid) FROM stdin;
\.


--
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.profile (id, authorizationserverid, createdon, profile, profiletype, resource, updatedon) FROM stdin;
\.


--
-- Data for Name: scope; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.scope (id, authorizationserverid, createdon, name, updatedon) FROM stdin;
\.


--
-- Data for Name: signingkey; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.signingkey (id, authorizationserverid, createdon, keytype, metadata, privatekey, publickey, updatedon) FROM stdin;
\.


--
-- Data for Name: template; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.template (id, authorizationserverid, createdon, template, templatetype, updatedon) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.users (id, authorizationserverid, createdon, email, passwordhash, updatedon, username) FROM stdin;
\.


--
-- Name: application application_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.application
    ADD CONSTRAINT application_pkey PRIMARY KEY (id);


--
-- Name: applicationsecret applicationsecret_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.applicationsecret
    ADD CONSTRAINT applicationsecret_pkey PRIMARY KEY (id);


--
-- Name: authorizationserver authorizationserver_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.authorizationserver
    ADD CONSTRAINT authorizationserver_pkey PRIMARY KEY (id);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: clientcode clientcode_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.clientcode
    ADD CONSTRAINT clientcode_pkey PRIMARY KEY (id);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (id);


--
-- Name: scope scope_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.scope
    ADD CONSTRAINT scope_pkey PRIMARY KEY (id);


--
-- Name: signingkey signingkey_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.signingkey
    ADD CONSTRAINT signingkey_pkey PRIMARY KEY (id);


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


--
-- Name: template template_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.template
    ADD CONSTRAINT template_pkey PRIMARY KEY (id);


--
-- Name: scope uk1ap3p7o5m5lpluvoxd1vcdumm; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.scope
    ADD CONSTRAINT uk1ap3p7o5m5lpluvoxd1vcdumm UNIQUE (authorizationserverid, name);


--
-- Name: template uk61losoa0neevj9i82j54a7c91; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.template
    ADD CONSTRAINT uk61losoa0neevj9i82j54a7c91 UNIQUE (authorizationserverid, templatetype);


--
-- Name: profile ukk44wj9sin1h9ecgtphtrc2250; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.profile
    ADD CONSTRAINT ukk44wj9sin1h9ecgtphtrc2250 UNIQUE (id, profiletype);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: cartobucket-auth
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.1 (Debian 15.1-1.pgdg110+1)
-- Dumped by pg_dump version 15.1 (Debian 15.1-1.pgdg110+1)

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
                                   privatekey text,
                                   publickey text,
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
7c0cc6db-2878-4008-a8a3-2daefa3a59f8	52a10a2d-080c-4aaa-a184-ec3ba982a9bd	7c0cc6db-2878-4008-a8a3-2daefa3a59f8	2023-01-20 18:02:37.410953	Admin Application	2023-01-20 18:02:37.410999
\.


--
-- Data for Name: applicationsecret; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.applicationsecret (id, applicationid, applicationsecrethash, authorizationserverid, createdon, name, scopes, updatedon) FROM stdin;
52c9c510-c836-4914-a6c6-f4d1b67a0334	7c0cc6db-2878-4008-a8a3-2daefa3a59f8	d0e00eecd7ab0cbd3d7f0a0f1c77f2e5d453b706f0d9551198e32fbfe80ff6cc	52a10a2d-080c-4aaa-a184-ec3ba982a9bd	2023-01-20 19:55:02.68647	Admin Secret	[]	2023-01-20 19:55:02.686436
\.


--
-- Data for Name: authorizationserver; Type: TABLE DATA; Schema: public; Owner: cartobucket-auth
--

COPY public.authorizationserver (id, audience, authorizationcodetokenexpiration, clientcredentialstokenexpiration, createdon, name, serverurl, updatedon) FROM stdin;
1d96275b-39e9-471c-9cc7-ef664d83ed63	api://	300	100	2023-01-20 05:44:46.233917	Admin Console	http://localhost:8080	2023-01-20 05:44:46.233957
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
a38db391-b9f6-4f36-bdab-492842bd4af1	1d96275b-39e9-471c-9cc7-ef664d83ed63	2023-01-20 18:02:37.410953	{"sub": "7c0cc6db-2878-4008-a8a3-2daefa3a59f8"}	1	7c0cc6db-2878-4008-a8a3-2daefa3a59f8	2023-01-20 18:02:37.410999
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
3b44627c-99b7-47ef-88e1-cc53309c598e	1d96275b-39e9-471c-9cc7-ef664d83ed63	2023-01-20 05:44:46.436907	RSA	{}	-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDD0hBRSRfFXr029d3H2CxBag6z\r\nRIb2ss+gC1dZYOxJVLbODtDgd0bm1zx7fMy6CmIObZW2h5AEOViMphmJZSg71Im8OVP+9FSwN3P7\r\nHwuho98YlHqBaE7HGli+jjmmYXqYrB4nJSVtSTb2SqiqbgOLs9OTCbrf4qXV9ArVUVMumLEqKWK5\r\nrl72i88nTaEolTTJRMqHuI8puzmj9y8wvAkDDGAkrsWhhw5H3MHOMOtii2t9c+l6MKlTB21qMiGl\r\nHyYiepqd+gofJs2EKeWye1vl0AQPh86t10f/WUZDwKq/HJ8OLhCvdiVQn+K1hvR8YpcgkC0RmDm7\r\n2iXsW/dGjz0dAgMBAAECggEAFz5hinctzP3sYXrjavEpe6WeU2rQcbW9jcaa2on13NR4kyf7ZObe\r\nRyYIULN6d6DQ4oaLzVaft9NOGU/ZWyTjQYrTw0BeIeFtsLG8YMlexOk3NkCK9BO7ZMh8p+Mo9lYU\r\nH82FEhTHhwaNnBmLlhiomhYWiJm7PA+4AJf5teLJMrq0QqCx7IWU9mDJ2y9Ou0BP9Db7erEzviOu\r\nN2qVZUJwA3WReUluIgT2KfRUCkqcGOTn20CVzy8r1DkFc3bgzU7v1MFdj7Neao2Cl2khT+l+ZoC3\r\nfekc+/J5U1Ikj48vcN8ptiFeuJ9Y9VF5fHgSVb04HFG5JbKMcKd9bH/5ii2wbwKBgQDSlFH4tJcX\r\nk4Q+FgYYjND8CqMYn/h5GqBLIvcOFw6/FaZwCl9CrzcYVhW1ckxt7fywE9wUyJOJMSDZxOqhwlgk\r\nL7SJpt424Wp6fztFNPD9tpaQiNbQeOgIKURZUng5hmj5LXWy/GJ184bC8t3PFSDz6pcn6igrhpIL\r\n8ECSG5thCwKBgQDuDsw8C5F2eubetG2uxlk5ZBYTGwEz/SrK8tv1Sxxfg16+f4Sg2djp/CkPh9pt\r\n4k6XQHym5fuPrdDS9NXkFfgyE3ebkV0Zws8HKZnsn2NAYANEi5G/+wAtYDt3F0G0/pZqxSgd7Ftx\r\n0zEykF0iVo8gFgTGZ+NL1Vset+yTgNYDdwKBgQC+EeZTSB3AyAPrTlG+Qww3ExrXlTzfgqsAZtT8\r\nQM+spkwOgS4usJxYOQtrNXnLF66m96YElG6mvFNcfKMT0qlgMDcSsC2O97P2UXUcKIcFWpNYbksG\r\n7Lry8tygixG9SpbhoqjEH453zJHs0O5ohjeyMiKxgmczskmMwRRnTMfYtQKBgGVEqD720Nds6t9c\r\nM7iAqh4O5JEWEMnz7YtOCqHrZNOAyAITrvDaGztWSIVRzmqmNbaD+ULqXLw/PbEv8/lGG0H6JAxq\r\npwc5ZuZuHQuathcBgQJcuRDJDsc+w60imJINJyGeYzVvgWsHY0tWPDhKSDJnC3r3YKq2xT9pEFuj\r\nF+d7AoGBAIp8fgafGBWfHk0BBXXQczM65/Gt3k1fxNMD8vff2QanWrvdj78X9/la9QwV5Sh7oXAb\r\ngE84KEx7FOVljHmxDzZaOelkiRZLaqKQlyc/PoUpjI1ZdD9jvqpsMc0AvQ//pyXrDl+46EDJCd+q\r\nuKcoNQ50nc7jyto3y9ePj4LUnTCu\n-----END PRIVATE KEY-----\n	-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw9IQUUkXxV69NvXdx9gsQWoOs0SG9rLP\r\noAtXWWDsSVS2zg7Q4HdG5tc8e3zMugpiDm2VtoeQBDlYjKYZiWUoO9SJvDlT/vRUsDdz+x8LoaPf\r\nGJR6gWhOxxpYvo45pmF6mKweJyUlbUk29kqoqm4Di7PTkwm63+Kl1fQK1VFTLpixKiliua5e9ovP\r\nJ02hKJU0yUTKh7iPKbs5o/cvMLwJAwxgJK7FoYcOR9zBzjDrYotrfXPpejCpUwdtajIhpR8mInqa\r\nnfoKHybNhCnlsntb5dAED4fOrddH/1lGQ8CqvxyfDi4Qr3YlUJ/itYb0fGKXIJAtEZg5u9ol7Fv3\r\nRo89HQIDAQAB\n-----END PUBLIC KEY-----\n	2023-01-20 05:44:46.436941
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

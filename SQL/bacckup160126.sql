--
-- PostgreSQL database dump
--

\restrict dR7GfLlTKReDCUcPzdQhb0eFUx20KmtcsZKGfICVm0qhBYIxb0fMxLNwV7PKxTX

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

-- Started on 2026-01-16 01:29:21

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 4968 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 223 (class 1259 OID 16487)
-- Name: card; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card (
    id uuid NOT NULL,
    identity_hash character(64) NOT NULL,
    identity_version integer NOT NULL,
    public_id character varying(40) NOT NULL,
    name character varying(100) NOT NULL,
    card_type character varying(30) NOT NULL,
    faction character varying(30) NOT NULL,
    energy_cost integer NOT NULL,
    attack integer,
    defense integer,
    power_score integer NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    enabled boolean DEFAULT true NOT NULL
);


ALTER TABLE public.card OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16419)
-- Name: card_effect; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_effect (
    id uuid NOT NULL,
    card_id uuid NOT NULL,
    trigger character varying(50) NOT NULL,
    condition_keyword character varying(50),
    ability character varying(50) NOT NULL,
    value integer,
    target_type character varying(50) NOT NULL
);


ALTER TABLE public.card_effect OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16434)
-- Name: card_effect_constraint; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_effect_constraint (
    effect_id uuid NOT NULL,
    constraint_type character varying(50) NOT NULL
);


ALTER TABLE public.card_effect_constraint OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16407)
-- Name: card_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_keyword (
    card_id uuid NOT NULL,
    keyword character varying(50) NOT NULL
);


ALTER TABLE public.card_keyword OWNER TO postgres;

--
-- TOC entry 4811 (class 2606 OID 16440)
-- Name: card_effect_constraint card_effect_constraint_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_effect_constraint
    ADD CONSTRAINT card_effect_constraint_pkey PRIMARY KEY (effect_id, constraint_type);


--
-- TOC entry 4809 (class 2606 OID 16428)
-- Name: card_effect card_effect_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_effect
    ADD CONSTRAINT card_effect_pkey PRIMARY KEY (id);


--
-- TOC entry 4807 (class 2606 OID 16413)
-- Name: card_keyword card_keyword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_keyword
    ADD CONSTRAINT card_keyword_pkey PRIMARY KEY (card_id, keyword);


--
-- TOC entry 4813 (class 2606 OID 16504)
-- Name: card card_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card
    ADD CONSTRAINT card_pkey PRIMARY KEY (id);


--
-- TOC entry 4814 (class 1259 OID 16505)
-- Name: uq_card_identity; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uq_card_identity ON public.card USING btree (identity_hash, identity_version);


--
-- TOC entry 4815 (class 2606 OID 16441)
-- Name: card_effect_constraint card_effect_constraint_effect_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_effect_constraint
    ADD CONSTRAINT card_effect_constraint_effect_id_fkey FOREIGN KEY (effect_id) REFERENCES public.card_effect(id) ON DELETE CASCADE;


-- Completed on 2026-01-16 01:29:21

--
-- PostgreSQL database dump complete
--

\unrestrict dR7GfLlTKReDCUcPzdQhb0eFUx20KmtcsZKGfICVm0qhBYIxb0fMxLNwV7PKxTX


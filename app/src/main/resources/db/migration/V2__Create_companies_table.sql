CREATE TABLE public.company
(
    id bigserial NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(200) NOT NULL,
    tax_identification_number character varying(15) NOT NULL,
    pension_insurance numeric(10, 2) NOT NULL DEFAULT 0,
    health_insurance numeric(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);
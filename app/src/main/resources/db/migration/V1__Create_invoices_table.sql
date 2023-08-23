CREATE TABLE public.invoice
(
    id bigserial NOT NULL,
    number character varying(50) NOT NULL,
    date date NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE public.invoices
(
    id bigserial NOT NULL,
    "number" character varying(50) NOT NULL,
    "date" date NOT NULL,
    PRIMARY KEY (id)
);
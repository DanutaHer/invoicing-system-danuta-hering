CREATE TABLE public.invoice_entry
(
    id bigserial NOT NULL,
    description character varying(200) NOT NULL,
    price numeric(10, 2) NOT NULL DEFAULT 0,
    vat_value numeric(10, 2) NOT NULL DEFAULT 0,
    vat_rate bigint NOT NULL,
    expense_related_to_car bigint NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.invoice_entry
    ADD CONSTRAINT vat_rate_fk FOREIGN KEY (vat_rate)
        REFERENCES public.vat (id);
# Invoicing

Models an invoice and the parties involved in it, and issues invoices through pluggable external Invoicing Providers.

## Language

**Party**:
A person or company that can act as an issuer, recipient, or payer on an invoice.
_Avoid_: Entity

**Issuer**:
The Party who issues the invoice.

**Recipient**:
The Party the invoice is addressed to.

**Payer**:
The Party responsible for paying the invoice, when it differs from the Recipient (e.g. parent-company or cost-center billing). When absent, the Recipient is the payer.

**Invoice Number**:
The human-facing, sequential identifier assigned to an invoice (e.g. by the Invoicing Provider), shown to clients and tax authorities.
_Avoid_: invoiceId used interchangeably with the internal id

**Invoicing Provider**:
An external system (e.g. fakturownia, KSeF) integrated with to issue — and, eventually, receive — invoices.
_Avoid_: Issuer (reserved for the issuing capability/port itself, not the provider as a whole)

**Invoice Position**:
A single line item on an invoice, describing a priced unit of work or goods.

## Decisions

See [docs/adr/](./docs/adr/) for the reasoning behind hard-to-reverse or non-obvious choices in this context.

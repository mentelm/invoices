# Context Map

## Contexts

- [Time Tracking](./toggl/CONTEXT.md) — pulls raw time entries and groups them into Work Items
- [Invoicing](./domain/CONTEXT.md) — models invoices and issues them through pluggable Invoicing Providers (fakturownia, KSeF)

## Relationships

- **Time Tracking → Invoicing**: a Work Item is meant to become an `InvoicePosition` line item on an invoice. This link is not yet wired in code — `GeneratePositions` does not currently consume `TimeEntrySummary`.

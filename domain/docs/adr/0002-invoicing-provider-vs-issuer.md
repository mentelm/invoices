# "Invoicing Provider" names the external system, "Issuer" names just the issuing capability

`fakturownia` and `KSeF` currently only issue invoices through the `IssueInvoice` port, but both are expected to eventually also receive invoices back. We reserved "Invoicing Provider" as the glossary term for the external system as a whole, keeping "Issuer" for the issuing capability/port specifically — so adding a future receiving capability won't force another rename of the provider concept.

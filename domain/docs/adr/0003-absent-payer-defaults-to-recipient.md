# Absent Payer defaults to the Recipient paying

`Invoice` models an optional third-party `Payer` distinct from the `Recipient`, to support cases like parent-company or cost-center billing. We decided that when `payer` is absent, the `Recipient` is implicitly responsible for payment, rather than requiring every invoice to state a payer explicitly — a reader seeing `Optional<Party> payer` might otherwise assume an empty payer is an error case rather than the common one.

# Rename `Entity` to `Party` in code, not just in docs

The abstract class representing an invoice issuer/recipient/payer was called `Entity`, a term already overloaded by generic DDD/ORM jargon. Rather than only recording "Party" as the preferred glossary term, we renamed the class itself (and its usages across `domain`, `fakturownia`, and `ksef`) so the code and the glossary can't drift apart — a future reader won't find `Entity` in the source and wonder why the docs call it something else.

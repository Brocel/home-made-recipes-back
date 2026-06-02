# Observability instructions

## Logging

- Use structured, meaningful logs.
- Log business-relevant context without leaking sensitive information.
- Keep log messages clear and stable.
- Use the appropriate log level for the event.

## Diagnostics

- Prefer correlation identifiers where available.
- Make operational failures easy to diagnose.
- Keep startup and health-check behavior predictable.
- Add metrics or tracing hooks when a change affects a critical path or an external integration.

## Runtime behavior

- Avoid noisy logs in hot paths.
- Make failure modes visible and actionable.
- Keep operational code simple and explicit.
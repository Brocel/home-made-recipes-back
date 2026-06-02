# Security instructions

## Input and output

- Assume all external input is untrusted.
- Validate and sanitize inputs at the boundary.
- Never hardcode secrets.
- Never log credentials, tokens, or sensitive personal data.
- Do not echo sensitive values in error messages.

## Authentication and authorization

- Validate authorization explicitly at the boundary.
- Keep authentication logic isolated and easy to audit.
- Prefer least-privilege access for service accounts and integrations.
- Do not weaken access checks to make tests pass.

## Implementation rules

- Prefer secure defaults in configuration.
- Avoid ad hoc crypto or custom security mechanisms.
- Keep JWT and authentication logic explicit and testable.
- Treat security-related changes as high priority and high risk.
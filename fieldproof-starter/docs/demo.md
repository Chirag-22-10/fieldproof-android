# Client demonstration (planned)

**Demo only:** use synthetic inspection data. This walkthrough is not evidence of production readiness or safety certification.

1. Open the synthetic facility inspection template and create an inspection.
2. Enable airplane mode.
3. Complete several checklist items, capture a photo, and add a note.
4. Attempt to submit with one required photo missing; confirm submission is blocked and the missing item is identified.
5. Force-stop/restart the app while offline; confirm inspection, note, and photo remain.
6. Reconnect and trigger synchronization.
7. Simulate an interrupted upload; confirm the local evidence remains and the failure is actionable.
8. Retry using the same operation ID; verify the backend contains one evidence record.
9. Resolve any version conflict explicitly if the scenario is enabled.
10. Review and approve the inspection, then export the PDF.
11. Show test results and known limitations.

## Limitations to disclose

- Synthetic data only.
- No autonomous safety or compliance certification.
- No AI-generated compliance decisions.
- No iOS client or background location tracking.
- Any deployment, security, or performance claims must be supported by actual test evidence.

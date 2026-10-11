# Sync state machine (initial proposal)

## Evidence sync states

- `queued`: durable local operation exists; not currently uploading.
- `uploading`: a worker has claimed the operation.
- `synced`: server acknowledged the idempotent operation and remote reference is stored locally.
- `failed`: last attempt failed; local evidence remains available and retry is offered.
- `needs_resolution`: server returned a conflict that cannot be safely retried without user input.

Transitions:

```text
queued -> uploading
uploading -> synced
uploading -> queued        (transient failure / retry scheduled)
uploading -> failed        (retry budget exhausted or actionable permanent error)
uploading -> needs_resolution (version conflict)
failed -> queued           (user retry or retry policy)
needs_resolution -> queued (after explicit resolution creates a new operation)
```

## Retry policy

Use WorkManager network constraints and exponential backoff for transient errors. Keep retry counts and last error codes in the local database. Do not delete local files merely because an upload was attempted. Avoid endless retries for permanent errors such as unsupported media type or authorization denial; show a useful action instead.

## Upload uncertainty

If the server stores an upload but the response is lost, the client retries with the same idempotency key. The server returns the prior result instead of creating a duplicate. WorkManager alone does not provide exactly-once delivery.

## Conflict policy

Never silently overwrite. Return `409`, show the user's local version and server version, and require a deliberate resolution. Record the selected resolution as a new versioned operation for auditability.

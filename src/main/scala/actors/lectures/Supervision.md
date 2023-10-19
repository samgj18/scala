# Supervision

## Life Cycle

When an actor fails:

1. Suspends its children
2. Sends a special message to its parent through a dedicated channel

The parent can then decide what to do:

- Resume the actor
- Restart the actor (default) (clears the state)
- Stop the actor
- Escalate the failure to its parent

# Actor Lifecycle

## Actor Instance

- [x] Has methods and fields
- [x] May have internal state

## Actor Reference (a.k.a. incarnation)

- [x] Created with `ActorSystem.actorOf`
- [x] Has a mailbox and can receive messages
- [x] Contains one Actor Instance at any given time
- [x] Contains a UUID

## Actor Path

- [x] Created with `ActorSystem.actorSelection`

## Actor Lifecycle

- Started
- Suspended
- Resumed
- Restarted
- Stopped

### Started

Create a new ActorRef with a UUID at the specified path.

### Suspended

The actor `will` enqueue messages to its mailbox, but `will not` process them.

### Resumed

The actor `will` process messages from its mailbox.

### Restarted

- The actor `will` be suspended.
- The Actor Instance `will` be swapped:
    - The old Actor Instance `will` call `preRestart`.
    - Replace the old Actor Instance with a new Actor Instance.
    - The new Actor Instance `will` call `postRestart`.
- The actor `will` be resumed.

**Note:** The internal state of the Actor Instance is not preserved. Stopping
frees the actor ref within the path.

- Call `postStop` on the old Actor Instance.
- All watched actors `will` receive a `Terminated` message.


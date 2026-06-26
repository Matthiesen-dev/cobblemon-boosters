# Dev Notes

## Jun 25 2026

### New Features

#### Queue Priority

Boost queues can now be globally sorted by priority instead of FIFO.

- Default behavior stays FIFO until `queuePriorityEnabled` is set to `true`.
- `queuePriorityMode` supports `FIFO`, `MULTIPLIER`, and `TIME_REMAINING`.
- `timePriorityDirection` supports `SHORTEST_FIRST` (default) and `LONGEST_FIRST`.
- `activePreemptionEnabled` must be `true` for a newly queued higher-priority boost to replace an active boost.

You can also update these settings at runtime with `/boosters queue-priority`.
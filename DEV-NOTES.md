# Dev Notes

## Jul 03 2026

### New Features

#### Display Options

By default, an active booster is shown as a vanilla **boss bar** at the top of the screen.
This can be changed with the `displayMode` option in `config/cobblemon_boosters/core.json`:

| `displayMode` | Result |
|---------------|--------|
| `BOSSBAR`     | Boss bar per active boost (default). |
| `SIDEBAR`     | Vanilla scoreboard sidebar on the right, one line per active boost. |
| `NONE`        | No on-screen display. |

The sidebar title and per-boost line text are configurable in
`config/cobblemon_boosters/messages.json` (`sidebarTitle` and each boost's `sidebarLine`,
which support the same placeholders as `barText`). Changes apply on `/boosters reload`.

> The sidebar uses the server-global scoreboard, so it can conflict with other mods/plugins
> that use the sidebar slot.

## Jun 25 2026

### New Features

#### Queue Priority

Boost queues can now be globally sorted by priority instead of FIFO.

- Default behavior stays FIFO until `queuePriorityEnabled` is set to `true`.
- `queuePriorityMode` supports `FIFO`, `MULTIPLIER`, and `TIME_REMAINING`.
- `timePriorityDirection` supports `SHORTEST_FIRST` (default) and `LONGEST_FIRST`.
- `activePreemptionEnabled` must be `true` for a newly queued higher-priority boost to replace an active boost.

You can also update these settings at runtime with `/boosters queue-priority`.
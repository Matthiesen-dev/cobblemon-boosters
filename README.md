# Cobblemon Boosters

This is a Server side mod that adds different types of timed boosters to your server. These boosters can be used to increase the 
chances of finding Shiny Pokemon, or increases the catch rate!

### Currently supported Booster types:
- **Catch Boosters**: Increases the catch rate for all players on the server.
- **Shiny Boosters**: Increases the chances of finding shiny Pokemon for all players on the

## Requirements
- [Cobblemon 1.7.3+1.21.1](https://www.modrinth.com/mod/cobblemon)
- [Adventure API for 1.21.1](https://modrinth.com/mod/adventure-platform-mod)

## Configuration

The configuration file is located at `config/cobblemon_boosters/config.json`. This file contains settings for the permission level defaults, 
and message configurations. The configuration file is automatically generated with default values if it does not exist.

This config file is also the location where you can figure how Discord webhooks are sent, and customize the messages sent to Discord when a booster is started or stopped. 
You can also customize the in-game messages sent to players when a booster is started, stopped, or when they check the status of the active boosters.

This file is also used to store and cache the active and queued boosts between server restarts, so it is important to not delete this file if you want to keep 
your active boosts between restarts. And you **will not be able to directly edit the file while the server is running as the config file will be overwritten at server stop.**

## Commands

- `/boosters` - Root command for boosters.
- `/boosters reload` - Reloads the boosters configuration.
  - Requires the `cobblemon_boosters.command.boosters.reload` permission (Defaults to OP status)
- `/boosters clear-queues` - Clears all active booster queues.
  - Requires the `cobblemon_boosters.command.boosters.clear_queues` permission. (Defaults to OP status)
- `/boosters check-queues` - Checks the status of all active booster queues.
  - Requires the `cobblemon_boosters.command.boosters.check_queues` permission. (Defaults to Any User)
- `/boosters catch` - The Root command for catch boosters.
  - `/boosters catch start <multiplier> <duration> <unit>` - Starts a catch booster with the specified multiplier, duration, and time unit.
    - Requires the `cobblemon_boosters.command.boosters.catch.start` permission. (Defaults to OP status)
  - `/boosters catch stop` - Stops the active catch booster.
    - Requires the `cobblemon_boosters.command.boosters.catch.stop` permission. (Defaults to OP status)
  - `/boosters catch status` - Checks the status of the active catch booster.
    - Requires the `cobblemon_boosters.command.boosters.catch.status` permission. (Defaults to Any User)
- `/boosters shiny` - The Root command for shiny boosters.
  - `/boosters shiny start <multiplier> <duration> <unit>` - Starts a shiny booster with the specified multiplier, duration, and time unit.
    - Requires the `cobblemon_boosters.command.boosters.shiny.start` permission. (Defaults to OP status)
  - `/boosters shiny stop` - Stops the active shiny booster.
    - Requires the `cobblemon_boosters.command.boosters.shiny.stop` permission. (Defaults to OP status)
  - `/boosters shiny status` - Checks the status of the active shiny booster.
    - Requires the `cobblemon_boosters.command.boosters.shiny.status` permission. (Defaults to Any User)
# Cobblemon Boosters

<div>
  <img src="https://mods.matthiesen.dev/badges/matthiesenCore.svg" alt="Matthiesen Core">
  <img src="https://mods.matthiesen.dev/badges/cobblemon.svg" alt="Cobblemon">
</div>

This is a Server side mod that adds different types of timed boosters to your server. These boosters can be used to increase the 
chances of finding Shiny Pokemon, or increases the catch rate!

### Currently supported Booster types:
- **Catch Boosters**: Increases the catch rate for all players on the server.
- **Experience Boosters**: Increases the experience gain for all player's Pokemon on the server.
- **Shiny Boosters**: Increases the chances of finding shiny Pokemon for all players on the server.
- **Spawn Bucket Boosters**: Increases the spawn rates for Pokemon in a specific spawn bucket for all players on the server.

## Requirements

- [Matthiesen Core](https://www.modrinth.com/mod/matthiesen-core)
- [Cobblemon](https://www.modrinth.com/mod/cobblemon)

### Optional Dependencies
- [Matthiesen Core Webhooks](https://modrinth.com/project/XP5CfD30) - Used for sending Discord webhooks when boosters are started or stopped.
- [GooeyLibs 3.1.1-1.21.x](https://modrinth.com/mod/gooeylibs) - Used for optional GUIs

## Docs

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/cobblemon-boosters/)

## Version Compatibility

| Minecraft Version | Cobblemon Version | Mod Version |
|-------------------|-------------------|-------------|
| 1.21.1            | 1.7.3             | 1.x.x       |

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Cobblemon Boosters](https://faststats.dev/project/cobblemon-boosters) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_core/metrics.properties` file.

## License

MIT - see `LICENSE`.
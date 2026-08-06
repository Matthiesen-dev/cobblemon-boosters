package dev.matthiesen.cobblemon_boosters.common.config.def;

import java.util.List;

public record DiscordEmbed(
        String title,
        String description,
        Integer color,
        DiscordAuthor author,
        List<DiscordEmbedField> fields,
        String timestamp
) {

    public record DiscordAuthor(
            String name,
            String icon_url
    ) {}

    public record DiscordEmbedField(
            String name,
            String value,
            boolean inline
    ) {}
}

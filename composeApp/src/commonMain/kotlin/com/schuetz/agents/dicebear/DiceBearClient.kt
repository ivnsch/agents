package com.schuetz.agents.dicebear

import com.schuetz.agents.AvatarUrlGenerator

// In our perfect AI world we'd use an AI avatar generator,
// but we'd have to ask the user for api keys for images too
// and this overcomplicates things right now
// also, DiceBear images are cute!
class DiceBearClientImpl : AvatarUrlGenerator {
    private val styles = listOf(
        "adventurer",
        "adventurer-neutral",
        "avataaars",
        "avataaars-neutral",
        "big-ears",
        "big-ears-neutral",
        "big-smile",
        "bottts",
        "bottts-neutral",
        "croodles",
        "croodles-neutral",
        "dylan",
        "fun-emoji",
        "icons",
        "jdenticon",
        "lorelei",
        "lorelei-neutral",
        "micah",
        "miniavs",
        "notionists",
        "notionists-neutral",
        "open-peeps",
        "personas",
        "pixel-art",
        "pixel-art-neutral",
        "rings",
        "shapes",
        "thumbs"
    )

    override fun generateRandomAvatarUrl(): String =
        // svg is supported too but didn't render on Android at least, would have to investigate
        "https://api.dicebear.com/9.x/${styles.random()}/png"
}

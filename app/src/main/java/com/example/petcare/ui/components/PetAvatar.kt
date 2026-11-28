package com.example.petcare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petcare.ui.theme.Ink


private val speciesEmoji = listOf(

    listOf("dog", "puppy", "chien", "كلب") to "🐶",

    listOf("cat", "kitten", "chat", "قط") to "🐱",

    listOf("parrot", "perroquet", "ببغاء") to "🦜",

    listOf("bird", "oiseau", "عصفور", "طير") to "🐦",

    listOf("rabbit", "bunny", "lapin", "أرنب") to "🐰",

    listOf("hamster", "هامستر") to "🐹",

    listOf("fish", "poisson", "سمك") to "🐠",

    listOf("turtle", "tortoise", "tortue", "سلحفاة") to "🐢",

    listOf("snake", "serpent", "ثعبان", "أفعى") to "🐍",

    listOf("horse", "cheval", "حصان") to "🐴",

    listOf("mouse", "souris", "فأر") to "🐭"
)


fun emojiFor(species: String): String? {

    val s = species.trim().lowercase()

    return speciesEmoji.firstOrNull { (words, _) ->

        words.any { s.contains(it) }

    }?.second
}


private fun speciesColor(emoji: String): Color {

    return when (emoji) {

        "🐶" -> Color(0xFFFFE0A3)

        "🐱" -> Color(0xFFE9D5FF)

        "🐦", "🦜" -> Color(0xFFBAE6FD)

        "🐰" -> Color(0xFFFBCFE8)

        "🐠" -> Color(0xFF93C5FD)

        "🐢", "🐍" -> Color(0xFFBBF7D0)

        else -> Color(0xFFCBD5E1)
    }
}


private val letterColors = listOf(

    Color(0xFFFFE0A3),
    Color(0xFFE9D5FF),
    Color(0xFFBAE6FD),
    Color(0xFFFBCFE8),
    Color(0xFFBBF7D0),
    Color(0xFFCCFBF1)
)


private fun letterColor(petId: String): Color {

    val index = Math.floorMod(
        petId.hashCode(),
        letterColors.size
    )

    return letterColors[index]
}


@Composable
fun PetAvatar(
    name: String,
    species: String,
    petId: String,
    size: Dp = 48.dp
) {

    val emoji = emojiFor(species)

    val background = if (emoji != null)
        speciesColor(emoji)
    else
        letterColor(petId)


    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .clearAndSetSemantics {
                contentDescription = "$name, $species"
            },

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = emoji ?: name.take(1).uppercase(),
            fontSize = (size.value * 0.5f).sp,
            color = Ink
        )
    }
}
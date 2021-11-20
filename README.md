# Jupiter, a Kotlin library for Bukkit and Spigot Plugins.
Starting off, this is a fork of https://github.com/DevSrSouza/KotlinBukkitAPI. 
I am not taking any of the credit for making this library. 

Jupiter adds some new features to KotlinBukkitAPI as well as code corrections and Java 16+ compatibility.

If help is needed using the library, you can use DevSrSouza's wiki https://github.com/DevSrSouza/KotlinBukkitAPI/wiki,
although KotlinPlugin has been renamed to Moon, whilst KotlinBukkitAPI was renamed to Jupiter.

## Samples

Event DSL sample
```kotlin
plugin.events {
  event<PlayerJoinEvent> {
    player.msg("&3Welcome ${player.name}".translateColor()) 
  }
  
  event<PlayerQuitEvent> {
    broadcast("&eThe player &c${player.name} &eleft :(".translateColor())
  }
}
```

Simple Command DSL example
```kotlin
plugin.simpleCommand("twitter") {
  sender.msg("&eFollow me on Twitter :D &ahttps://twitter.com/DevSrSouza".translateColor())
}
```

Item meta DSL and other stuff
```kotlin
val gem = item(Material.DIAMOND).apply {
  amount = 5
  meta<ItemMeta> {
    displayName = "&bGem".translateColor()
  }
}
val encbook = item(Material.ENCHANTED_BOOK).meta<EnchantmentStorageMeta> {
  displayName = "&4&lThe powerful BOOK".translateColor()
  addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
}
```

Another approach:
```kotlin
val gem = item(Material.DIAMOND, amount = 5).displayName("&bGem".translateColor())

val encbook = metadataItem<EnchantmentStorageMeta>(Material.ENCHANTED_BOOK) {
  displayName = "&4&lThe powerful BOOK".translateColor()
    addStoredEnchant(Enchantment.DAMAGE_ALL, 10, true) // putting sharpness 10 to the book
}
```

Menu creator DSL
```kotlin
val myMenu = menu(+"&cWarps", 3, true) {

  val arenaPvP = item(Material.DIAMOND_SWORD) {
      addEnchant(Enchantment.DAMAGE_ALL, 5, true)
      displayName = "&4Arena PvP".translateColor()
  }

  slot(2, 4, arenaPvP) { // Line, Slot
    onClick {
      player.teleport(Location(player.world, 250, 70, -355))
      close() // close the menu
    }
  }

  slot(2, 6, item(Material.GOLD).displayName("&6Shop".translateColor())) {
    onClick {
      player.teleport(Location(player.world, 2399, 70, -1234))
      close() // close the menu
    }
  }

  // when the menu renders to a player, will show the Paper item with their name.
  slot(3, 9, item(Material.PAPER).displayName("Hello {player}")) {
    onRender {
      showingItem?.meta<ItemMeta> {
         displayName = displayName.replace("{player}", player.name)
      } 
    }
  }
}

// open to player
myMenu.openToPlayer(player)
```

## Support
If you want support for this fork, you can come ask Hafixion on the Neptune Services Discord, as this library is used primarily for the Jupiter Series of plugins.
https://discord.gg/HvEKfjtzhS   

## Building
Looking to build this library? Simply run `gradle shadowJar`. You'll find a built jar file in build/libs.

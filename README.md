<div align="center">

# ⚡ Fast Client

**A high-performance, modular Minecraft client built for the modern era.**

[![Version](https://img.shields.io/badge/Version-8.0.0-blueviolet.svg?style=for-the-badge)](https://github.com/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-green.svg?style=for-the-badge)](https://minecraft.net/)
[![Platform](https://img.shields.io/badge/Platform-Fabric-lightgrey.svg?style=for-the-badge)](https://fabricmc.net/)

</div>

---

## 🔥 Key Features

- 🏎️ **Optimized Performance**: Built on top of Fabric with Sodium, Iris, and Lithium integration for maximum FPS.
- 🎨 **Modern UI**: Revolutionary user interface using Skia (HumbleUI) for a smooth, app-like experience.
- 🧩 **Modular Design**: Toggleable features categorized into HUD, Render, Player, and Misc.
- 🎵 **Integrated Music**: Native support for music playback with waveform visualizations.
- 🌐 **Rich Integration**: Discord Rich Presence, Web Browser integration, and more.

---

## 🛠️ Module Directory

Fast Client comes packed with a wide variety of modules to enhance your gameplay.

### 📊 HUD Modules
*Visual overlays to keep you informed.*

| Module | Description |
| :--- | :--- |
| **FPS Display** | Monitor your frames per second. |
| **Coordinates** | Real-time X, Y, Z position tracking. |
| **Combo Counter** | Track your consecutive hits. |
| **Reach Display** | Measure your attack distance. |
| **Keystrokes** | Show your WASD and mouse inputs. |
| **Ping Display** | Connection latency monitor. |
| **Speedometer** | Track your movement speed. |
| **Health Display** | Precise HP visualization. |
| **Memory Usage** | Monitor JVM memory allocation. |
| **Boss Bar** | Customizable boss health tracking. |

> [!TIP]
> Use the **HUD Settings** module to rearrange and customize every element on your screen.

### 🎭 Render Modules
*Enhance the visual experience of the game.*

- ✨ **Particles**: Fully customizable particle effects.
- 🩸 **Blood Particles**: Adds immersive hit effects.
- 🚀 **Projectile Trail**: Tracks the path of arrows, pearls, etc.
- 🌊 **Music Waveform**: Real-time visual for your music.
- 🦾 **Custom Hand**: Adjust hand position, scale, and swing.
- 💡 **Fullbright**: Perfect visibility in any environment.
- 🎞️ **Old Animations**: Bringing back the classic 1.7 feel.

### 🎮 Player Modules
*Gameplay tweaks and quality-of-life improvements.*

- 🎯 **Snap Tap**: Input priority for perfect movement.
- 🔍 **Zoom**: High-precision viewpoint magnification.
- 🔄 **Freelook**: 360-degree camera rotation independent of movement.
- ⏱️ **Time Changer**: Client-side world time adjustment.
- 🌤️ **Weather Changer**: Control local weather conditions.
- ⚔️ **Hit Delay Fix**: Removes the 1.8+ hit delay for smoother PvP.

### 🌟 Miscellaneous
- 👾 **Discord RPC**: Show off your status on Discord.
- 🌍 **Web Browser**: Browse the web without leaving the game.
- 👑 **Hypixel Utils**: Specialized tools for the Hypixel network.
- 📣 **Auto GG**: Automatically send "GG" after games.

---

## 💻 Technical Stack

Fast Client utilizes state-of-the-art libraries:

- **Loom/Fabric**: Modern modding toolchain.
- **Skia (HumbleUI)**: Advanced hardware-accelerated 2D graphics.
- **ViaFabricPlus**: Multi-version connectivity.
- **Sodium/Iris/Lithium**: The industry standard for performance optimization.
- **ProGuard**: Code optimization and minification.

---

## 📖 Documentation

- [Mixin Details](file:///f:/Fast-Client/MIXINS.md): A comprehensive look at how Fast Client modifies Minecraft's internals.

---

## 🚀 Building from Source

Ensure you have **JDK 21** installed.

1. Clone the repository.
2. Open a terminal in the project directory.
3. Run the following command:

```bash
./gradlew build
```

The compiled JAR will be located in `build/libs/`.

---

<div align="center">
Made with ❤️ for the Minecraft Community
</div>

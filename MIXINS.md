# 🛠️ Fast Client Mixins: Exhaustive Guide

This document provides a comprehensive technical breakdown of every Mixin in the project, explaining their specific logic and how they enable Fast Client's features.

---

## 🛠️ Mixin API & Annotations

Fast Client utilizes the **SpongePowered Mixin** framework along with **MixinExtras** for cleaner injections.

### Core Annotations
| Annotation | Purpose |
| :--- | :--- |
| `@Mixin` | Defines the class as a Mixin and specifies the target Minecraft class. |
| `@Shadow` | References a private/protected field or method from the target class. |
| `@Inject` | Injects custom code at a specific point (`At`) in a method. |
| `@Overwrite` | Completely replaces a method. Used for performance-critical path changes. |
| `@Unique` | Ensures a field/method does not collide with the target class. |
| `@Final` | Used on shadowed fields that are final in the target class. |
| `@Accessor` | Replaces the need for Shadow for simple field getters/setters. |

### MixinExtras
- `@ModifyExpressionValue`: Modifies the result of a sub-expression (like a method return or field load). Useful for boolean logic overrides.

---

## 💻 Client Core Mixins

### `MixinMinecraftClient`
*Targets: `net.minecraft.client.MinecraftClient`*
- **`onInit`**: Captures the `assetDir` from `RunArgs` for later resource loading.
- **`onStop`**: Ensures configuration files are saved and the `JCefBrowser` (integrated web browser) is safely closed.
- **`handleBlockBreaking`**: Implements **1.7 Animations**; adds block breaking particles and "fake swings" the hand when both attack and use keys are held.
- **`onHitDelayFix`**: Resets `attackCooldown` to 0 if the **Hit Delay Fix** module is enabled.
- **`updateWindowTitle`**: Overwrites the window title to show "Fast Client [Version] for [MC Version]".
- **`init`**: The main entry point for the client; initializes the Skia rendering surface and the `Fast` instance.
- **`tick`/`run`**: Posts `ClientTickEvent` and `GameLoopEvent` to the client's internal event bus.
- **`onResolutionChanged`**: Triggers a resize for the Kawase blur shaders to match the new window dimensions.

### `MixinKeyboard`
*Targets: `net.minecraft.client.Keyboard`*
- **`onKey`**: Hooked after `onKeyPressed` and `setKeyPressed`. It iterates through all client keybinds to update their internal pressed/down state.

### `MixinMouse`
*Targets: `net.minecraft.client.Mouse`*
- **`onMouseButton`**: Similar to the keyboard mixin, updates keybind states for mouse buttons.
- **`onMouseScroll`**: Posts a `MouseScrollEvent`, allowing modules (like Mod Menu or HUD) to intercept and cancel standard hotbar scrolling.

### `MixinWindow`
*Targets: `net.minecraft.client.util.Window`*
- **`onFramebufferSizeChanged`**: Recreates the Skia surface whenever the window is resized to ensure pixel-perfect rendering.

### `MixinKeyBinding`
*Targets: `net.minecraft.client.option.KeyBinding`*
- **`isPressed`/`setPressed`**: Implements **Snap Tap** logic. It overrides the input priority for WASD keys based on press timing to ensure perfect movement transitions.
- **`getRealIsPressed`**: Implements a "Duck" interface to return the true hardware state of a key regardless of client-side overrides.

---

## 🎨 Rendering & Visuals Mixins

### `MixinGameRenderer`
*Targets: `net.minecraft.client.render.GameRenderer`*
- **`render`**: 
    - Applies the **Kawase In-Game Blur** before rendering the main HUD.
    - Performs the **Skia Context Draw**, scaling to the window's scale factor and posting the `RenderSkiaEvent`.
- **`renderGuiBlur`**: Applies the **HUD Blur** immediately after the Minecraft HUD has finished rendering.
- **`getFov`**: Overrides the field of view for the **Zoom** module.

### `MixinInGameHud`
*Targets: `net.minecraft.client.gui.hud.InGameHud`*
- **`drawHeart`**: Implements the "No Heart Flash" animation from older Minecraft versions.
- **`renderMainHud`**: Posts a `RenderGameOverlayEvent` allowing modules to draw using Minecraft's `DrawContext`.

### `MixinHeldItemRenderer`
*Targets: `net.minecraft.client.render.item.HeldItemRenderer`*
- **`renderFirstPersonItem`**:
    - Applies transformation offsets for **Old Bow** and **Old Rod** animations.
    - Implements the **Custom Hand** module, allowing users to modify the hand's X, Y, Z, and Scale.
- **`applyOldSwingOffset`**: Re-implements the 1.7-style swing offset for food, blocking, and bows.

### `MixinLightmapTextureManager`
*Targets: `net.minecraft.client.render.LightmapTextureManager`*
- **`injectFullBright`**: Uses `@ModifyExpressionValue` to override the gamma setting, providing the **Fullbright** effect.

### `MixinInGameOverlayRenderer`
*Targets: `net.minecraft.client.gui.hud.InGameOverlayRenderer`*
- **`renderUnderwaterOverlay`/`renderFireOverlay`**: Allows the **Overlay Editor** module to cancel these visual overlays for a "Clear View".

### `MixinEntityRenderer`
*Targets: `net.minecraft.client.render.entity.EntityRenderer`*
- **`onRenderLevelHead`**: Implements the **LevelHead** feature for Hypixel, drawing the player's network level above their nameplate.

### `MixinCamera`
*Targets: `net.minecraft.client.render.Camera`*
- **`lockRotation`**: The core of the **Freelook** module. It decouples the camera rotation from the player entity, allowing 360-degree rotation while moving in a fixed direction.

### `BufferRendererAccessor`
*Targets: `net.minecraft.client.render.BufferRenderer`*
- Provides access to the `currentVertexBuffer` for low-level GL rendering.

---

## 🎮 Entity & World Mixins

### `MixinEntity`
*Targets: `net.minecraft.entity.Entity`*
- **`changeLookDirection`**: Posts `PlayerDirectionChangeEvent` and handles internal camera pitch/yaw storage for the Freelook system via the `IMixinCameraEntity` interface.

### `MixinLivingEntity`
*Targets: `net.minecraft.entity.LivingEntity`*
- **`tickMovement`**: Implements **No Jump Delay** by resetting the jump cooldown.
- **`jump`/`onDamaged`**: Updates timing data for the **Jump Reset Indicator** module.
- **`fakeSwingHand`**: Interface implementation to trigger the swing animation without an actual interaction packet.

### `MixinPlayerEntity`
*Targets: `net.minecraft.entity.player.PlayerEntity`*
- **`disableCooldown`**: Overrides `getAttackCooldownProgress` to always return 1.0, removing the visual attack indicator (1.8 style logic).
- **`injectGetMainArm`**: Implements **Force Main Hand**, forcing other players to appear as left or right-handed.

### `MixinWorld`
*Targets: `net.minecraft.world.World`*
- **`getRainGradient`/`isRaining`**: Overrides world weather state for the **Weather Changer** (Clear/Rain/Thunder toggle).

### `MixinBiome`
*Targets: `net.minecraft.world.biome.Biome`*
- **`getPrecipitation`**: Forces precipitation to be **SNOW** when the **Weather Changer**'s snow mode is enabled.

### `MixinClientWorldProperties`
*Targets: `net.minecraft.client.world.ClientWorld.Properties`*
- **`getTimeOfDay`**: Overrides the world time client-side for the **Time Changer** module.

---

## 🖥️ GUI & Interface Mixins

### `MixinBossBarHud`
*Targets: `net.minecraft.client.gui.hud.BossBarHud`*
- **`render`**: Cancels vanilla rendering to implement the custom **Boss Bar** HUD module with custom position and scaling logic.

### `MixinChatScreen`
*Targets: `net.minecraft.client.gui.screen.ChatScreen`*
- **`mouseClicked`/`mouseMoved`/`keyPressed`**: Forwards all input events to the **Integrated Web Browser** when it is active and overlaying the chat.

### `MixinMultiplayerScreen`
*Targets: `net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen`*
- **`connect`**: Posts a `ServerJoinEvent` when the player clicks "Join Server", allowing for server-specific module logic.

### `MixinPackScreen`
*Targets: `net.minecraft.client.gui.screen.pack.PackScreen`*
- **`init`**: Adds a custom "Convert" button to the resource pack screen for the **Fast Client Resource Converter**.

---

## 🌐 Network & Compatibility Mixins

### `MixinClientConnection`
*Targets: `net.minecraft.network.ClientConnection`*
- **`send`/`handlePacket`**: Hooks into all incoming and outgoing packets. Dispatches `SendPacketEvent` and `ReceivePacketEvent`, and manually handles `BundleS2CPacket` to ensure event consistency.

### `PlayerInteractEntityC2SPacketAccessor`
*Targets: `net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket`*
- Provides access to interaction `type` and `entityId` for reach and combo tracking.

### `viafabricplus.MixinMinecraftClient`
*Targets: `net.minecraft.client.MinecraftClient`*
- Fixes specific animation issues when using **ViaFabricPlus** to connect to older servers, specifically relating to item usage while breaking blocks.

---

## 🏗️ Technical Patterns

### Duck Typing (Interface Injection)
Located in `com.fastclient.mixin.interfaces`. used to add methods to vanilla classes.
- `IMixinMinecraftClient`: Exposes asset directory.
- `IMixinCameraEntity`: Stores independent Freelook pitch/yaw.
- `IMixinLivingEntity`: Allows triggering "Fake" swings.
- `IMixinKeyBinding`: Exposes the actual hardware state of a key.

### Priority Levels
- **300**: Core Client (Early initialization).
- **1000**: Standard (Most mod features).
- **2000**: Compatibility (ViaFabricPlus adjustments).

---

> [!TIP]
> This documentation is automatically generated and verified against the Fast Client source code.

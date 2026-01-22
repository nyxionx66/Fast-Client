# FastClient Mod - Product Requirements Document

## Original Problem Statement
Make all the icons in ClickGUI to white and texts to white. Also add a module to show armor stats with option to select vertical or horizontal orientation.

## Architecture & Tasks Done

### Date: January 2026

**Completed Tasks:**
1. **White Icons & Text in ClickGUI**
   - Modified `NavigationRail.java` - Changed navigation icons and labels to white
   - Modified `ModsPage.java` - Changed mod card text to white  
   - Modified `SettingBar.java` - Changed setting titles and descriptions to white

2. **Armor Stats Module**
   - Created new `ArmorStatsMod.java` HUD module
   - Added orientation setting (Vertical/Horizontal)
   - Displays total armor protection and toughness
   - Registered in `ModManager.java`
   - Added localization in `en.lang`

## Files Modified
- `/app/src/main/java/com/fastclient/gui/modmenu/component/NavigationRail.java`
- `/app/src/main/java/com/fastclient/gui/modmenu/pages/ModsPage.java`
- `/app/src/main/java/com/fastclient/gui/modmenu/component/SettingBar.java`
- `/app/src/main/java/com/fastclient/management/mod/ModManager.java`
- `/app/src/main/resources/assets/fast/languages/en.lang`

## Files Created
- `/app/src/main/java/com/fastclient/management/mod/impl/hud/ArmorStatsMod.java`

## Tech Stack
- Java (Minecraft Mod)
- Gradle build system
- Skia for UI rendering
- Minecraft Fabric/Forge modding framework

## What's Implemented
- [x] All ClickGUI icons set to white
- [x] All ClickGUI text set to white
- [x] ArmorStatsMod with vertical/horizontal orientation option
- [x] Displays armor protection and toughness values

## Backlog / Future Enhancements
- P1: Add individual armor piece durability display
- P2: Add enchantment info to armor stats
- P2: Custom color settings for the armor stats module

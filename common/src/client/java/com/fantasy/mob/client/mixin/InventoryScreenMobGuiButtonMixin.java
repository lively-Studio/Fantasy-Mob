/*
 * Copyright (c) 2026 lively-Studio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fantasy.mob.client.mixin;

import com.fantasy.mob.client.MobGuiButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在生存模式物品栏(E键)界面添加「已驯服生物」按钮（仅当 {@link MobGuiButton#isVisible()}）。
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMobGuiButtonMixin {

    @Inject(method = "init", at = @At("RETURN"))
    private void fantasy_mob_addButton(CallbackInfo ci) {
        if (!MobGuiButton.isVisible()) return;
        InventoryScreen screen = (InventoryScreen) (Object) this;
        MobGuiHandledScreenAccessor accessor = (MobGuiHandledScreenAccessor) screen;
        ScreenAccessor screenAccessor = (ScreenAccessor) screen;
        int x = accessor.getX() + accessor.getBackgroundWidth() - 80;
        int y = accessor.getY() + 5;
        Runnable onClick = MobGuiButton.getOnClick();
        screenAccessor.invokeAddDrawableChild(ButtonWidget.builder(
                MobGuiButton.getText(),
                btn -> MinecraftClient.getInstance().execute(onClick)
        ).dimensions(x, y, 78, 20).build());
    }
}
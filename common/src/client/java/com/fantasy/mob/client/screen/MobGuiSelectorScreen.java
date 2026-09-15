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

package com.fantasy.mob.client.screen;

import com.fantasy.mob.network.MobNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

/**
 * 多只已驯服生物时的选择界面：每只一个按钮，点击打开其背包。
 * 贴近原版风格，通用，不依赖任何特定生物类型。
 */
public class MobGuiSelectorScreen extends Screen {

    private final int[] ids;
    private final String[] names;

    public MobGuiSelectorScreen(int[] ids, String[] names) {
        super(Text.translatable("fantasy_mob.selector.title"));
        this.ids = ids;
        this.names = names;
    }

    @Override
    protected void init() {
        int buttonW = 220;

        GridWidget grid = new GridWidget();
        grid.getMainPositioner().alignHorizontalCenter().margin(6, 2);
        GridWidget.Adder adder = grid.createAdder(1);

        for (int i = 0; i < ids.length; i++) {
            final int entityId = ids[i];
            String label = (i < names.length && names[i] != null && !names[i].isBlank())
                    ? names[i] : Text.translatable("fantasy_mob.selector.open").getString() + " #" + entityId;
            adder.add(ButtonWidget.builder(
                    Text.literal(label),
                    btn -> {
                        ClientPlayNetworking.send(new MobNetworking.OpenMobBackpackPayload(entityId));
                        if (this.client != null) this.client.setScreen(null);
                    }
            ).width(buttonW).build());
        }

        adder.add(ButtonWidget.builder(
                ScreenTexts.CANCEL,
                btn -> this.close()
        ).width(buttonW).build());

        grid.refreshPositions();
        // 垂直置中：以按钮堆为中心
        grid.setPosition((this.width - buttonW) / 2,
                this.height / 2 - (ids.length * 24 + 16) / 2);
        grid.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title,
                this.width / 2, 24, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
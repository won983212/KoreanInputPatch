package com.won983212.kpatch.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfigScreen extends Screen {
    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options row list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options row list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;

    /** List of options rows shown on the screen */
    private OptionsRowList optionsRowList;
    private final Screen parentScreen;


    public ConfigScreen(Screen parentScreen) {
        super(new TranslationTextComponent("koreanime.configGui.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.optionsRowList = new OptionsRowList(
                minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.optionsRowList.addBig(new SliderPercentageOption(
                "koreanime.configGui.visibleMode.title",
                0, 2,
                1,
                unused -> Config.getDouble(Config.IME_INDICATOR_VISIBLE_MODE),
                (unused, newValue) -> Config.set(Config.IME_INDICATOR_VISIBLE_MODE, newValue.intValue()),
                (gs, option) -> new StringTextComponent(
                        I18n.get("koreanime.configGui.visibleMode.title") + ": " + I18n.get("koreanime.configGui.visibleMode" + (int) option.get(gs) + ".desc"))
        ));

        this.optionsRowList.addBig(new BooleanOption(
                "koreanime.configGui.useAnimation.title",
                unused -> Config.getBoolean(Config.IME_INDICATOR_ANIMATE),
                (unused, newValue) -> Config.set(Config.IME_INDICATOR_ANIMATE, newValue)
        ));

        this.children.add(this.optionsRowList);

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("gui.done"),
                button -> this.onClose()
        ));
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parentScreen);
        Config.save();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, font, title.getString(), width / 2,
                TITLE_HEIGHT, 0xffffff);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}

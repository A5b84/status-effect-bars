package io.github.a5b84.statuseffectbars.config;

import net.minecraft.client.resource.language.I18n;

@SuppressWarnings("unused")
public enum Direction {
    LEFT_TO_RIGHT(false, false),
    RIGHT_TO_LEFT(false, true),
    BOTTOM_TO_TOP(true, true),
    TOP_TO_BOTTOM(true, false);

    public final boolean swapXY;
    public final boolean reverseAxis;

    Direction(boolean swapXY, boolean reverseAxis) {
        this.swapXY = swapXY;
        this.reverseAxis = reverseAxis;
    }

    @Override
    public String toString() {
        return I18n.translate("text.autoconfig.status-effect-bars.option.direction." + name());
    }

}

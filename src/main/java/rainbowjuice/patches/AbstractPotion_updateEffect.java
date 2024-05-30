package rainbowjuice.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import org.apache.commons.lang3.ArrayUtils;
import rainbowjuice.RainbowJuice;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractPotion.class,
        method = "updateEffect"
)
public class AbstractPotion_updateEffect {
    public static void Postfix(AbstractPotion __instance) {
        if (Objects.equals(__instance.ID, "Fruit Juice")) {
            if (RainbowJuice.rainbowLiquid) {
                __instance.liquidColor.r = (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.liquidColor.g = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.liquidColor.b = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.liquidColor.a = 1.0F;
            }
            if (RainbowJuice.rainbowHybrid) {
                __instance.hybridColor.r = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.hybridColor.g = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.hybridColor.b = (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.hybridColor.a = 1.0F;
            }
            if (RainbowJuice.rainbowSpots && !ArrayUtils.contains(RainbowJuice.spotlessBottles, RainbowJuice.bottle)) {
                __instance.spotsColor.r = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.spotsColor.g = (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.spotsColor.b = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
                __instance.spotsColor.a = 1.0F;
            }
        }
    }
}

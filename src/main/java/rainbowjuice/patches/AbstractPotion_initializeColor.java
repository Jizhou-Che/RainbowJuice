package rainbowjuice.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import org.apache.commons.lang3.ArrayUtils;
import rainbowjuice.RainbowJuice;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractPotion.class,
        method = "initializeColor"
)
public class AbstractPotion_initializeColor {
    public static void Postfix(AbstractPotion __instance) {
        if (Objects.equals(__instance.ID, "Fruit Juice")) {
            __instance.liquidColor = RainbowJuice.colorList.get(RainbowJuice.liquidColor).cpy();
            __instance.hybridColor = RainbowJuice.colorList.get(RainbowJuice.hybridColor).cpy();
            if (!ArrayUtils.contains(RainbowJuice.spotlessBottles, RainbowJuice.bottle)) {
                __instance.spotsColor = RainbowJuice.colorList.get(RainbowJuice.spotsColor).cpy();
            }
        }
    }
}

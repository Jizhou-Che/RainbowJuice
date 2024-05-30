package rainbowjuice.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rainbowjuice.RainbowJuice;

import java.util.Objects;

@SpirePatch2(
        clz = AbstractPotion.class,
        method = "initializeImage"
)
public class AbstractPotion_initializeImage {
    public static void Prefix(AbstractPotion __instance) {
        if (Objects.equals(__instance.ID, "Fruit Juice")) {
            __instance.size = RainbowJuice.bottleList.get(RainbowJuice.bottle);
        }
    }
}

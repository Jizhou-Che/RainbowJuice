package rainbowjuice;

import basemod.*;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rainbowjuice.util.TextureLoader;

import java.io.IOException;
import java.util.ArrayList;

@SpireInitializer
public class RainbowJuice implements PostInitializeSubscriber, EditStringsSubscriber {
    public static final Logger logger = LogManager.getLogger(RainbowJuice.class.getName());
    public static final ArrayList<Color> colorList = new ArrayList<>();
    public static final ArrayList<AbstractPotion.PotionSize> bottleList = new ArrayList<>();
    public static final ArrayList<Texture> bottleTextureList = new ArrayList<>();
    public static final int[] spotlessBottles = {0, 3, 4, 5, 10, 14}; // ANVIL, CARD, EYE, FAIRY, MOON, SPIKY.
    public static final int defaultBottle = 7; // HEART.
    private static final int defaultLiquidColor = 21; // ORANGE.
    private static final int defaultHybridColor = 15; // LIME.
    private static final int defaultSpotsColor = 0; // CLEAR.
    public static int liquidColor = defaultLiquidColor;
    public static int hybridColor = defaultHybridColor;
    public static int spotsColor = defaultSpotsColor;
    public static boolean rainbowLiquid = false;
    public static boolean rainbowHybrid = false;
    public static boolean rainbowSpots = false;
    public static int bottle = defaultBottle;

    public RainbowJuice() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new RainbowJuice();
    }

    @Override
    public void receivePostInitialize() {
        initializeColorList();
        initializeBottleList();

        loadData();

        initializeConfigPanel();
    }

    @Override
    public void receiveEditStrings() {
        loadLocalizationFiles(Settings.language);
    }

    private void saveData() {
        logger.info("RainbowJuice | Saving config data...");
        try {
            SpireConfig config = new SpireConfig("RainbowJuice", "RainbowJuiceConfig");
            config.setInt("liquidColor", liquidColor);
            config.setInt("hybridColor", hybridColor);
            config.setInt("spotsColor", spotsColor);
            config.setInt("bottle", bottle);
            config.save();
        } catch (IOException e) {
            logger.error("RainbowJuice | Failed to save config data!");
        }
    }

    private void loadData() {
        logger.info("RainbowJuice | Loading config data...");
        try {
            SpireConfig config = new SpireConfig("RainbowJuice", "RainbowJuiceConfig");
            config.load();
            if (config.has("liquidColor") && config.has("hybridColor") && config.has("spotsColor") && config.has("bottle")) {
                liquidColor = config.getInt("liquidColor");
                hybridColor = config.getInt("hybridColor");
                spotsColor = config.getInt("spotsColor");
                bottle = config.getInt("bottle");
            } else {
                clearData();
            }
        } catch (IOException e) {
            logger.error("RainbowJuice | Failed to load config data!");
            clearData();
        }
    }

    private void clearData() {
        logger.info("RainbowJuice | Clearing config data...");
        liquidColor = defaultLiquidColor;
        hybridColor = defaultHybridColor;
        spotsColor = defaultSpotsColor;
        bottle = defaultBottle;
        saveData();
    }

    private void initializeConfigPanel() {
        logger.info("RainbowJuice | Initializing config panel...");

        ModPanel configPanel = new ModPanel();
        BaseMod.registerModBadge(TextureLoader.getTexture("rainbowjuice/images/ui/Badge.png"), "RainbowJuice", "瓶装易碎柯妮法", "RainbowJuice : 缤纷果汁\n\nYou can customize the look of your fruit juice!\n你可以自定义果汁的外观！", configPanel);

        UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("rainbowjuice:Config");

        ModLabel label1 = new ModLabel(uiStrings.TEXT[3], 750f, 625f, configPanel,
                (me) -> {
                    // Update function.
                });
        ModLabeledButton labeledButton1_1 = new ModLabeledButton(uiStrings.TEXT[1], 450f, 600f, configPanel,
                (me) -> {
                    // Press function.
                    liquidColor -= 1;
                    if (liquidColor == -1) {
                        liquidColor = colorList.size() - 1;
                    }
                    rainbowLiquid = liquidColor == colorList.size() - 1;
                    if (rainbowLiquid) {
                        label1.text = uiStrings.TEXT[4];
                    } else {
                        label1.text = uiStrings.TEXT[3];
                    }
                    label1.color = colorList.get(liquidColor).cpy();
                    saveData();
                });
        ModLabeledButton labeledButton1_2 = new ModLabeledButton(uiStrings.TEXT[2], 600f, 600f, configPanel,
                (me) -> {
                    // Press function.
                    liquidColor += 1;
                    if (liquidColor == colorList.size()) {
                        liquidColor = 0;
                    }
                    rainbowLiquid = liquidColor == colorList.size() - 1;
                    if (rainbowLiquid) {
                        label1.text = uiStrings.TEXT[4];
                    } else {
                        label1.text = uiStrings.TEXT[3];
                    }
                    label1.color = colorList.get(liquidColor).cpy();
                    saveData();
                });
        ModLabel label2 = new ModLabel(uiStrings.TEXT[5], 750f, 525f, configPanel,
                (me) -> {
                    // Update function.
                });
        ModLabeledButton labeledButton2_1 = new ModLabeledButton(uiStrings.TEXT[1], 450f, 500f, configPanel,
                (me) -> {
                    // Press function.
                    hybridColor -= 1;
                    if (hybridColor == -1) {
                        hybridColor = colorList.size() - 1;
                    }
                    rainbowHybrid = hybridColor == colorList.size() - 1;
                    if (rainbowHybrid) {
                        label2.text = uiStrings.TEXT[6];
                    } else {
                        label2.text = uiStrings.TEXT[5];
                    }
                    label2.color = colorList.get(hybridColor).cpy();
                    saveData();
                });
        ModLabeledButton labeledButton2_2 = new ModLabeledButton(uiStrings.TEXT[2], 600f, 500f, configPanel,
                (me) -> {
                    // Press function.
                    hybridColor += 1;
                    if (hybridColor == colorList.size()) {
                        hybridColor = 0;
                    }
                    rainbowHybrid = hybridColor == colorList.size() - 1;
                    if (rainbowHybrid) {
                        label2.text = uiStrings.TEXT[6];
                    } else {
                        label2.text = uiStrings.TEXT[5];
                    }
                    label2.color = colorList.get(hybridColor).cpy();
                    saveData();
                });
        ModLabel label3 = new ModLabel(uiStrings.TEXT[7], 750f, 425f, configPanel,
                (me) -> {
                    // Update function.
                });
        ModLabeledButton labeledButton3_1 = new ModLabeledButton(uiStrings.TEXT[1], 450f, 400f, configPanel,
                (me) -> {
                    // Press function.
                    spotsColor -= 1;
                    if (spotsColor == -1) {
                        spotsColor = colorList.size() - 1;
                    }
                    rainbowSpots = spotsColor == colorList.size() - 1;
                    if (rainbowSpots) {
                        label3.text = uiStrings.TEXT[8];
                    } else {
                        label3.text = uiStrings.TEXT[7];
                    }
                    label3.color = colorList.get(spotsColor).cpy();
                    saveData();
                });
        ModLabeledButton labeledButton3_2 = new ModLabeledButton(uiStrings.TEXT[2], 600f, 400f, configPanel,
                (me) -> {
                    // Press function.
                    spotsColor += 1;
                    if (spotsColor == colorList.size()) {
                        spotsColor = 0;
                    }
                    rainbowSpots = spotsColor == colorList.size() - 1;
                    if (rainbowSpots) {
                        label3.text = uiStrings.TEXT[8];
                    } else {
                        label3.text = uiStrings.TEXT[7];
                    }
                    label3.color = colorList.get(spotsColor).cpy();
                    saveData();
                });
        ModLabel label4_1 = new ModLabel(uiStrings.TEXT[9], 750f, 325f, configPanel,
                (me) -> {
                    // Update function.
                });
        ModLabel label4_2 = new ModLabel(uiStrings.TEXT[10], 1000f, 325f, configPanel,
                (me) -> {
                    // Update function.
                });
        ModColorDisplay colorDisplay0 = new ModColorDisplay(910f, 310f, bottleTextureList.get(bottle), null,
                (me) -> {
                    // Click function.
                });
        ModLabeledButton labeledButton4_1 = new ModLabeledButton(uiStrings.TEXT[1], 450f, 300f, configPanel,
                (me) -> {
                    // Press function.
                    bottle -= 1;
                    if (bottle == -1) {
                        bottle = bottleList.size() - 1;
                    }
                    colorDisplay0.texture = bottleTextureList.get(bottle);
                    if (ArrayUtils.contains(spotlessBottles, bottle)) {
                        label4_2.color = colorList.get(2).cpy();
                    } else {
                        label4_2.color = colorList.get(0).cpy();
                    }
                    saveData();
                });
        ModLabeledButton labeledButton4_2 = new ModLabeledButton(uiStrings.TEXT[2], 600f, 300f, configPanel,
                (me) -> {
                    // Press function.
                    bottle += 1;
                    if (bottle == bottleList.size()) {
                        bottle = 0;
                    }
                    colorDisplay0.texture = bottleTextureList.get(bottle);
                    if (ArrayUtils.contains(spotlessBottles, bottle)) {
                        label4_2.color = colorList.get(2).cpy();
                    } else {
                        label4_2.color = colorList.get(0).cpy();
                    }
                    saveData();
                });
        ModLabeledButton labeledButton0 = new ModLabeledButton(uiStrings.TEXT[0], 400f, 700f, configPanel,
                (me) -> {
                    // Click function.
                    clearData();
                    rainbowLiquid = false;
                    rainbowHybrid = false;
                    rainbowSpots = false;
                    label1.text = uiStrings.TEXT[3];
                    label1.color = colorList.get(liquidColor).cpy();
                    label2.text = uiStrings.TEXT[5];
                    label2.color = colorList.get(hybridColor).cpy();
                    label3.text = uiStrings.TEXT[7];
                    label3.color = colorList.get(spotsColor).cpy();
                    label4_2.color = colorList.get(0).cpy(); // CLEAR.
                    colorDisplay0.texture = bottleTextureList.get(bottle);
                });

        rainbowLiquid = liquidColor == colorList.size() - 1;
        rainbowHybrid = hybridColor == colorList.size() - 1;
        rainbowSpots = spotsColor == colorList.size() - 1;
        if (rainbowLiquid) {
            label1.text = uiStrings.TEXT[4];
        }
        if (rainbowHybrid) {
            label2.text = uiStrings.TEXT[6];
        }
        if (rainbowSpots) {
            label3.text = uiStrings.TEXT[8];
        }
        label1.color = colorList.get(liquidColor).cpy();
        label2.color = colorList.get(hybridColor).cpy();
        label3.color = colorList.get(spotsColor).cpy();
        if (ArrayUtils.contains(spotlessBottles, bottle)) {
            label4_2.color = colorList.get(2).cpy(); // WHITE.
        } else {
            label4_2.color = colorList.get(0).cpy(); // CLEAR.
        }

        configPanel.addUIElement(labeledButton0);
        configPanel.addUIElement(label1);
        configPanel.addUIElement(labeledButton1_1);
        configPanel.addUIElement(labeledButton1_2);
        configPanel.addUIElement(label2);
        configPanel.addUIElement(labeledButton2_1);
        configPanel.addUIElement(labeledButton2_2);
        configPanel.addUIElement(label3);
        configPanel.addUIElement(labeledButton3_1);
        configPanel.addUIElement(labeledButton3_2);
        configPanel.addUIElement(label4_1);
        configPanel.addUIElement(label4_2);
        configPanel.addUIElement(labeledButton4_1);
        configPanel.addUIElement(labeledButton4_2);
        configPanel.addUIElement(colorDisplay0);
    }

    private void loadLocalizationFiles(Settings.GameLanguage language) {
        if (language == Settings.GameLanguage.ZHS) {
            BaseMod.loadCustomStringsFile(UIStrings.class, "rainbowjuice/localization/zhs/ui.json");
        } else {
            BaseMod.loadCustomStringsFile(UIStrings.class, "rainbowjuice/localization/eng/ui.json");
        }
    }

    private void initializeColorList() {
        colorList.add(Color.CLEAR.cpy());
        colorList.add(Color.BLACK.cpy());
        colorList.add(Color.WHITE.cpy());
        colorList.add(Color.LIGHT_GRAY.cpy());
        colorList.add(Color.GRAY.cpy());
        colorList.add(Color.DARK_GRAY.cpy());
        colorList.add(Color.BLUE.cpy());
        colorList.add(Color.NAVY.cpy());
        colorList.add(Color.ROYAL.cpy());
        colorList.add(Color.SLATE.cpy());
        colorList.add(Color.SKY.cpy());
        colorList.add(Color.CYAN.cpy());
        colorList.add(Color.TEAL.cpy());
        colorList.add(Color.GREEN.cpy());
        colorList.add(Color.CHARTREUSE.cpy());
        colorList.add(Color.LIME.cpy());
        colorList.add(Color.FOREST.cpy());
        colorList.add(Color.OLIVE.cpy());
        colorList.add(Color.YELLOW.cpy());
        colorList.add(Color.GOLD.cpy());
        colorList.add(Color.GOLDENROD.cpy());
        colorList.add(Color.ORANGE.cpy());
        colorList.add(Color.BROWN.cpy());
        colorList.add(Color.TAN.cpy());
        colorList.add(Color.FIREBRICK.cpy());
        colorList.add(Color.RED.cpy());
        colorList.add(Color.SCARLET.cpy());
        colorList.add(Color.CORAL.cpy());
        colorList.add(Color.SALMON.cpy());
        colorList.add(Color.PINK.cpy());
        colorList.add(Color.MAGENTA.cpy());
        colorList.add(Color.PURPLE.cpy());
        colorList.add(Color.VIOLET.cpy());
        colorList.add(Color.MAROON.cpy());
        colorList.add(Color.WHITE.cpy()); // RAINBOW.
    }

    private void initializeBottleList() {
        bottleList.add(AbstractPotion.PotionSize.ANVIL);
        bottleList.add(AbstractPotion.PotionSize.BOLT);
        bottleList.add(AbstractPotion.PotionSize.BOTTLE);
        bottleList.add(AbstractPotion.PotionSize.CARD);
        bottleList.add(AbstractPotion.PotionSize.EYE);
        bottleList.add(AbstractPotion.PotionSize.FAIRY);
        bottleList.add(AbstractPotion.PotionSize.H);
        bottleList.add(AbstractPotion.PotionSize.HEART);
        bottleList.add(AbstractPotion.PotionSize.JAR);
        bottleList.add(AbstractPotion.PotionSize.M);
        bottleList.add(AbstractPotion.PotionSize.MOON);
        bottleList.add(AbstractPotion.PotionSize.S);
        bottleList.add(AbstractPotion.PotionSize.SNECKO);
        bottleList.add(AbstractPotion.PotionSize.SPHERE);
        bottleList.add(AbstractPotion.PotionSize.SPIKY);
        bottleList.add(AbstractPotion.PotionSize.T);

        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Anvil.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Bolt.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Bottle.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Card.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Eye.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Fairy.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/H.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Heart.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Jar.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/M.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Moon.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/S.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Snecko.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Sphere.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/Spiky.png"));
        bottleTextureList.add(TextureLoader.getTexture("rainbowjuice/images/ui/potionbottle/T.png"));
    }
}

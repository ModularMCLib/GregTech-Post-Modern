package com.modularmc.gtpm.api.cover.filter;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.element.GTTextFieldElement;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.utils.TagExprFilter;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.modularmc.gtpm.api.gui.UITemplate.setBounds;

@TransVariants(keyPrefix = "cover.tag_filter",
               value = {
                       @Variant(id = "matches",
                                source = "Item matches",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品匹配")),
                       @Variant(id = "matches_not",
                                source = "Item does not match",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品不匹配")),
                       @Variant(id = "test_slot.info",
                                source = "Insert a item to test if it matches the filter expression",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "放入一件物品以测试是否匹配过滤表达式")),
                       @Variant(id = "title",
                                source = "Tag Filter",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "标签过滤")),
                       @Variant(id = "info.0",
                                source = "§bAccepts complex expressions",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b接受复杂表达式")),
                       @Variant(id = "info.1",
                                source = "§6a & b§r = AND",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6a & b§r = 且（AND）")),
                       @Variant(id = "info.10",
                                source = "§bExample: §6*dusts/gold | (c:circuits & !*lv)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§b使用范例：§6*dusts/gold | (c:circuits & !*lv)")),
                       @Variant(id = "info.11",
                                source = "This matches all gold dusts or all circuits except LV ones",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "这将匹配所有种类的金粉或LV以外的电路")),
                       @Variant(id = "info.2",
                                source = "§6a | b§r = OR",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6a | b§r = 或（OR）")),
                       @Variant(id = "info.3",
                                source = "§6a ^ b§r = XOR",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6a ^ b§r = 异或（XOR）")),
                       @Variant(id = "info.4",
                                source = "§6!a§r = NOT",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6!a§r = 非（NOT）")),
                       @Variant(id = "info.5",
                                source = "§6(a)§r for grouping",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6(a)§r 改变优先级")),
                       @Variant(id = "info.6",
                                source = "§6*§r for wildcard",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6*§r 为通配符")),
                       @Variant(id = "info.7",
                                source = "§6$§r for untagged",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6$§r 为没有标签的物品/流体等")),
                       @Variant(id = "info.8",
                                source = "§bTags come in the form 'namespace:tag/subtype'.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§b标签的格式为'命名空间:标签/子类型'。")),
                       @Variant(id = "info.9",
                                source = "The 'c:' namespace is assumed if one isn't provided.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "如果没有提供命名空间，则为'c:'。")),
               })
public abstract class TagFilter<T, S extends Filter<T, S>> implements Filter<T, S> {

    private static final int MAX_FILTER_LENGTH = 64;
    private static final Pattern DOUBLE_WILDCARD = Pattern.compile("\\*{2,}");
    private static final Pattern DOUBLE_AND = Pattern.compile("&{2,}");
    private static final Pattern DOUBLE_OR = Pattern.compile("\\|{2,}");
    private static final Pattern DOUBLE_NOT = Pattern.compile("!{2,}");
    private static final Pattern DOUBLE_XOR = Pattern.compile("\\^{2,}");
    private static final Pattern DOUBLE_SPACE = Pattern.compile(" {2,}");

    @Getter
    protected String tagFilterExpression = "";

    protected Consumer<S> itemWriter = filter -> {};
    protected Consumer<S> onUpdated = filter -> itemWriter.accept(filter);

    @Nullable
    protected TagExprFilter.TagExprParser.MatchExpr matchExpr = null;

    protected TagFilter() {}

    @Override
    public boolean isBlank() {
        return tagFilterExpression.isBlank();
    }

    public void setFilterExpr(String filterExpr) {
        this.tagFilterExpression = filterExpr;
        matchExpr = TagExprFilter.parseExpression(tagFilterExpression);
        // noinspection unchecked
        onUpdated.accept((S) this);
    }

    @Override
    public UIElement openConfigurator(int x, int y) {
        UIElement group = new UIElement();
        setBounds(group, x, y, 18 * 3 + 25, 18 * 3);
        group.addChild(createInfoIcon());
        group.addChild(createTextField());
        return group;
    }

    private UIElement createInfoIcon() {
        UIElement icon = new UIElement();
        icon.style(style -> style
                .backgroundTexture(GuiTextures.INFO_ICON)
                .tooltips(LangHandler.getMultiLang("cover.tag_filter.info").toArray(Component[]::new)));
        setBounds(icon, 0, 0, 20, 20);
        return icon;
    }

    private GTTextFieldElement createTextField() {
        GTTextFieldElement textField = new GTTextFieldElement(0, 29, 18 * 3 + 25, 12);
        textField.setAnyString();
        textField.setText(tagFilterExpression, false);
        textField.setTextResponder(input -> updateFilterExpr(textField, input));
        return textField;
    }

    private void updateFilterExpr(GTTextFieldElement textField, String input) {
        String normalized = normalizeInput(input);
        if (!normalized.equals(input)) {
            textField.setText(normalized, false);
        }
        setFilterExpr(normalized);
    }

    private static String normalizeInput(String input) {
        if (input.length() > MAX_FILTER_LENGTH) {
            input = input.substring(0, MAX_FILTER_LENGTH);
        }
        // remove all operators that are double
        input = DOUBLE_WILDCARD.matcher(input).replaceAll("*");
        input = DOUBLE_AND.matcher(input).replaceAll("&");
        input = DOUBLE_OR.matcher(input).replaceAll("|");
        input = DOUBLE_NOT.matcher(input).replaceAll("!");
        input = DOUBLE_XOR.matcher(input).replaceAll("^");
        input = DOUBLE_SPACE.matcher(input).replaceAll(" ");
        // move ( and ) so it doesn't create invalid expressions f.e. xxx (& yyy) => xxx & (yyy)
        // append or prepend ( and ) if the amount is not equal
        StringBuilder builder = new StringBuilder();
        int unclosed = 0;
        char last = ' ';
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ') {
                if (last != '(')
                    builder.append(" ");
                continue;
            }
            if (c == '(')
                unclosed++;
            else if (c == ')') {
                unclosed--;
                if (last == '&' || last == '|' || last == '^') {
                    int l = builder.lastIndexOf(" " + last);
                    int l2 = builder.lastIndexOf(String.valueOf(last));
                    int insertionIndex = l >= 0 && l == l2 - 1 ? l : l2;
                    builder.insert(insertionIndex, ")");
                    continue;
                }
                if (i > 0 && builder.charAt(builder.length() - 1) == ' ') {
                    builder.deleteCharAt(builder.length() - 1);
                }
            } else if ((c == '&' || c == '|' || c == '^') && last == '(') {
                builder.deleteCharAt(builder.lastIndexOf("("));
                builder.append(c).append(" (");
                continue;
            }

            builder.append(c);
            last = c;
        }
        if (unclosed > 0) {
            builder.append(")".repeat(unclosed));
        } else if (unclosed < 0) {
            unclosed = -unclosed;
            for (int i = 0; i < unclosed; i++) {
                builder.insert(0, "(");
            }
        }
        input = builder.toString();
        return input.replaceAll(" {2,}", " ");
    }

    @Override
    public void setOnUpdated(Consumer<S> onUpdated) {
        this.onUpdated = filter -> {
            this.itemWriter.accept(filter);
            onUpdated.accept(filter);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagFilter<?, ?> tagFilter = (TagFilter<?, ?>) o;
        return tagFilterExpression.equals(tagFilter.tagFilterExpression);
    }

    @Override
    public int hashCode() {
        return tagFilterExpression.hashCode();
    }
}

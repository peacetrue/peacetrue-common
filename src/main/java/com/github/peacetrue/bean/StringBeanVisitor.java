package com.github.peacetrue.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * 字符串 Bean 访问者。
 * 遍历 Bean 然后返回其字符串表示，
 * 等效于转换为 properties 文件。
 *
 * @author peace
 **/
@Getter
@SuperBuilder
public class StringBeanVisitor extends ConcreteBeanVisitor<String> {

    /** 默认实例。 */
    public static final StringBeanVisitor DEFAULT = StringBeanVisitor.builder().build();

    /** 属性间分隔符，属性与属性之间的分隔符，默认为换行符 */
    @Builder.Default
    private String propertySeparator = "\n";
    /** 属性内分隔符，属性内路径与对象的分隔符，默认为等号 */
    @Builder.Default
    private String pathObjectSeparator = "=";
    /** 叶子节点对象转换为字符串，默认使用 {@link String#valueOf(Object)} */
    @Builder.Default
    private Function<Object, String> toString = String::valueOf;

    @Override
    public String visitLeaf(@Nullable String path, @Nullable Object object) {
        if (path == null) return toString.apply(object);
        return getPropertyPathBuilder().build(path, object) + pathObjectSeparator + toString.apply(object);
    }

    @Nullable
    @Override
    protected String merge(@Nullable String one, @Nullable String two) {
        if (one == null) return two;
        return one + propertySeparator + two;
    }

}

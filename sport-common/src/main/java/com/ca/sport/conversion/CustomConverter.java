package com.ca.sport.conversion;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器
 * 去掉前后空格
 * <S, T>  : S  页面上类型   T ： 转换后的类型
 *
 * @author lx
 */
public class CustomConverter implements Converter<String, String> {
    //去掉前后空格
    @Override
    public String convert(String source) {
        try {
            if (null != source) {
                source = source.trim();
                if (!"".equals(source)) {
                    return source;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}

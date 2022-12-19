package com.qinweizhao.blog.model.enums;

import org.springframework.lang.Nullable;

/**
 * Input type enum.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public enum InputType {

    /**
     * Text input type
     */
    TEXT,

    /**
     * Number input type
     */
    NUMBER,

    /**
     * Radio box input type
     */
    RADIO,

    /**
     * Select input type
     */
    SELECT,

    /**
     * Textarea input type
     */
    TEXTAREA,

    /**
     * Color picker input type
     */
    COLOR,

    /**
     * Attachment picker input type
     */
    ATTACHMENT,

    /**
     * Switch input type, only true or false
     */
    SWITCH;

    /**
     * Convert type to input type.
     *
     * @param type input type
     * @return corresponding input type or TEXT if the given type is missing or mismatch
     */
    public static InputType typeOf(@Nullable Object type) {
        if (type != null) {
            for (InputType inputType : values()) {
                if (inputType.name().equalsIgnoreCase(type.toString())) {
                    return inputType;
                }
            }
        }
        return TEXT;
    }

}

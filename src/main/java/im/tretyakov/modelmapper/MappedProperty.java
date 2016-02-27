package im.tretyakov.modelmapper;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * Annotation which describes configuration for property mapping
 *
 * Created by talbot on 27.02.16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MappedProperty {

    /**
     * Property name, which will be used instead of field name
     *
     * @return Property name
     */
    @Nonnull
    String value() default "";

    /**
     * Exclude field from mapping
     *
     * @return {@code TRUE} if field should be excluded from mapping
     */
    boolean exclude() default false;
}

package im.tretyakov.modelmapper;

import java.lang.annotation.*;

/**
 * Marks a View model class which could be mapped to Entity
 *
 * Created by talbot on 27.02.16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedView {
}

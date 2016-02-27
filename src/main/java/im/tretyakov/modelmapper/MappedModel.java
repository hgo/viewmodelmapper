package im.tretyakov.modelmapper;

import java.lang.annotation.*;

/**
 * Marks an entity model class which could be mapped to view model
 *
 * Created by talbot on 27.02.16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedModel {
}

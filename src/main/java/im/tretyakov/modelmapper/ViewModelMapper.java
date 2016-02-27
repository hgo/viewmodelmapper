package im.tretyakov.modelmapper;

import javax.annotation.Nonnull;

/**
 * A mapper maps a source view object of type {@code V} (generally given view object DTO)
 * to a target model entity of type {@code M} or vice versa.
 * <p>
 * Created on 12.09.15.
 *
 * @author tretyakov (dmitry@tretyakov.im)
 */
public interface ViewModelMapper<V, M> {

    /**
     * Maps entity model of given type {@code M} into view object of type {@code V}
     *
     * @param model Model entity which will be mapped
     * @return A view mapped from a given model
     */
    @Nonnull
    V marshall(@Nonnull M model) throws IllegalStateException;

    /**
     * Maps a source view object of type {@code V} into a target model entity of type {@code M}
     *
     * @param view View object which will be mapped
     * @return A mapped model
     */
    @Nonnull
    M unMarshall(@Nonnull V view) throws IllegalStateException;
}

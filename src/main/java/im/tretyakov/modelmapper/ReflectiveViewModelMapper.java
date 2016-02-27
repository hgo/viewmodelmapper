package im.tretyakov.modelmapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Simple mapper which maps a source view object of type {@code V}
 * to a target model entity of type {@code M} using reflection.
 * <p/>
 * Created by talbot on 27.02.16.
 */
public class ReflectiveViewModelMapper<V, M> implements ViewModelMapper<V, M> {

    private final Class<V> viewClass;

    private final Class<M> modelClass;

    public ReflectiveViewModelMapper(@Nonnull Class<V> viewClass, @Nonnull Class<M> modelClass) {
        this.viewClass = viewClass;
        this.modelClass = modelClass;
    }

    /**
     * Maps entity model of given type {@code M} into view object of type {@code V}
     *
     * @param model Model entity which will be mapped
     * @return A view mapped from a given model
     */
    @Nonnull
    @Override
    public V marshall(@Nonnull M model) throws IllegalStateException {
        final V result;
        try {
            result = this.viewClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not instantiate view object of type " + this.viewClass.getClass(), e);
        }
        final Set<Field> fields = new HashSet<>();
        for (final Field field : this.viewClass.getDeclaredFields()) {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) ||
                    (field.isAnnotationPresent(MappedProperty.class) && field.getAnnotation(MappedProperty.class).exclude())
                    ) {
                continue;
            }
            fields.add(field);
        }
        for (final Field field : fields) {
            final MappedProperty annotation = field.getDeclaredAnnotation(MappedProperty.class);
            final String modelFieldName = annotation == null || annotation.value().isEmpty() ? field.getName() : annotation.value();
            final Object value;
            final Class<?> modelFieldClass = this.getPropertyType(model, modelFieldName);
            if (modelFieldClass == null) {
                continue;
            }
            Object data;
            try {
                final String methodPrefix = Boolean.class.equals(field.getType()) ? "is" : "get";
                final Method method = this.modelClass.getMethod(
                        methodPrefix + modelFieldName.substring(0, 1).toUpperCase() + modelFieldName.substring(1, modelFieldName.length())
                );
                if (method != null) {
                    data = method.invoke(model);
                } else {
                    data = null;
                }
                if (data != null) {
                    if (List.class.isAssignableFrom(field.getType()) && List.class.isAssignableFrom(data.getClass())) {
                        final ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
                        final ParameterizedType modelType;
                        modelType = (ParameterizedType) modelClass.getDeclaredField(modelFieldName).getGenericType();
                        value = this.getCollection(
                                (Class<?>) fieldType.getActualTypeArguments()[0],
                                (Class<?>) modelType.getActualTypeArguments()[0],
                                (List) data
                        );
                    } else if (modelFieldClass.isAnnotationPresent(MappedModel.class)
                            && field.getType().isAnnotationPresent(MappedView.class)
                            ) {
                        value = this.getMappedView(field.getType(), modelFieldClass, data);
                    } else if (modelFieldClass.equals(field.getType())) {
                        value = data;
                    } else {
                        throw new IllegalStateException(
                                "View type and model type are not the same: VIEW "
                                        + modelFieldClass.getName() + ", MODEL " + field.getType().getName()
                        );
                    }
                } else {
                    value = null;
                }
                if (this.isWritableProperty(result, field.getName())) {
                    this.setPropertyValue(result, field, value);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Error while processing reflection metadata: " + e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Maps a source view object of type {@code V} into a target model entity of type {@code M}
     *
     * @param view View object which will be mapped
     * @return A mapped model
     */
    @Nonnull
    @Override
    public M unMarshall(@Nonnull V view) throws IllegalStateException {
        final M result;
        try {
            result = this.modelClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not instantiate model object of type " + this.modelClass.getClass(), e);
        }

        if (!view.getClass().isAnnotationPresent(MappedView.class)) return result;
        final Set<Field> fields = new HashSet<>();
        for (Field field : this.viewClass.getDeclaredFields()) {
            if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) ||
                    (field.isAnnotationPresent(MappedProperty.class) && field.getAnnotation(MappedProperty.class).exclude())
                    ) {
                continue;
            }
            fields.add(field);
        }
        for (final Field field : fields) {
            final MappedProperty annotation = field.getDeclaredAnnotation(MappedProperty.class);
            String fieldName = annotation == null || annotation.value().isEmpty() ? field.getName() : annotation.value();
            final Class<?> viewFieldClass = field.getType();
            final Class<?> modelFieldClass = this.getPropertyType(result, fieldName);
            if (modelFieldClass == null) {
                continue;
            }
            try {
                final Field modelField = this.modelClass.getDeclaredField(fieldName);
                final Object data = this.getPropertyValue(view, field);
                final Object value;
                if (data != null) {
                    if (List.class.isAssignableFrom(viewFieldClass) && List.class.isAssignableFrom(data.getClass())) {
                        final ParameterizedType viewType = (ParameterizedType) field.getGenericType();
                        final ParameterizedType modelType = (ParameterizedType) modelClass.getDeclaredField(fieldName).getGenericType();
                        value = this.getCollection(
                                (Class<?>) modelType.getActualTypeArguments()[0],
                                (Class<?>) viewType.getActualTypeArguments()[0],
                                (List) data
                        );
                    } else if (modelFieldClass.isAnnotationPresent(MappedModel.class)
                            && viewFieldClass.isAnnotationPresent(MappedView.class)
                            ) {
                        value = this.getMappedModel(modelFieldClass, viewFieldClass, data);
                    } else if (modelFieldClass.equals(viewFieldClass)) {
                        value = data;
                    } else {
                        throw new IllegalStateException(
                                "View type and model type are not the same: VIEW "
                                        + modelFieldClass.getName() + ", MODEL " + field.getType().getName()
                        );
                    }
                } else {
                    value = null;
                }
                if (this.isWritableProperty(result, modelField.getName())) {
                    this.setPropertyValue(result, modelField, value);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Error while processing reflection metadata: " + e.getMessage(), e);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <S, D> List<S> getCollection(@Nonnull Class<S> sourceType, @Nonnull Class<D> destinationType, @Nonnull Collection data) {
        List<S> result = new ArrayList<>();
        for (Object item : data) {
            if (sourceType.isAnnotationPresent(MappedView.class)) {
                result.add(this.getMappedView(sourceType, destinationType, item));
            } else if (sourceType.isAnnotationPresent(MappedModel.class)) {
                result.add(this.getMappedModel(sourceType, destinationType, item));
            } else if (sourceType.equals(destinationType)) {
                result.add((S) item);
            } else {
                throw new IllegalArgumentException(
                        "Trying to map collections of incompatible types: " + sourceType.getName() + ", " + destinationType.getName());
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private <VT, MT> VT getMappedView(@Nonnull Class<VT> viewType, @Nonnull Class<MT> modelType, @Nonnull Object data) {
        return new ReflectiveViewModelMapper<>(viewType, modelType).marshall((MT) data);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private <MT, VT> MT getMappedModel(@Nonnull Class<MT> modelType, @Nonnull Class<VT> viewType, @Nonnull Object data) {
        return new ReflectiveViewModelMapper<>(viewType, modelType).unMarshall((VT) data);
    }

    @Nullable
    private Class<?> getPropertyType(@Nonnull Object object, @Nonnull String propertyName) {
        Class<?> clazz;
        try {
            final Field field = object.getClass().getDeclaredField(propertyName);
            clazz = field == null ? null : field.getType();
        } catch (NoSuchFieldException e) {
            clazz = null;
        }
        return clazz;
    }

    private boolean isWritableProperty(@Nonnull Object object, @Nonnull String propertyName) {
        boolean isWritable;
        try {
            final Field field = object.getClass().getDeclaredField(propertyName);
            isWritable = field != null && !Modifier.isFinal(field.getModifiers());
        } catch (NoSuchFieldException e) {
            isWritable = false;
        }
        return isWritable;
    }

    private void setPropertyValue(
            @Nonnull Object object,
            @Nonnull Field field,
            @Nullable Object value
    ) throws ReflectiveOperationException {
        field.setAccessible(true);
        field.set(object, field.getType().cast(value));
    }

    private Object getPropertyValue(@Nonnull Object object, @Nonnull Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(object);
    }
}

package fr.bimiot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Builder<T> {
    private final Supplier<T> instantiator;
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    private Builder(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> Builder<T> of(Supplier<T> instantiator) {
        return new Builder<>(instantiator);
    }

    public <U> Builder<T> with(BiConsumer<T, U> setter, U value) {
        Consumer<T> consumer = instance -> setter.accept(instance, value);
        modifiers.add(consumer);
        return this;
    }

    public T build(){
        T instance = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(instance));
        modifiers.clear();
        return instance;
    }
}

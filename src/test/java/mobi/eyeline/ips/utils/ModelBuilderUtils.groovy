package mobi.eyeline.ips.utils

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

class ModelBuilderUtils {

    static class Deferred {

        static interface DeferredReference<T> {
            T resolve(List<T> _)
        }

        static <T> T resolve(List<T> others, Map classifier) {
            if (!classifier) throw new IllegalArgumentException()

            def matches = { obj ->
                // Ensure all the properties declared in classifier match.
                classifier.keySet().collect { k -> (obj.properties[k] == classifier[k]) }.every()
            }

            def resolved = others.findAll { matches(it) }
            switch (resolved.size()) {
                case 1:     return resolved.first()
                default:    throw new IllegalArgumentException('Unable to resolve reference')
            }
        }

        static <T> List<T> resolveAll(List<T> list) {
            list.collect {
                switch (it) {
                    case DeferredReference: return (it as DeferredReference).resolve(list)
                    default:                return it
                }
            }
        }
    }

    static abstract class Context<T> {
        final T enclosing

        Context(T enclosing) { this.enclosing = enclosing }

        /**
         * Calls {@code closure} on the {@code obj} iff {@link #enclosing} object is set.
         */
        protected <J> J bind(J obj, @ClosureParams(FirstParam) Closure closure) {
            if (enclosing) closure.call(obj)
            obj
        }

        T invoke(Closure closure) { this.with closure; enclosing }
    }

    static abstract class ListContext<T> {
        final Map common
        protected final List<T> list = []

        ListContext(Map common) { this.common = common.asImmutable() }

        protected T create(T obj, @ClosureParams(FirstParam) Closure closure) {
            closure.call obj
            add(obj)
        }

        protected T add(T obj) { list << obj; obj }

        /**
         * Should probably be overridden to provide proper {@code @DelegatesTo} type.
         */
        protected List<T> invoke(Closure closure) { this.with closure; list }
    }
}

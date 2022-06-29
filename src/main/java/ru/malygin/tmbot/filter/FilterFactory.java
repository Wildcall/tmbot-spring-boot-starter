package ru.malygin.tmbot.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class FilterFactory {

    private final Map<Class<? extends AbstractFilter>, AbstractFilter> filtersMap;
    private final List<Class<? extends AbstractFilter>> filters;

    public FilterFactory(List<AbstractFilter> filtersMap) {
        this.filtersMap = filtersMap
                .stream()
                .collect(Collectors.toUnmodifiableMap(AbstractFilter::getClass, Function.identity()));
        initFilters(this.filtersMap.values());
        this.filters = this.filtersMap
                .keySet()
                .stream()
                .sorted(Comparator.nullsLast(this::orderComparator))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Class<? extends AbstractFilter>> getFilters() {
        return filters;
    }

    public AbstractFilter getFilter(Class<? extends AbstractFilter> clazz) {
        return this.filtersMap.get(clazz);
    }

    private void initFilters(Collection<? extends AbstractFilter> filters) {
        filters.forEach(filter -> filter.init(filter));
    }

    private int orderComparator(Class<? extends AbstractFilter> o1,
                                Class<? extends AbstractFilter> o2) {
        UpdateFilterOrder var1 = o1.getDeclaredAnnotation(UpdateFilterOrder.class);
        UpdateFilterOrder var2 = o2.getDeclaredAnnotation(UpdateFilterOrder.class);
        int o1Order = var1 == null ? 0 : var1.value();
        int o2Order = var2 == null ? 0 : var2.value();
        return o2Order - o1Order;
    }
}

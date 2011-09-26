package org.richfaces.cdk.ordering;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class PartialOrderToCompleteOrder<T> {

    private Set<T> allOptions = Sets.newLinkedHashSet();
    private List<PartialOrdering> partialOrderings = new LinkedList<PartialOrdering>();
    private Map<T, Set<T>> dependencies = Maps.newLinkedHashMap();

    public void addPartialOrder(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        checkCurrentPartialOrders(collection);

        allOptions.addAll(collection);
        partialOrderings.add(new PartialOrdering(collection));
        registerDependencies(Lists.newLinkedList(collection));
    }

    private void checkCurrentPartialOrders(Collection<T> collection) {
        for (PartialOrdering p : partialOrderings) {
            List<T> filtered = p.filter(collection);
            if (!p.isStrictlyOrdered(filtered)) {
                throw new IllegalPartialOrderingException("\ncollection: " + collection + "\n" + p);
            }
        }
    }

    private void registerDependencies(Collection<T> collection) {
        List<T> reversedOrder = Lists.reverse(Lists.newLinkedList(collection));
        Set<T> newItemDependencies = Sets.newLinkedHashSet(collection);
        
        for (T newItem : reversedOrder) {
            newItemDependencies.remove(newItem);
            registerDependenciesForItem(newItem, Sets.newLinkedHashSet(newItemDependencies));
        }
    }

    private void registerDependenciesForItem(T item, Set<T> newItemDependencies) {
        if (!dependencies.containsKey(item)) {
            dependencies.put(item, Sets.<T>newHashSet());
        }
        Set<T> itemDependences = dependencies.get(item);
        itemDependences.addAll(newItemDependencies);
    }

    public Set<T> getAllOptions() {
        return allOptions;
    }

    public Ordering<T> getCompleteOrdering() {
        return new CompleteOrdering();
    }

    public Collection<T> getCompletelyOrdered() {
        return new CompleteOrdering().sortedCopy(allOptions);
    }

    public class CompleteOrdering extends Ordering<T> {
        
        private Set<T> ordered = getCurrentOrder();
        
        private Predicate<T> IS_ORDERED = new Predicate<T>() {
            public boolean apply(T item) {
                return ordered.contains(item);
            }
        };
        
        public int compare(T left, T right) {
            if (!ordered.contains(left) || !ordered.contains(right)) {
                throw new IllegalStateException();
            }
            if (left.equals(right)) {
                return 0;
            }
            for (T item : ordered) {
                if (item.equals(left)) {
                    return -1;
                } else if (item.equals(right)) {
                    return +1;
                }
            }
            throw new IllegalStateException();
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
            List<T> originList = (List<T>) Lists.newLinkedList(iterable);
            Collection<T> onlyOrdered = Collections2.filter(originList, IS_ORDERED);
            Collection<T> onlyNotOrdered = Collections2.filter(originList, Predicates.not(IS_ORDERED));
            
            List<T> itemsInOrder = super.sortedCopy(onlyOrdered);
            itemsInOrder.addAll(onlyNotOrdered);
            
            return (List<E>) itemsInOrder;
            
        }
        
        public Set<T> getCurrentOrder() {
            Set<T> result = Sets.newLinkedHashSet();
            DependencyResolver resolver = new DependencyResolver();
            
            for (int i = 0; i < dependencies.size(); i++) {
                List<T> nodesWithoutDependencies = resolver.findNodesWithoutDependencies();
                result.addAll(nodesWithoutDependencies);
                resolver.removeNodes(nodesWithoutDependencies);
            }
            
            if (resolver.getSize() > 0) {
                throw new IllegalStateException();
            }
            
            return result;
        }
        
        private class DependencyResolver {
            private Map<T, Set<T>> deps = deepCopyOfDependencies();
            
            private List<T> findNodesWithoutDependencies() {
                List<T> list = Lists.newLinkedList();
                for (Entry<T, Set<T>> entry : deps.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        list.add(entry.getKey());
                    }
                }
                return list;
            }
            
            private void removeNodes(List<T> nodes) {
                for (Set<T> values : deps.values()) {
                    values.removeAll(nodes);
                }
                for (T node : nodes) {
                    deps.remove(node);
                }
            }
            
            private Map<T, Set<T>> deepCopyOfDependencies() {
                Map<T, Set<T>> result = Maps.newLinkedHashMap();
                for (Entry<T, Set<T>> entry : dependencies.entrySet()) {
                    result.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
                }
                return result;
            }
            
            public int getSize() {
                return deps.size();
            }
        }
    }

    private class PartialOrdering extends Ordering<T> {
        private LinkedList<T> order = Lists.newLinkedList();
        private HashSet<T> items = Sets.newHashSet();

        public PartialOrdering(Collection<T> collection) {
            order = Lists.newLinkedList(collection);
            items = Sets.newHashSet(collection);
        }

        public int compare(T left, T right) {
            if (!items.contains(left)) {
                throw new IllegalArgumentException("'" + left + "' is not part of this partial ordering");
            }
            if (!items.contains(right)) {
                throw new IllegalArgumentException("'" + right + "' is not part of this partial ordering");
            }
            return order.indexOf(left) - order.indexOf(right);
        }

        public List<T> filter(Collection<T> collection) {
            List<T> list = new LinkedList<T>(collection);
            list.retainAll(items);
            return list;
        }

        public boolean contains(T... itemz) {
            for (T item : itemz) {
                if (!items.contains(item)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "PartialOrder" + order;
        }
    }
}

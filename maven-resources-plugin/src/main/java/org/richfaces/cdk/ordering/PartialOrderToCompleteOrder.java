package org.richfaces.cdk.ordering;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class PartialOrderToCompleteOrder<T> {

    private Set<T> allOptions = Sets.newLinkedHashSet();
    private List<PartialOrdering> partialOrderings = new LinkedList<PartialOrdering>();

    public void addPartialOrder(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        checkCurrentPartialOrders(collection);

        allOptions.addAll(collection);
        partialOrderings.add(new PartialOrdering(collection));
    }

    private void checkCurrentPartialOrders(Collection<T> collection) {
        for (PartialOrdering p : partialOrderings) {
            List<T> filtered = p.filter(collection);
            if (!p.isStrictlyOrdered(filtered)) {
                throw new IllegalPartialOrderingException("\ncollection: " + collection + "\n" + p);
            }
        }
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

        public int compare(T left, T right) {
            for (PartialOrdering p : partialOrderings) {
                if (p.contains(left, right)) {
                    return p.compare(left, right);
                }
            }
            if (!allOptions.contains(left)) {
                return +1;
            } else if (!allOptions.contains(right)) {
                return -1;
            } else {
                if (left instanceof Comparable) {
                    return ((Comparable) left).compareTo(right);
                }
                return left.toString().compareTo(right.toString());
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

package projavafx.collections;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class FXCollectionsExample {
    public static void main(String[] args) {
        ObservableList<String> strings = FXCollections.observableArrayList();
        strings.addListener(new MyListener());

        System.out.println("Calling addAll(\"Zero\", \"One\", \"Two\", \"Three\"): ");
        strings.addAll("Zero", "One", "Two", "Three");

        System.out.println("Calling copy: ");
        FXCollections.copy(strings, Arrays.asList("Four", "Five"));

        System.out.println("Calling replaceAll: ");
        FXCollections.replaceAll(strings, "Two", "Two_1");

        System.out.println("Calling reverse: ");
        FXCollections.reverse(strings);

        System.out.println("Calling rotate(strings, 2): ");
        FXCollections.rotate(strings, 2);

        System.out.println("Calling shuffle(strings): ");
        FXCollections.shuffle(strings);

        System.out.println("Calling shuffle(strings, new Random(0L)): ");
        FXCollections.shuffle(strings, new Random(0L));

        System.out.println("Calling sort(strings): ");
        FXCollections.sort(strings);

        System.out.println("Calling sort(strings, c) with custom comparator: ");
        FXCollections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                // Reverse the order
                return rhs.compareTo(lhs);
            }
        });

        System.out.println("Calling fill(strings, \"Ten\"): ");
        FXCollections.fill(strings, "Ten");
    }

    private static class MyListener implements ListChangeListener<String> {
        @Override
        public void onChanged(Change<? extends String> change) {
            System.out.println("\tlist = " + change.getList());
            System.out.println(prettyPrint(change));
        }

        private String prettyPrint(Change<? extends String> change) {
            StringBuilder sb = new StringBuilder("\tChange event data:\n");
            int i = 0;
            while (change.next()) {
                sb.append("\t\tcursor = ")
                    .append(i++)
                    .append("\n");

                final String kind =
                    change.wasPermutated() ? "permutated" :
                        change.wasReplaced() ? "replaced" :
                            change.wasRemoved() ? "removed" :
                                change.wasAdded() ? "added" : "none";

                sb.append("\t\tKind of change: ")
                    .append(kind)
                    .append("\n");

                sb.append("\t\tAffected range: [")
                    .append(change.getFrom())
                    .append(", ")
                    .append(change.getTo())
                    .append("]\n");

                if (kind.equals("added") || kind.equals("replaced")) {
                    sb.append("\t\tAdded size: ")
                        .append(change.getAddedSize())
                        .append("\n");
                    sb.append("\t\tAdded sublist: ")
                        .append(change.getAddedSubList())
                        .append("\n");
                }

                if (kind.equals("removed") || kind.equals("replaced")) {
                    sb.append("\t\tRemoved size: ")
                        .append(change.getRemovedSize())
                        .append("\n");
                    sb.append("\t\tRemoved: ")
                        .append(change.getRemoved())
                        .append("\n");
                }

                if (kind.equals("permutated")) {
                    StringBuilder permutationStringBuilder = new StringBuilder("[");
                    for (int k = change.getFrom(); k < change.getTo(); k++) {
                        permutationStringBuilder.append(k)
                            .append("->")
                            .append(change.getPermutation(k));
                        if (k < change.getTo() - 1) {
                            permutationStringBuilder.append(", ");
                        }
                    }
                    permutationStringBuilder.append("]");
                    String permutation = permutationStringBuilder.toString();
                    sb.append("\t\tPermutation: ").append(permutation).append("\n");
                }
            }
            return sb.toString();
        }
    }
}

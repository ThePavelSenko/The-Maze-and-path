package backend.academy;

import lombok.Getter;

@Getter
public class Edge {
    private final Cell cell1;
    private final Cell cell2;
    private final int weight;

    public Edge(final Cell cell1, final Cell cell2, final Integer weight) {
        if (cell1 == null || cell2 == null) {
            throw new IllegalArgumentException("Cells in an Edge cannot be null");
        }
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("Weight must be non-null and non-negative");
        }
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.weight = weight;
    }

    public Edge(final Cell cell1, final Cell cell2) {
        this(cell1, cell2, 0); // default weight
    }
}


package backend.academy;

import lombok.Getter;

@Getter public class Edge {
    private final Cell cell1;
    private final Cell cell2;
    private final int weight;

    public Edge(final Cell cell1, final Cell cell2, final Integer weight) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.weight = weight;
    }

    public Edge(final Cell cell1, final Cell cell2) {
        this(cell1, cell2, 0);
    }
}

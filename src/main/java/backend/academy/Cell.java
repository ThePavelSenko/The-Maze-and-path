package backend.academy;

public record Cell(int row, int col) {
    public Cell {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Row and Column must be non-negative");
        }
    }
}


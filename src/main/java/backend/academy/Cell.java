package backend.academy;

public record Cell(int row, int col) {
    public int row() {
        return this.row;
    }

    public int col() {
        return this.col;
    }
}

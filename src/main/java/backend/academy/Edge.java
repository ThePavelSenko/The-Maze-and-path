package backend.academy;

public record Edge(Cell cell1, Cell cell2) {
    public Cell cell1() {
        return cell1;
    }

    public Cell cell2() {
        return cell2;
    }
}

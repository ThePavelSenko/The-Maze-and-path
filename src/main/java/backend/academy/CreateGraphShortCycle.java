package backend.academy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/** Interface for creating graphs with short cycles. **/
public interface CreateGraphShortCycle {
    int MIN_CYCLE_LENGTH = 10;


    default boolean createsShortCycle(Edge edge, List<Edge> currentEdges) {
        Cell cell1 = edge.cell1();
        Cell cell2 = edge.cell2();

        Queue<Cell> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Integer> distances = new HashMap<>();

        queue.add(cell1);
        visited.add(cell1);
        distances.put(cell1, 0);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            int distance = distances.get(current);

            if (distance >= MIN_CYCLE_LENGTH) {
                return false;
            }

            for (Cell neighbor : getNeighbors(current, currentEdges)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    distances.put(neighbor, distance + 1);

                    if (neighbor.equals(cell2) && distance + 1 < MIN_CYCLE_LENGTH) {
                        return true;
                    }
                }
            }
        }
        return false; // No short cycle found
    }

    private List<Cell> getNeighbors(Cell cell, List<Edge> edges) {
        List<Cell> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.cell1().equals(cell)) {
                neighbors.add(edge.cell2());
            } else if (edge.cell2().equals(cell)) {
                neighbors.add(edge.cell1());
            }
        }
        return neighbors;
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final int n;
  private int openSpaces = 0;
  private boolean[] grid;
  private final WeightedQuickUnionUF connections;
  private int lastOpenTop = -1;
  private int lastOpenBottom = -1;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int num) {
    if (num <= 0) {
      throw new IllegalArgumentException();
    }
    n = num;
    grid = new boolean[n*n];
    connections = new WeightedQuickUnionUF(n*n);
  }

  // Transforms a (row,col) coordinate to a flatten one
  private int coord2idx(int row, int col) {
    return (row-1) * n + col - 1;
  }

  // Checks if the index values are inside the grid
  private boolean inGrid(int row, int col) {
    return row > 0 && row <= n && col > 0 && col <= n;
  }

  // Connect the specified tile to its neighbors
  private void connectNeighbors(int row, int col) {
    if (!inGrid(row, col) || !isOpen(row, col)) {
      return;
    }

    int idx = coord2idx(row, col);

    int[][] neighbors = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};

    for (int[] nCoords: neighbors) {
      int nRow = row + nCoords[0];
      int nCol = col + nCoords[1];
      int nIdx = coord2idx(nRow, nCol);
      // Check if neighbor exists and isOpen
      if (inGrid(nRow, nCol) && isOpen(nRow, nCol)) {
        connections.union(idx, nIdx);
      }
    }
  }

  // opens the site (row, col) if it is not open already

  public void open(int row, int col) {
    if (!inGrid(row, col)) {
      throw new IllegalArgumentException();
    }
    if (isOpen(row, col)) {
      return;
    }
    int idx = coord2idx(row, col);
    grid[idx] = true;
    openSpaces++;

    // Connect to top group if applicable
    if (row == 1) {
      if (lastOpenTop >= 0) {
        connections.union(idx, lastOpenTop);
      }
      lastOpenTop = idx;
    }

    // connect to bottom group if applicable
    if (row == n) {
      if (lastOpenBottom >= 0) {
        connections.union(idx, lastOpenBottom);
      }
      lastOpenBottom = idx;
    }

    // connect the open neighbors
    connectNeighbors(row, col);
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    if (!inGrid(row, col)) {
      throw new IllegalArgumentException();
    }
    return grid[coord2idx(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    if (!inGrid(row, col)) {
      throw new IllegalArgumentException();
    }
    if (lastOpenTop < 0 || !isOpen(row, col)) {
      return false;
    }
    int topRoot = connections.find(lastOpenTop);
    int siteRoot = connections.find(coord2idx(row, col));
    return topRoot == siteRoot;
  }

  // returns the nubmer of open sites
  public int numberOfOpenSites() {
    return openSpaces;
  }

  // does the system percolate?
  public boolean percolates() {
    if (lastOpenTop < 0 || lastOpenBottom < 0) {
      return false;
    }
    int topRoot = connections.find(lastOpenTop);
    int bottomRoot = connections.find(lastOpenBottom);
    return topRoot == bottomRoot;
  }

  public static void main(String[] args) {
    System.out.println("Testing Percolation Implementation");
    Percolation p = new Percolation(3);
    System.out.println("Created grid of 3x3");
    System.out.println("Grid should not percolate");
    if (p.percolates()) {
      throw new RuntimeException();
    }
    System.out.println("Opening top middle tile");
    p.open(1, 2);
    System.out.println("There should be 1 open tile");
    if (p.numberOfOpenSites() != 1) {
      throw new RuntimeException();
    }

    System.out.println("Opening bottom left tile");
    p.open(3, 1);
    System.out.println("There should be 2 open tiles");
    if (p.numberOfOpenSites() != 2) {
      throw new RuntimeException();
    }
    System.out.println("System should not percolate");
    if (p.percolates()) {
      throw new RuntimeException();
    }
    System.out.println("Opening middle tile");
    p.open(2, 2);
    System.out.println("There should be 3 open tiles");
    if (p.numberOfOpenSites() != 3) {
      throw new RuntimeException();
    }
    System.out.println("middle tile should be full");
    if (!p.isFull(2, 2)) {
      throw new RuntimeException();
    }
    System.out.println("System should not percolate");
    if (p.percolates()) {
      throw new RuntimeException();
    }
    System.out.println("Opening middle left tile");
    p.open(2, 1);
    System.out.println("There should be 4 open tiles");
    if (p.numberOfOpenSites() != 4) {
      throw new RuntimeException();
    }
    System.out.println("System should percolate");
    if (!p.percolates()) {
      throw new RuntimeException();
    }

    System.out.println("Should throw Illegal Argument Exception");
    try {
      p.open(-1, 3);
    } catch (IllegalArgumentException e) {
      System.out.println("Success");
    }
  }
}

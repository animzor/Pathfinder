package javafiler.graphs;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GraphMethods<N>{

public abstract void add(N ny);

public abstract void connect(N from, N to, String namn, int vikt);

public abstract Set<N> getNodes();

public abstract List<Edge<N>> getEdgesFrom(N nod);

public abstract Map<N, List<Edge<N>>> getNodesMap();

public abstract Edge<N> getEdgeBetween(N s1, N s2);

public abstract void dfs(N where, Set<N> besökta);

public abstract boolean pathExists(N from, N to);

public abstract void dfs2(N where, N whereFrom, Set<N> b, Map<N, N> via);

public abstract List<Edge<N>> anyPath(N from, N to);

public abstract void setConnectionWeight(N from, N to, int i);

public abstract List<N> Dijkstra(N from, N to);

public abstract String toString();

}
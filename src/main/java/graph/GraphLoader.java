package graph;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class to load graphs from JSON files.
 * Expected format:
 * {
 *   "vertices": n,
 *   "edges": [
 *     {"from": 0, "to": 1, "weight": 1.0},
 *     ...
 *   ]
 * }
 */
public class GraphLoader {
    
    /**
     * Load a graph from a JSON file
     */
    public static Graph loadFromJson(String filepath) throws IOException {
        String content = Files.readString(Paths.get(filepath));
        return parseJson(content);
    }
    
    /**
     * Parse JSON string to create a graph
     */
    private static Graph parseJson(String json) {
        // Simple JSON parser (avoiding external dependencies)
        json = json.trim();
        
        // Extract vertices count
        int vertices = extractInt(json, "vertices");
        Graph graph = new Graph(vertices);
        
        // Extract edges array
        String edgesStr = extractArray(json, "edges");
        if (edgesStr != null && !edgesStr.isEmpty()) {
            String[] edgeObjects = splitEdges(edgesStr);
            for (String edgeObj : edgeObjects) {
                if (edgeObj.trim().isEmpty()) continue;
                
                int from = extractInt(edgeObj, "from");
                int to = extractInt(edgeObj, "to");
                double weight = extractDouble(edgeObj, "weight", 1.0);
                
                graph.addEdge(from, to, weight);
            }
        }
        
        return graph;
    }
    
    private static int extractInt(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*(\\d+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        throw new IllegalArgumentException("Key not found: " + key);
    }
    
    private static double extractDouble(String json, String key, double defaultValue) {
        String pattern = "\"" + key + "\"\\s*:\\s*([\\d.]+)";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return defaultValue;
    }
    
    private static String extractArray(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]+)\\]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.DOTALL);
        java.util.regex.Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
    
    private static String[] splitEdges(String edgesStr) {
        List<String> edges = new ArrayList<>();
        int braceCount = 0;
        int start = 0;
        
        for (int i = 0; i < edgesStr.length(); i++) {
            char c = edgesStr.charAt(i);
            if (c == '{') {
                if (braceCount == 0) start = i;
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    edges.add(edgesStr.substring(start, i + 1));
                }
            }
        }
        
        return edges.toArray(new String[0]);
    }
    
    /**
     * Save a graph to JSON file
     */
    public static void saveToJson(Graph graph, String filepath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"vertices\": ").append(graph.getVertices()).append(",\n");
        sb.append("  \"edges\": [\n");
        
        boolean first = true;
        for (int v = 0; v < graph.getVertices(); v++) {
            for (Graph.Edge edge : graph.getAdjacent(v)) {
                if (!first) sb.append(",\n");
                first = false;
                sb.append("    {\"from\": ").append(v)
                  .append(", \"to\": ").append(edge.to)
                  .append(", \"weight\": ").append(edge.weight)
                  .append("}");
            }
        }
        
        sb.append("\n  ]\n");
        sb.append("}\n");
        
        Files.writeString(Paths.get(filepath), sb.toString());
    }
}

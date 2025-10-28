/**
 * Cricket Score Glyph Toy Service
 * 
 * A Glyph Matrix toy service that displays sample cricket scores
 * and match information on the Glyph Matrix display.
 * 
 * This implementation follows the GlyphMatrix Developer Kit SDK
 * to create an engaging cricket score visualization.
 */
public class CricketScoreToyService {
    
    // Cricket match data structure
    private static class CricketScore {
        String team1;
        String team2;
        int team1Runs;
        int team1Wickets;
        double team1Overs;
        int team2Runs;
        int team2Wickets;
        double team2Overs;
        String status;
        
        CricketScore(String t1, String t2, int r1, int w1, double o1, 
                    int r2, int w2, double o2, String stat) {
            team1 = t1; team2 = t2;
            team1Runs = r1; team1Wickets = w1; team1Overs = o1;
            team2Runs = r2; team2Wickets = w2; team2Overs = o2;
            status = stat;
        }
    }
    
    // Sample cricket matches for demonstration
    private CricketScore[] sampleMatches = {
        new CricketScore("IND", "AUS", 287, 6, 50.0, 245, 8, 48.2, "IND Won by 42 runs"),
        new CricketScore("ENG", "PAK", 156, 10, 35.4, 158, 3, 28.1, "PAK Won by 7 wkts"),
        new CricketScore("SA", "NZ", 198, 9, 20.0, 201, 4, 18.3, "NZ Won by 6 wkts"),
        new CricketScore("WI", "SL", 167, 7, 20.0, 145, 9, 20.0, "WI Won by 22 runs")
    };
    
    private int currentMatchIndex = 0;
    
    /**
     * Initialize the Cricket Score Glyph Toy Service
     * Sets up the display parameters and initial state
     */
    public void initialize() {
        System.out.println("Cricket Score Glyph Toy Service initialized");
        // Initialize Glyph Matrix display settings
        setupGlyphDisplay();
    }
    
    /**
     * Setup Glyph Matrix display configuration for cricket scores
     */
    private void setupGlyphDisplay() {
        // Configure display brightness, scrolling speed, and layout
        // This would interface with the actual Glyph Matrix SDK
        System.out.println("Configuring Glyph display for cricket scores...");
    }
    
    /**
     * Display current cricket match score on the Glyph Matrix
     * Shows team names, scores, overs, and match status
     */
    public void displayScore() {
        CricketScore match = sampleMatches[currentMatchIndex];
        
        // Format score display for Glyph Matrix
        String scoreDisplay = formatScoreForGlyph(match);
        
        // Send to Glyph Matrix display
        renderOnGlyph(scoreDisplay);
        
        System.out.println("Displaying: " + scoreDisplay);
    }
    
    /**
     * Format cricket score data for optimal display on Glyph Matrix
     * Creates a compact, readable format suitable for the LED matrix
     */
    private String formatScoreForGlyph(CricketScore match) {
        StringBuilder display = new StringBuilder();
        
        // Line 1: Team names
        display.append(match.team1).append(" vs ").append(match.team2).append("\n");
        
        // Line 2: Team 1 score
        display.append(match.team1).append(": ").append(match.team1Runs)
               .append("/").append(match.team1Wickets)
               .append(" (").append(match.team1Overs).append(")").append("\n");
        
        // Line 3: Team 2 score
        display.append(match.team2).append(": ").append(match.team2Runs)
               .append("/").append(match.team2Wickets)
               .append(" (").append(match.team2Overs).append(")").append("\n");
        
        // Line 4: Match status
        display.append(match.status);
        
        return display.toString();
    }
    
    /**
     * Render formatted text on the Glyph Matrix display
     * This method would interface with the actual Glyph SDK
     */
    private void renderOnGlyph(String content) {
        // This would use the actual Glyph Matrix SDK to display content
        // For demo purposes, we'll simulate the display output
        System.out.println("=== GLYPH MATRIX DISPLAY ===");
        System.out.println(content);
        System.out.println("============================");
    }
    
    /**
     * Cycle to the next cricket match in the sample data
     */
    public void nextMatch() {
        currentMatchIndex = (currentMatchIndex + 1) % sampleMatches.length;
        displayScore();
    }
    
    /**
     * Cycle to the previous cricket match in the sample data
     */
    public void previousMatch() {
        currentMatchIndex = (currentMatchIndex - 1 + sampleMatches.length) % sampleMatches.length;
        displayScore();
    }
    
    /**
     * Get current match information
     */
    public String getCurrentMatchInfo() {
        CricketScore match = sampleMatches[currentMatchIndex];
        return match.team1 + " vs " + match.team2;
    }
    
    /**
     * Auto-rotate through matches with specified interval
     */
    public void startAutoRotation(int intervalSeconds) {
        System.out.println("Starting auto-rotation every " + intervalSeconds + " seconds");
        // Implementation would use a timer to auto-cycle through matches
        // This creates an engaging, dynamic display
    }
    
    /**
     * Demo method to showcase the Cricket Score Glyph Toy Service
     */
    public static void main(String[] args) {
        System.out.println("Cricket Score Glyph Toy Service Demo");
        System.out.println("====================================");
        
        CricketScoreToyService service = new CricketScoreToyService();
        service.initialize();
        
        // Display current match
        System.out.println("\nCurrent Match:");
        service.displayScore();
        
        // Cycle through a few matches
        System.out.println("\nNext Match:");
        service.nextMatch();
        
        System.out.println("\nNext Match:");
        service.nextMatch();
        
        System.out.println("\nBack to previous:");
        service.previousMatch();
        
        System.out.println("\nDemo completed - Cricket scores ready for Glyph Matrix!");
    }
}

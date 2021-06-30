import java.awt.Color;

public enum Tile {
    SPACE(' ', AsciiPanel.black), //(char)250
    BGSTAR('.', AsciiPanel.white), //(char)250
    BGSTAR2((char)250, AsciiPanel.white), //(char)250
    PLANETGREEN('#', new Color(16, 83, 19)),
    PLANETBLUE('#', new Color(8, 73, 104)),
    PLANETRED('#', new Color(168, 65, 65)),
    PLANETPINK('#', new Color(255, 170, 207)),
    PLANETGREY('#', new Color(130, 130, 130)),
    PLANETBROWN('#', new Color(71, 61, 43)),
    PLANETLIGHTBROWN('#', new Color(137, 118, 97)),
    PLANETDARKGREY('#', new Color(56, 56, 56)),
    PLANETPOLAR('#', new Color(236, 231, 231)),

    IMAGESPACE(' ', new Color(33, 32, 32)),

    REDGIANT('0', Color.red),
    YELLOWSTAR('o',new Color(255, 213, 0)),
    NEUTRONSTAR('*', Color.cyan),
    WHITESTAR('o',new Color(255, 255, 255)),


    UNKNOWN(' ', AsciiPanel.white),
    BOUNDS('x', AsciiPanel.brightBlack);

    private boolean spotted = false;
    public boolean isSpotted() {
        return spotted;
    }
    public void setSpotted(boolean what) {
        this.spotted = what;
    }
    private boolean visible;
    public boolean visible() {return visible;}

    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    Tile(char glyph, Color color){
        this.glyph = glyph;
        this.color = color;
        this.visible = true;
    }

}
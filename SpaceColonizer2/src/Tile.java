import java.awt.Color;

public enum Tile {
    SPACE(' ', AsciiPanel.black, new Color(0,0,0,0)), //(char)250
    BGSTAR('.', AsciiPanel.white, new Color(0,0,0,0)), //(char)250
    BGSTAR2((char)250, AsciiPanel.white, new Color(0,0,0,0)), //(char)250

    PLANETSHALLOW('#', Color.white, new Color(0,0,0,0)),
    PLANETDEEP('#', Color.white, new Color(0,0,0,0)),
    PLANETPOLAR('#', new Color(236, 231, 231), new Color(0,0,0,0)),

    PLANETGREEN('#', new Color(16, 83, 19), new Color(0,0,0,0)),
    PLANETBLUE('#', new Color(8, 73, 104), new Color(0,0,0,0)),
    PLANETRED('#', new Color(168, 65, 65), new Color(0,0,0,0)),
    PLANETPINK('#', new Color(255, 170, 207), new Color(0,0,0,0)),
    PLANETGREY('#', new Color(130, 130, 130), new Color(0,0,0,0)),
    PLANETBROWN('#', new Color(71, 61, 43), new Color(0,0,0,0)),
    PLANETLIGHTBROWN('#', new Color(137, 118, 97), new Color(0,0,0,0)),
    PLANETDARKGREY('#', new Color(56, 56, 56), new Color(0,0,0,0)),


    IMAGESPACE(' ', new Color(33, 32, 32), new Color(0,0,0,0)),

    REDGIANT('0', new Color(255, 0, 0), Color.black),
    YELLOWSTAR('o',new Color(255, 222, 0), Color.black),
    NEUTRONSTAR('*', new Color(0, 237, 255), Color.black),
    WHITESTAR('o',new Color(255, 255, 255),Color.black),


    UNKNOWN(' ', AsciiPanel.white, new Color(0,0,0,0)),
    BOUNDS('x', AsciiPanel.brightBlack, new Color(0,0,0,0));

    private boolean spotted = false;
    public boolean isSpotted() {
        return spotted;
    }
    public void setSpotted(boolean what) {
        this.spotted = what;
    }
    private boolean visible;
    public boolean visible() {return visible;}

    public Color bgcolor;
    public Color bgcolor() { return bgcolor; }

    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    Tile(char glyph, Color color, Color bgcolor){
        this.glyph = glyph;
        this.color = color;
        this.visible = true;
        this.bgcolor = bgcolor;
    }

    public boolean isSpace() {
        if (this == Tile.SPACE || this == Tile.BGSTAR || this == Tile.BGSTAR2) {
            return true;
        }
        return false;
    }

}
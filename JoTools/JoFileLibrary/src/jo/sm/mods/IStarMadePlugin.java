package jo.sm.mods;

public interface IStarMadePlugin
{
    public String getName();
    public String getDescription();
    public String getAuthor();
    public Object getParameterBean();
    public int[][] getClassifications(); // list of type, subtypes
}

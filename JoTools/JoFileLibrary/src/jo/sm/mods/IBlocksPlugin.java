package jo.sm.mods;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.ship.data.Block;

public interface IBlocksPlugin extends IStarMadePlugin
{
    public static final int TYPE_SHIP = 1;
    public static final int TYPE_STATION = 2;
    public static final int TYPE_SHOP = 3;
    public static final int TYPE_PLANET = 4;
    public static final int TYPE_FLOATINGROCK = 5;

    public static final int SUBTYPE_PAINT = 1;
    public static final int SUBTYPE_MODIFY = 2;
    public static final int SUBTYPE_GENERATE = 3;
    public static final int SUBTYPE_EDIT = 4;
    public static final int SUBTYPE_FILE = 5;
    public static final int SUBTYPE_VIEW = 6;

    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb);
}

package jo.sm.mods;

import jo.sm.data.SparseMatrix;
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

    public SparseMatrix<Block> modify(SparseMatrix<Block> original, Object params);
}

package jo.sm.data;

import java.util.ArrayList;
import java.util.List;

import jo.sm.logic.GridLogic;
import jo.sm.ship.data.Block;

public class UndoBuffer
{
    private List<byte[]>    mBuffer;
    private int             mPointer;
    
    public UndoBuffer()
    {
        mBuffer = new ArrayList<byte[]>();
        mPointer = 0;
    }
    
    public SparseMatrix<Block> undo()
    {
        if (mPointer > 0)
        {
            mPointer--;
            return GridLogic.fromBytes(mBuffer.get(mPointer));
        }
        else
            return null;
    }
    
    public SparseMatrix<Block> redo()
    {
        if (mPointer < mBuffer.size())
            return GridLogic.fromBytes(mBuffer.get(mPointer++));
        else
            return null;
    }
    
    public void checkpoint(SparseMatrix<Block> grid)
    {
    	if (grid.size() > 10000)
    		return;
        while (mBuffer.size() > mPointer)
            mBuffer.remove(mPointer);
        mBuffer.add(GridLogic.toBytes(grid));
        mPointer++;
    }
    
    public void clear()
    {
        mBuffer.clear();
    }
}

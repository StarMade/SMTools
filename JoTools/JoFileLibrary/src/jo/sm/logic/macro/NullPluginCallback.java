package jo.sm.logic.macro;

import jo.sm.mods.IPluginCallback;

public class NullPluginCallback implements IPluginCallback
{
	private int	mTotal = 0;
	private int	mDone = 0;
	private int mPC = 0;

	@Override
	public void setStatus(String status)
	{
		System.out.println("Status: "+status);
	}

	@Override
	public void startTask(int size)
	{
		mTotal = size;
		mDone = 0;
		mPC = 0;
	}

	@Override
	public void workTask(int amnt)
	{
		int oldPC = mPC;
		mDone += amnt;
		mPC = mDone*100/mTotal;
		if (mPC != oldPC)
		{
			if (mPC%10 == 0)
				System.out.print(mPC+"%");
			else
				System.out.println(".");
		}
	}

	@Override
	public void endTask()
	{
		System.out.println();
	}

	@Override
	public boolean isPleaseCancel()
	{
		return false;
	}

	@Override
	public void setErrorTitle(String title)
	{
		System.out.println("Error: "+title);
	}

	@Override
	public void setErrorDescription(String desc)
	{
		System.out.println(desc);
	}

	@Override
	public void setError(Throwable t)
	{
		t.printStackTrace();
	}

}

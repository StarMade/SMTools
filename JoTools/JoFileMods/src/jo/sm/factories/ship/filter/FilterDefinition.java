package jo.sm.factories.ship.filter;

import java.util.Set;

public class FilterDefinition
{
	private String	mTitle;
	private String	mDescription;
	private String	mAuthor;
	private int		mPriority;
	private Set<Short>	mBlocks;
	
	public String getTitle()
	{
		return mTitle;
	}
	public void setTitle(String title)
	{
		mTitle = title;
	}
	public String getDescription()
	{
		return mDescription;
	}
	public void setDescription(String description)
	{
		mDescription = description;
	}
	public String getAuthor()
	{
		return mAuthor;
	}
	public void setAuthor(String author)
	{
		mAuthor = author;
	}
	public int getPriority()
	{
		return mPriority;
	}
	public void setPriority(int priority)
	{
		mPriority = priority;
	}
	public Set<Short> getBlocks()
	{
		return mBlocks;
	}
	public void setBlocks(Set<Short> blocks)
	{
		mBlocks = blocks;
	}
}

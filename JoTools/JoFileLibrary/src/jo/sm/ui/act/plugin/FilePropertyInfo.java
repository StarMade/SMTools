package jo.sm.ui.act.plugin;

import javax.swing.JFileChooser;


public class FilePropertyInfo
{
	private String	   mApproveButtonText;
	private String	   mApproveButtonTooltipText;
	private String	   mDialogTitle;
	private int        mDialogType;
	private int		   mFileSelectionMode;
	private String[][] mFilters;

	public FilePropertyInfo()
	{
		mDialogType = JFileChooser.OPEN_DIALOG;
		mFileSelectionMode = JFileChooser.FILES_ONLY;
	}
	
	public String[][] getFilters()
	{
		return mFilters;
	}

	public void setFilters(String[][] filters)
	{
		mFilters = filters;
	}

	public String getApproveButtonText()
	{
		return mApproveButtonText;
	}

	public void setApproveButtonText(String approveButtonText)
	{
		mApproveButtonText = approveButtonText;
	}

	public String getApproveButtonTooltipText()
	{
		return mApproveButtonTooltipText;
	}

	public void setApproveButtonTooltipText(String approveButtonTooltipText)
	{
		mApproveButtonTooltipText = approveButtonTooltipText;
	}

	public String getDialogTitle()
	{
		return mDialogTitle;
	}

	public void setDialogTitle(String dialogTitle)
	{
		mDialogTitle = dialogTitle;
	}

	public int getDialogType()
	{
		return mDialogType;
	}

	public void setDialogType(int dialogType)
	{
		mDialogType = dialogType;
	}

	public int getFileSelectionMode()
	{
		return mFileSelectionMode;
	}

	public void setFileSelectionMode(int fileSelectionMode)
	{
		mFileSelectionMode = fileSelectionMode;
	}
}

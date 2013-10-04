package jo.sm.mods;

public interface IPluginCallback
{
    public void setStatus(String status);
    public void startTask(int size);
    public void workTask(int amnt);
    public void endTask();
    public boolean isPleaseCancel();
    public void setErrorTitle(String title);
    public void setErrorDescription(String desc);
    public void setError(Throwable t);
}

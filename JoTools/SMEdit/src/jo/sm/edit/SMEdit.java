package jo.sm.edit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class SMEdit
{
    private String      mWebRoot;
    private Calendar    mLocalDate;
    private Calendar    mRemoteDate;
    private List<String[]> mTargets;
    
    public SMEdit()
    {
        mWebRoot = "http://www.ocean-of-storms.com/vote4joe/";
        mTargets = new ArrayList<String[]>();
    }
    
    public void run()
    {
        if (!validateCurrentDirectory())
        {
            JOptionPane.showMessageDialog(null, "The current directory is not the StarMade directory. Please move SMEdit.jar to the StarMade directory and run again.",
                    "Wrong Directory", JOptionPane.ERROR_MESSAGE);
            return;
        }
        readLocalDate();
        if (readRemoteFile())
            if (doWeUpdate())
                updateTargets();
        runSMEdit();
    }
    
    private void runSMEdit()
    {
        try
        {
            File jo_smJar = new File("./jo_sm.jar");
            URL url = jo_smJar.toURI().toURL();
            URLClassLoader smLoader = new URLClassLoader(new URL[]{url}, SMEdit.class.getClassLoader());
            Class<?> rf = smLoader.loadClass("jo.sm.ui.RenderFrame");
            Method main = rf.getMethod("main", String[].class);
            String[] args = new String[0];
            main.invoke(null, (Object)args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void updateTargets()
    {
        for (String[] target : mTargets)
        {
            try
            {
                URL remote = new URL(mWebRoot+target[1]);
                File local = new File(target[0]);
                System.out.println(remote+" -> "+local);
                InputStream is = new BufferedInputStream(remote.openStream());
                OutputStream os = new BufferedOutputStream(new FileOutputStream(local));
                for (;;)
                {
                    int ch = is.read();
                    if (ch == -1)
                        break;
                    os.write(ch);;
                }
                is.close();
                os.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private boolean doWeUpdate()
    {
        if (mRemoteDate == null)
            return false;
        if (mTargets.size() == 0)
            return false;
        long delta = Math.abs(mLocalDate.getTimeInMillis() - mRemoteDate.getTimeInMillis());
        if (delta < 2*60*1000)
            return false;
        int update = JOptionPane.showConfirmDialog(null, "There is a new version of SMEdit available. Shall we download it?");
        if (update != JOptionPane.YES_OPTION)
            return false;
        return true;
    }
    
    private boolean readRemoteFile()
    {
        try
        {
            URL u = new URL(mWebRoot+"jo_sm.date");
            InputStream is = u.openStream();
            InputStreamReader rdr = new InputStreamReader(is, "iso-8859-1");
            mRemoteDate = readDate(rdr);
            for (;;)
            {
                String line = readLine(rdr);
                if (line == null)
                    break;
                String[] elems = line.split(",");
                mTargets.add(elems);
            }
            mTargets.add(new String[] { "./jo_sm.date", "jo_sm.date" });
            rdr.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    private void readLocalDate()
    {
        mLocalDate = Calendar.getInstance();
        mLocalDate.setTimeInMillis(0);
        File localFile = new File("./jo_sm.date");
        if (!localFile.exists())
            return;
        try
        {
            FileReader rdr = new FileReader(localFile);
            mLocalDate = readDate(rdr);
            rdr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private Calendar readDate(Reader rdr) throws IOException
    {
        Calendar c = Calendar.getInstance();
        String line = readLine(rdr);
        StringTokenizer st = new StringTokenizer(line, " /");
        if (st.countTokens() != 4)
            throw new IllegalArgumentException("Expected 4 tokens on date '"+line+"'");
        st.nextToken(); // skip day of week
        String month = st.nextToken();
        String day = st.nextToken();
        String year = st.nextToken();
        c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        c.set(Calendar.YEAR, Integer.parseInt(year));
        line = readLine(rdr);
        st = new StringTokenizer(line, " :");
        if (st.countTokens() != 3)
            throw new IllegalArgumentException("Expected 4 tokens on date '"+line+"'");
        String hour = st.nextToken();
        String minute = st.nextToken();
        String ampm = st.nextToken();
        c.set(Calendar.HOUR, Integer.parseInt(hour)%12);
        c.set(Calendar.MINUTE, Integer.parseInt(minute));
        if ("AM".equals(ampm))
            c.set(Calendar.AM_PM, Calendar.AM);
        else
            c.set(Calendar.AM_PM, Calendar.PM);
        return c;
    }
    
    private String readLine(Reader rdr) throws IOException
    {
        StringBuffer line = new StringBuffer();
        for (;;)
        {
            int ch = rdr.read();
            if (ch == -1)
                if (line.length() > 0)
                    break;
                else
                    return null;
            if (ch == '\r')
                continue;
            if (ch == '\n')
                break;
            line.append((char)ch);
        }
        return line.toString();
    }
    
    private boolean validateCurrentDirectory()
    {
        File cd = new File(".");
        File jar = new File(cd, "StarMade.jar");
        if (!jar.exists())
            return false;
        return true;
    }

    public static void main(String[] args)
    {
        SMEdit app = new SMEdit();
        app.run();
    }

}

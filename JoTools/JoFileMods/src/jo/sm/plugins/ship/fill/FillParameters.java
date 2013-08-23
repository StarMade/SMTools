package jo.sm.plugins.ship.fill;


public class FillParameters
{
    private int mEmpty;
    private int mThrusters;
    private int mPower;
    private int mWeapon;
    private int mShield;
    private int mSalvage;
    private int mMissileDumb;
    private int mMissileHeat;
    private int mMissileFafo;
    
    public FillParameters()
    {
        mEmpty = 50;
        mThrusters = 20;
        mPower = 5;
        mWeapon = 20;
        mShield = 5;
        mSalvage = 0;
        mMissileDumb = 0;
        mMissileHeat = 0;
        mMissileFafo = 0;
    }

    public int getEmpty()
    {
        return mEmpty;
    }

    public void setEmpty(int empty)
    {
        mEmpty = empty;
    }

    public int getThrusters()
    {
        return mThrusters;
    }

    public void setThrusters(int thrusters)
    {
        mThrusters = thrusters;
    }

    public int getPower()
    {
        return mPower;
    }

    public void setPower(int power)
    {
        mPower = power;
    }

    public int getWeapon()
    {
        return mWeapon;
    }

    public void setWeapon(int weapon)
    {
        mWeapon = weapon;
    }

    public int getShield()
    {
        return mShield;
    }

    public void setShield(int shield)
    {
        mShield = shield;
    }

    public int getSalvage()
    {
        return mSalvage;
    }

    public void setSalvage(int salvage)
    {
        mSalvage = salvage;
    }

    public int getMissileDumb()
    {
        return mMissileDumb;
    }

    public void setMissileDumb(int missileDumb)
    {
        mMissileDumb = missileDumb;
    }

    public int getMissileHeat()
    {
        return mMissileHeat;
    }

    public void setMissileHeat(int missileHeat)
    {
        mMissileHeat = missileHeat;
    }

    public int getMissileFafo()
    {
        return mMissileFafo;
    }

    public void setMissileFafo(int missileFafo)
    {
        mMissileFafo = missileFafo;
    }
}

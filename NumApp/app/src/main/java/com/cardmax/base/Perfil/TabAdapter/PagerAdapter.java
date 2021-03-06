package com.cardmax.base.Perfil.TabAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cardmax.base.Perfil.Fragment.Card;
import com.cardmax.base.Perfil.Fragment.MuropFragment;
import com.cardmax.base.Perfil.Fragment.SavexFragment;
import com.cardmax.base.Perfil.Fragment.ServiciosFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {


    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case  0:
                Card first = new Card();
                return first;
            case 1:
                try {
                    MuropFragment second = new MuropFragment();
                    return second;
                }catch (Exception e){

                    System.out.println("impuesto"+e);

                }

            case 2:
                ServiciosFragment serv = new ServiciosFragment();
                return serv;

            case 3:
                SavexFragment save = new SavexFragment();
                return save;

            default:
                return null;


        }

    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}

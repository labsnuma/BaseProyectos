package com.cardmax.base.Chat;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cardmax.base.Chat.Grupos.GroupsFragment;


public class TabsAccessorAdapter extends FragmentPagerAdapter {
/*    Drawable myDrawable;
    String title = "preguntas";*/

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 2:
                SolicitudesFragment solicitudesFragment = new SolicitudesFragment();
                return solicitudesFragment;

          /*  case 3:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

            case 4:
                BuscarFragment buscrFragment = new BuscarFragment();
                return buscrFragment;*/

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return "chats";
            case 1:

                return "grupos";
            case 2:

                return "solicitudes";

      /*      case 3:
                return "contactos";

            case 4:
                return "buscar";*/

            default:
                return null;
        }


    }


}

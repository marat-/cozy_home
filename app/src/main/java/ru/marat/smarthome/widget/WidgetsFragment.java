/*******************************************************************************
 * Copyright (c) 2017. Marat Shaidullin. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: - Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. - Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Oracle or the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package ru.marat.smarthome.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import ru.marat.smarthome.R;
import ru.marat.smarthome.command.CmdGridFragment;
import ru.marat.smarthome.scenario.ScenariosListFragment;

public class WidgetsFragment extends Fragment {

  public static TabLayout tabLayout;
  public static ViewPager viewPager;
  public static int int_items = 3;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    /**
     *Inflate tab_layout and setup Views.
     */
    View widgetsFamilyView = inflater.inflate(R.layout.widget_tab_layout, null);
    ButterKnife.bind(this, widgetsFamilyView);
    tabLayout = (TabLayout) widgetsFamilyView.findViewById(R.id.tabs);
    viewPager = (ViewPager) widgetsFamilyView.findViewById(R.id.viewpager);

    /**
     *Set an Apater for the View Pager
     */
    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

    /**
     * Now , this is a workaround ,
     * The setupWithViewPager dose't works without the runnable .
     * Maybe a Support Library Bug .
     */

    tabLayout.post(new Runnable() {
      @Override
      public void run() {
        tabLayout.setupWithViewPager(viewPager);
      }
    });

    return widgetsFamilyView;

  }

  class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
      super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return new CmdGridFragment();
        case 1:
          return new ScenariosListFragment();
        default:
          return new ScenariosListFragment();

      }
    }

    @Override
    public int getCount() {

      return int_items;

    }

    /**
     * This method returns the title of the tab according to the position.
     */
    @Override
    public CharSequence getPageTitle(int position) {

      switch (position) {
        case 0:
          return getString(R.string.cmd_fragment_title);
        case 1:
          return getString(R.string.scnr_fragment_title);
      }
      return null;
    }
  }

}

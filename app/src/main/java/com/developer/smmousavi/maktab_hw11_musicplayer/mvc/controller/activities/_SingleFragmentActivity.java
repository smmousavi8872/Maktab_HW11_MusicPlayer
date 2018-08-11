package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;


public abstract class _SingleFragmentActivity extends AppCompatActivity {

  public abstract Fragment createFragment();

  public abstract String getTag();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_single_fragment);

    FragmentManager fm = getSupportFragmentManager();
    Fragment foundFragment = fm.findFragmentById(R.id.fragment_container);

    if (foundFragment == null) {
      foundFragment = createFragment();
      String tag = getTag();
      fm.beginTransaction()
        .add(R.id.fragment_container, foundFragment, tag)
        .commit();
    }
  }
}

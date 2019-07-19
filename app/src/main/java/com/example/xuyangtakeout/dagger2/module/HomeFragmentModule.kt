package com.example.xuyangtakeout.dagger2.module


import com.example.xuyangtakeout.presenter.HomeFragmentPresenter
import com.example.xuyangtakeout.ui.fragment.HomeFragment
import dagger.Module
import dagger.Provides

/**
 * Created by lidongzhi on 2017/8/30.
 */
@Module class HomeFragmentModule(val homeFragment: HomeFragment){

    @Provides fun provideHomeFragmentPresenter(): HomeFragmentPresenter{
        return HomeFragmentPresenter(homeFragment)
    }
}
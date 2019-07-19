package com.example.xuyangtakeout.dagger2.component

import com.example.xuyangtakeout.dagger2.module.HomeFragmentModule
import com.example.xuyangtakeout.ui.fragment.HomeFragment

import dagger.Component

/**
 * Created by lidongzhi on 2017/8/30.
 */
@Component(modules = arrayOf(HomeFragmentModule::class)) interface HomeFragmentComponent {

    fun inject(homeFragment: HomeFragment)
}
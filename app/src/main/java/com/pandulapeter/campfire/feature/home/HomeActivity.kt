package com.pandulapeter.campfire.feature.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.pandulapeter.campfire.HomeBinding
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.data.storage.StorageManager
import com.pandulapeter.campfire.feature.home.downloaded.DownloadedFragment
import com.pandulapeter.campfire.feature.home.favorites.FavoritesFragment
import com.pandulapeter.campfire.feature.home.library.LibraryFragment
import com.pandulapeter.campfire.util.consume
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Displays the home screen of the app which contains the three main pages the user can access
 * ([LibraryFragment], [DownloadedFragment] and [FavoritesFragment]) and the bottom navigation view.
 * The last selected item is persisted.
 *
 * Controlled by [HomeViewModel].
 */
class HomeActivity : DaggerAppCompatActivity() {

    @Inject lateinit var storageManager: StorageManager
    private var selectedItem: NavigationItem = NavigationItem.LIBRARY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and set up the navigation listener.
        val binding = DataBindingUtil.setContentView<HomeBinding>(this, R.layout.activity_home).apply {
            viewModel = HomeViewModel()
            bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.library -> consume { replaceActiveFragment(NavigationItem.LIBRARY) }
                    R.id.downloaded -> consume { replaceActiveFragment(NavigationItem.DOWNLOADED) }
                    R.id.favorites -> consume { replaceActiveFragment(NavigationItem.FAVORITES) }
                    else -> false
                }
            }
        }
        // Restore the state if needed. After app start we need to manually set the selected item, otherwise
        // the View takes care of it and we only need to update the displayed Fragment.
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = when (storageManager.lastSelectedNavigationItem) {
                NavigationItem.LIBRARY -> R.id.library
                NavigationItem.DOWNLOADED -> R.id.downloaded
                NavigationItem.FAVORITES -> R.id.favorites
            }
        } else {
            replaceActiveFragment(storageManager.lastSelectedNavigationItem)
        }
    }

    /**
     * Checks if the user actually changed the current selection and if so, persists it. Replaces the Fragment if
     * the selection changed or the container was empty.
     */
    private fun replaceActiveFragment(navigationItem: NavigationItem) {
        if (selectedItem != navigationItem || supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            storageManager.lastSelectedNavigationItem = navigationItem
            selectedItem = navigationItem
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, when (navigationItem) {
                NavigationItem.LIBRARY -> LibraryFragment()
                NavigationItem.DOWNLOADED -> DownloadedFragment()
                NavigationItem.FAVORITES -> FavoritesFragment()
            }).commit()
        }
    }
}
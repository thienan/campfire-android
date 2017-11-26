package com.pandulapeter.campfire.feature.home.favorites

import android.databinding.ObservableBoolean
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.data.model.SongInfo
import com.pandulapeter.campfire.data.repository.SongInfoRepository
import com.pandulapeter.campfire.feature.home.shared.HomeFragment
import com.pandulapeter.campfire.feature.home.shared.HomeFragmentViewModel
import com.pandulapeter.campfire.feature.home.shared.SongInfoViewModel

/**
 * Handles events and logic for [FavoritesFragment].
 */
class FavoritesViewModel(homeCallbacks: HomeFragment.HomeCallbacks?, songInfoRepository: SongInfoRepository) : HomeFragmentViewModel(homeCallbacks, songInfoRepository) {

    val shouldShowShuffle = ObservableBoolean(false)

    init {
        refreshAdapterItems()
    }

    fun addSongToFavorites(songInfo: SongInfo, position: Int) {
        songInfoRepository.addSongToFavorites(songInfo, position)
        refreshAdapterItems()
    }

    fun removeSongFromFavorites(songInfo: SongInfo) {
        songInfoRepository.removeSongFromFavorites(songInfo)
        refreshAdapterItems()
    }

    fun swapSongPositions(originalPosition: Int, targetPosition: Int) {
        songInfoRepository.swapSongFavoritesPositions(originalPosition, targetPosition)
        refreshAdapterItems()
    }

    fun shuffleItems() {
        songInfoRepository.shuffleFavorites()
        refreshAdapterItems()
    }

    private fun refreshAdapterItems() {
        val downloaded = songInfoRepository.getDownloadedSongs()
        adapter.items = songInfoRepository.getFavoriteIds().mapNotNull { id ->
            downloaded.find { it.id == id }?.let { songInfo ->
                SongInfoViewModel(
                    songInfo = songInfo,
                    actionDescription = R.string.favorites_drag_item_to_rearrange,
                    actionIcon = R.drawable.ic_drag_handle_24dp,
                    isActionTinted = false)
            }
        }
        shouldShowShuffle.set(adapter.itemCount > 2)
    }
}
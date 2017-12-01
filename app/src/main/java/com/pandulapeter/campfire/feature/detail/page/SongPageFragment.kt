package com.pandulapeter.campfire.feature.detail.page

import android.os.Bundle
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.SongPageBinding
import com.pandulapeter.campfire.feature.shared.CampfireFragment

/**
 * Displays lyrics and chords to a single song.
 *
 * Controlled by [SongPageViewModel].
 */
class SongPageFragment : CampfireFragment<SongPageBinding, SongPageViewModel>(R.layout.fragment_song_page) {

    override val viewModel by lazy { SongPageViewModel(arguments.songId) }

    companion object {
        private const val SONG_ID = "song_id"
        private val Bundle?.songId
            get() = this?.getString(SONG_ID) ?: ""

        fun newInstance(songId: String) = SongPageFragment().apply {
            arguments = Bundle().apply {
                putString(SONG_ID, songId)
            }
        }
    }
}
package com.pandulapeter.campfire.feature.home.managePlaylists

import android.os.Bundle
import android.view.View
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentManagePlaylistsBinding
import com.pandulapeter.campfire.feature.shared.TopLevelFragment
import com.pandulapeter.campfire.util.drawable

class ManagePlaylistsFragment : TopLevelFragment<FragmentManagePlaylistsBinding, ManagePlaylistsViewModel>(R.layout.fragment_manage_playlists) {

    override val viewModel = ManagePlaylistsViewModel()
    override var onFloatingActionButtonClicked: (() -> Unit)? = { showSnackbar(R.string.work_in_progress) }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultToolbar.updateToolbarTitle(R.string.home_manage_playlists)
        mainActivity.floatingActionButton.run {
            setImageDrawable(context.drawable(R.drawable.ic_add_24dp))
            show()
        }
    }
}
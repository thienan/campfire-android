package com.pandulapeter.campfire.feature.detail.page

import android.os.Bundle
import android.view.View
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentDetailPageBinding
import com.pandulapeter.campfire.feature.detail.DetailFragment
import com.pandulapeter.campfire.feature.shared.CampfireFragment
import com.pandulapeter.campfire.util.BundleArgumentDelegate
import com.pandulapeter.campfire.util.withArguments

class DetailPageFragment : CampfireFragment<FragmentDetailPageBinding, DetailPageViewModel>(R.layout.fragment_detail_page) {

    companion object {
        private var Bundle.songId by BundleArgumentDelegate.String("songId")

        fun newInstance(songId: String) = DetailPageFragment().withArguments {
            it.songId = songId
        }
    }

    override val viewModel by lazy { DetailPageViewModel(arguments?.songId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (parentFragment as? DetailFragment)?.onDataLoaded()
    }
}
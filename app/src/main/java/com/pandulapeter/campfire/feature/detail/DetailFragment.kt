package com.pandulapeter.campfire.feature.detail

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentDetailBinding
import com.pandulapeter.campfire.feature.shared.TopLevelFragment
import com.pandulapeter.campfire.util.*


class DetailFragment : TopLevelFragment<FragmentDetailBinding, DetailViewModel>(R.layout.fragment_detail) {

    companion object {
        private var Bundle.songId by BundleArgumentDelegate.String("songId")
        private var Bundle.playlistId by BundleArgumentDelegate.String("playlistId")

        fun newInstance(songId: String, playlistId: String = "") = DetailFragment().withArguments {
            it.songId = songId
            it.playlistId = playlistId
        }
    }

    override val viewModel by lazy { DetailViewModel(arguments.songId) }
    override var onFloatingActionButtonClicked: (() -> Unit)? = { toggleAutoScroll() }
    override val navigationMenu = R.menu.detail
    private val drawablePlayToPause by lazy { context.animatedDrawable(R.drawable.avd_play_to_pause_24dp) }
    private val drawablePauseToPlay by lazy { context.animatedDrawable(R.drawable.avd_pause_to_play_24dp) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fun createTransition(delay: Long) = TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addTransition(ChangeImageTransform())
            .apply {
                ordering = TransitionSet.ORDERING_TOGETHER
                startDelay = delay
            }
        sharedElementEnterTransition = createTransition(50)
        sharedElementReturnTransition = createTransition(0)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        defaultToolbar.updateToolbarTitle("Title", "Subtitle")
        if (savedInstanceState == null) {
            mainActivity.transformMainToolbarButton(true)
        }
        mainActivity.floatingActionButton.run {
            setImageDrawable(context.drawable(R.drawable.ic_play_24dp))
            show()
        }
        mainActivity.autoScrollControl.visibleOrGone = false
        binding.textView.text = "Song: ${arguments.songId}\nPlaylist: ${arguments.playlistId}"
        (view?.parent as? ViewGroup)?.run {
            viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver?.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if (mainActivity.autoScrollControl.visibleOrInvisible) {
            toggleAutoScroll()
        }
    }

    override fun onResume() {
        super.onResume()
        (mainActivity.autoScrollControl.tag as? Animator)?.let {
            it.cancel()
            mainActivity.autoScrollControl.tag = null
        }
        mainActivity.autoScrollControl.visibleOrInvisible = false
        mainActivity.floatingActionButton.setImageDrawable(context.drawable(R.drawable.ic_play_24dp))
    }

    override fun onBackPressed() = if (mainActivity.autoScrollControl.visibleOrInvisible) {
        toggleAutoScroll()
        true
    } else super.onBackPressed()

    override fun inflateToolbarButtons(context: Context) = listOf(
        context.createToolbarButton(R.drawable.ic_playlist_border_24dp) { showSnackbar("Work in progress") },
        context.createToolbarButton(R.drawable.ic_song_options_24dp) { mainActivity.openSecondaryNavigationDrawer() }
    )

    override fun onDrawerStateChanged(state: Int) {
        if (mainActivity.autoScrollControl.visibleOrInvisible) {
            toggleAutoScroll()
        }
    }

    private fun toggleAutoScroll() = mainActivity.autoScrollControl.run {
        if (tag == null) {
            val drawable = if (visibleOrInvisible) drawablePauseToPlay else drawablePlayToPause
            mainActivity.floatingActionButton.setImageDrawable(drawable)
            animatedVisibilityEnd = !animatedVisibilityEnd
            drawable?.start()
        }
    }
}
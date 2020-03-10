package com.example.share_music_movil.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.share_music_movil.R

class playlistFragment : Fragment() {

    private lateinit var playListModel: playlistModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playListModel =
                ViewModelProviders.of(this).get(playlistModel::class.java)
        val root = inflater.inflate(R.layout.fragment_playlist, container, false)
        val textView: TextView = root.findViewById(R.id.text_playlist)
        playListModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}

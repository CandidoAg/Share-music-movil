package com.example.share_music_movil.ui.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.share_music_movil.R

class favFragment : Fragment() {

    private lateinit var FavViewModel: favViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        FavViewModel =
                ViewModelProviders.of(this).get(favViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_fav, container, false)
        val textView: TextView = root.findViewById(R.id.text_fav)
        FavViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
